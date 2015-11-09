package android_km3serialport_api.gps;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android_km3serialport_api.SerialPortBase;
import android_km3serialport_api.attributeevent.AttrChangedEvent;

/**
 * Gps串口数据接收处理类
 */
public class GpsSerialPort extends SerialPortBase {

    private static GpsSerialPort gpsSerialPort;
    private StringBuffer stringBuffer;//测试完记得改回private！！！！！！！
    private GpsDataParser parser;

    //private final byte[] byteLock = new byte[0];

    public GpsSerialPort(String path, int baudRate,GpsDataParser parser) throws IOException {
        super(path, baudRate,0);
        stringBuffer=new StringBuffer();
        this.parser=parser;
        gpsSerialPort=this;
    }

    public GpsSerialPort(String path, int baudRate, GpsProperties gpsProperties) throws IOException {
        super(path, baudRate,0);
        stringBuffer=new StringBuffer();
        this.parser=new GpsDataParser(gpsProperties);
        gpsSerialPort=this;
    }

    /**
     * 返回当前类的一个实例，两个参数仅在实例不存在时初始化时使用
     * @param path 串口设备
     * @param baudRate 波特率
     * @param parser 解析器
     * @return 返回当前类的一个实例
     */
    public  static GpsSerialPort getInstance(String path, int baudRate,GpsDataParser parser) throws IOException {
        return gpsSerialPort==null?new GpsSerialPort(path,baudRate,parser):gpsSerialPort;
    }

    /**
     * 返回当前类的一个实例，两个参数仅在实例不存在时初始化时使用
     * @param path 串口设备
     * @param baudRate 波特率
     * @param gpsProperties Gps属性
     * @return 返回当前类的一个实例
     */
    public  static GpsSerialPort getInstance(String path, int baudRate,GpsProperties gpsProperties) throws IOException {
        return gpsSerialPort==null?new GpsSerialPort(path,baudRate,gpsProperties):gpsSerialPort;
    }

    public GpsDataParser getParser() {
        return parser;
    }

    //String tmp="";

    @Override
    public void analyze() {
        //在这里调用parser解析数据

        //synchronized (byteLock){

            if(stringBuffer.length()>=86){
                int head = -1;
                while((head =stringBuffer.indexOf("$"))!=-1) {
                int nextHead = -1;
                int tail = -1;

                if (head != 0) {
                    stringBuffer.delete(0, head);
                    head = stringBuffer.indexOf("$");
                }

                tail = stringBuffer.indexOf("*", head);
                nextHead = stringBuffer.indexOf("$", head + 1);
                if (nextHead != -1 && tail > nextHead) {
                    stringBuffer.delete(head, nextHead);
                    head = stringBuffer.indexOf("$");
                    nextHead = stringBuffer.indexOf("$", head + 1);
                    tail = stringBuffer.indexOf("*", head);
                }

                if (head != -1 && tail != -1) {
                    if (tail + 3 <= stringBuffer.length()) {
                        String gpsData = stringBuffer.substring(head, tail + 3);
                        stringBuffer.delete(head, tail + 3);
                        parser.parseGpsProtocol(gpsData);
                        //System.out.println("GPS数据 长度："+gpsData.length()+"  内容："+gpsData);
                    } else {
                        stringBuffer.setLength(0);
                    }
                } else {
                    stringBuffer.setLength(0);
                }
            }

        // }

            /*if(stringBuffer.length()>=86){
            int headIndex=-1;
            headIndex=stringBuffer.indexOf("$GPGGA");
            if(headIndex!=-1){
                stringBuffer.delete(0,headIndex);
                headIndex = stringBuffer.indexOf("$GPGGA");
                if(stringBuffer.length()-headIndex>=86){
                    parser.parseGpsProtocol(stringBuffer.substring(headIndex,86));
                    stringBuffer.delete(0,86);
                }
            }else {
                stringBuffer.setLength(0);
            }
            }*/

          /* if( stringBuffer.indexOf("$")!=0){
            stringBuffer.setLength(0);
            }else if(stringBuffer.length()==86){
               parser.parseGpsProtocol(stringBuffer.toString());
               stringBuffer.setLength(0);
            }*/
        }else{
                try {
                    Thread.sleep(210);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
          }

    }



    @Override
    public void onSerialClosed() {
        //strBuffer.delete(0, strBuffer.length());//清空strBuffer strBuffer.setLength(0);
        stringBuffer.setLength(0);
        parser.timeSpan=-1;
        parser.lastCoordinate=new GpsCoordinate(0, 0, 0);
        parser.getGpsProperties().init();
    }

    @Override
    protected void onDataReceived(final byte[] buffer,final int size) {
        //String gpsStrData = new String(buffer, 0, size);
       // synchronized (byteLock){
           // stringBuffer.append(new String(buffer, 0, size).replace(" ","").trim());
             stringBuffer.append(new String(buffer, 0, size));
       // }
//        Collections.addAll(stringList, singleGpsData.split(","));
//        if ((!threadAnalyze.isAlive()) && stringBuffer.length() > 0) {
//            threadAnalyze.start();//启动分析数据线程
//        }
    }

    @Override
    public void onAttrChanged(AttrChangedEvent event) {
        if(handlerArrayList.size()<1){
            return;
        }
        Object[] objects=new Object[2];
        objects[1]=event.getOldValue();
        objects[2] = event.getNewValue();
        int what=Integer.parseInt(event.getSource().toString());
        for(Handler handler:handlerArrayList){
            if(handler!=null){
                Message msg=handler.obtainMessage();
                msg.what = what;
                msg.obj=objects;
                msg.sendToTarget();
            }
        }
    }
}
