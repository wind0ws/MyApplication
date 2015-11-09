package android_km3serialport_api.car;


import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.io.IOException;

import android_km3serialport_api.SerialPortBase;
import android_km3serialport_api.attributeevent.AttrChangedEvent;

/**
 * 车载板解析
 */
public class CarSerialPort extends SerialPortBase {
    private static CarSerialPort carSerialPort;
    private CarDataParser parser;
    protected IntFIFO intFIFO;//     !!!!!!!!!记得测试完要改回protected

    public CarSerialPort(String deviceName,int baudRate,CarDataParser parser) throws IOException {
        super(deviceName,baudRate,0);
        intFIFO =new IntFIFO(256);//注意此处，如果太小，可能存不下串口来的数据
        this.parser=parser;
        this.parser.getCarProperties().addListener(this);
        carSerialPort=this;
        //super(context);
    }

    public CarSerialPort(String deviceName,int baudRate,CarProperties carProperties) throws IOException {
        super(deviceName,baudRate,0);
        intFIFO =new IntFIFO(256);//注意此处，如果太小，可能存不下串口来的数据
        this.parser=new CarDataParser(carProperties);
        this.parser.getCarProperties().addListener(this);
        carSerialPort=this;
        //super(context);
    }

    /**
     * 返回当前类的一个实例。三个参数仅在实例不存在时初始化时使用
     * @param path 串口设备
     * @param baudRate 波特率
     * @param parser 车载板解析类
     * @return 返回当前类的一个实例
     */
    public  static CarSerialPort getInstance(String path, int baudRate,CarDataParser parser) throws IOException {
       return carSerialPort==null?new CarSerialPort(path,baudRate,parser):carSerialPort;
    }

    /**
     * 返回当前类的一个实例。三个参数仅在实例不存在时初始化时使用
     * @param path 串口设备
     * @param baudRate 波特率
     * @param carProperties 车辆各个传感器属性类
     * @return 返回当前类的一个实例
     */
    public  static CarSerialPort getInstance(String path, int baudRate,CarProperties carProperties) throws IOException {
        return carSerialPort==null?new CarSerialPort(path,baudRate,carProperties):carSerialPort;
    }


    public CarDataParser getParser() {
        return parser;
    }

    @Override
    public void analyze() {
       // int size = 0;
        while ( intFIFO.getSize() > 12) {
            //Log.d("Thresh0ld", "intFIFO的大小：" + size);
            int[] oneFrame = parser.getOneFrameFromQueue(intFIFO,
                    CarDataParser.FRAME_HEAD_SERIAL2PC, CarDataParser.FRAME_TAIL_SERIAL2PC);
            if (oneFrame != null) {
                if (parser.getFunctionCode(oneFrame, CarDataParser.FRAME_HEAD_SERIAL2PC, 0x88, 0x01) == 1) {
                    int[] oneFrameWithoutSpecialChar = parser.getOneFrameWithoutSpecialChar(oneFrame);
                    int[] firstUnit = parser.getFirstUnitFuncOneDataCode(oneFrameWithoutSpecialChar);
                    parser.analyzeFirstUnitFuncOne(firstUnit);
                }
            }
        }
        try {
            Thread.sleep(210);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onSerialClosed() {
        intFIFO.removeAll();
        parser.getCarProperties().init();
    }

    @Override
    protected void onDataReceived(final byte[] buffer,final int size) {
        if(buffer!=null&&size>0){
            try {
                intFIFO.add(bytesToInts(buffer,size,0));
            } catch (InterruptedException e) {
                //Log.d(TAG,"向ByteFIFO增加bytes抛出异常");
                 e.printStackTrace();
            }
//            if((!threadAnalyze.isAlive()) &&intFIFO.getSize()>12){
//                threadAnalyze.start();
//            }
        }
    }

    @Override
    public void onAttrChanged(AttrChangedEvent event) {
        //在类里面新建List 然后每次触发 属性值改变时，把变化收集起来。在分析数据前清空这个list
        //解析完数据再判断这个list是否为0，为0则没有改变，不用发消息，否则把改变的属性发走
        if(handlerArrayList.size()<1){
            return;
        }
        int what=-1,oldVal=-1,newVal=-1;
        try {
         what=Integer.parseInt(event.getSource().toString());
         oldVal=Integer.parseInt(event.getOldValue().toString());
         newVal=Integer.parseInt(event.getNewValue().toString());}
        catch (Exception e) {
            Log.d(TAG, "转换属性事件到int出错。");
        }
        for(Handler handler:handlerArrayList){
            if(handler!=null){
                Message msg=handler.obtainMessage();
                msg.what=what;
                msg.arg1 = oldVal;
                msg.arg2 = newVal;
                //msg.obj=parser.getCarProperties();
                msg.sendToTarget();
            }
        }

    }
}
