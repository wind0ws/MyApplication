package com.micharl.serialportdemo;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.micharl.serialportdemo.attributeevent.AttrChangedEvent;
import com.micharl.serialportdemo.attributeevent.IAttrChangedListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 车载板解析
 */
public class CarSerialPort extends SerialPortBase {
    private static CarSerialPort carSerialPort;
    private CarDataParser parser;
    protected IntFIFO intFIFO;

    public CarSerialPort(String deviceName,int baudRate,CarDataParser parser) {
        super();
        intFIFO =new IntFIFO(512);//注意此处，如果太小，可能存不下串口来的数据
        this.parser=parser;
        this.parser.getCarProperties().addListener(this);
        carSerialPort=this;
        //super(context);
    }

    public CarSerialPort(String deviceName,int baudRate,CarProperties carProperties) {
        super();
        intFIFO =new IntFIFO(512);//注意此处，如果太小，可能存不下串口来的数据
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
    public  static CarSerialPort getInstance(java.lang.String path, int baudRate,CarDataParser parser){
       return carSerialPort==null?new CarSerialPort(path,baudRate,parser):carSerialPort;
    }

    /**
     * 返回当前类的一个实例。三个参数仅在实例不存在时初始化时使用
     * @param path 串口设备
     * @param baudRate 波特率
     * @param carProperties 车辆各个传感器属性类
     * @return 返回当前类的一个实例
     */
    public  static CarSerialPort getInstance(java.lang.String path, int baudRate,CarProperties carProperties){
        return carSerialPort==null?new CarSerialPort(path,baudRate,carProperties):carSerialPort;
    }


    @Override
    public void onReceived(byte[] bytes, int size) {
        if(bytes!=null&&bytes.length>0){
            try {
                intFIFO.add(bytesToInts(bytes,size,0));
            } catch (InterruptedException e) {
                //Log.d(TAG,"向ByteFIFO增加bytes抛出异常");
               // e.printStackTrace();
            }
            if((!threadAnalyze.isAlive()) &&intFIFO.getSize()>12){
                threadAnalyze.start();
            }
        }
    }


    @Override
    public void analyze() {
        while (intFIFO.getSize()>12){
            Log.d(TAG, "车载板analyze（）在分析数据（intFIFO在解析数据）");
            int[] oneFrame=  parser.getOneFrameFromQueue(intFIFO,
                    CarDataParser.FRAME_HEAD_SERIAL2PC,CarDataParser.FRAME_TAIL_SERIAL2PC);
            if(oneFrame!=null){
                if(parser.getFunctionCode(oneFrame,CarDataParser.FRAME_HEAD_SERIAL2PC,0x88,0x01)==1){
                    int[] oneFrameWithoutSpecialChar=  parser.getOneFrameWithoutSpecialChar(oneFrame);
                    int[] firstUnit= parser.getFirstUnitFuncOneDataCode(oneFrameWithoutSpecialChar);
                    parser.analyzeFirstUnitFuncOne(firstUnit);
                }
            }}
    }

    @Override
    public void onAttrChanged(AttrChangedEvent event) {
        //在类里面新建List 然后每次触发 属性值改变时，把变化收集起来。在分析数据前清空这个list
        //解析完数据再判断这个list是否为0，为0则没有改变，不用发消息，否则把改变的属性发走
        int what=Integer.parseInt(event.getSource().toString());
        int oldVal=Integer.parseInt(event.getOldValue().toString());
        int newVal=Integer.parseInt(event.getNewValue().toString());
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

    @Override
    public void onSerialClosed() {
        intFIFO.removeAll();
        parser.getCarProperties().init();
    }
}
