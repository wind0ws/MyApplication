package com.micharl.serialportdemo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.micharl.serialportdemo.attributeevent.AttrChangedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gps串口数据接收处理类
 */
public class GpsSerialPort extends SerialPortBase {

    private static GpsSerialPort gpsSerialPort;
    private List<String> stringList;
    private GpsDataParser parser;

    public GpsSerialPort(String path, int baudRate,GpsDataParser parser) {
        super(path, baudRate);
        stringList=new ArrayList<>();
        this.parser=parser;
        gpsSerialPort=this;
    }

    public GpsSerialPort(String path, int baudRate,GpsProperties gpsProperties) {
        super(path, baudRate);
        stringList=new ArrayList<>();
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
    public  static GpsSerialPort getInstance(java.lang.String path, int baudRate,GpsDataParser parser){
        return gpsSerialPort==null?new GpsSerialPort(path,baudRate,parser):gpsSerialPort;
    }

    /**
     * 返回当前类的一个实例，两个参数仅在实例不存在时初始化时使用
     * @param path 串口设备
     * @param baudRate 波特率
     * @param gpsProperties Gps属性
     * @return 返回当前类的一个实例
     */
    public  static GpsSerialPort getInstance(java.lang.String path, int baudRate,GpsProperties gpsProperties){
        return gpsSerialPort==null?new GpsSerialPort(path,baudRate,gpsProperties):gpsSerialPort;
    }


    @Override
    public void onReceived(byte[] bytes, int i) {
        stringList.clear();
        String singleGpsData = new String(bytes, 0, i);
        Collections.addAll(stringList, singleGpsData.split(","));
        if((!threadAnalyze.isAlive())&&stringList.size()>0){
            threadAnalyze.start();//启动分析数据线程
        }
    }

    @Override
    public void analyze() {
        if(stringList.size()<15){
            Log.d(TAG,"GPGGA数据不足15位,此次分析被取消");
            stringList.clear();
            return;
        }
        //在这里调用parser解析数据



    }

    @Override
    public void onAttrChanged(AttrChangedEvent event) {
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

    @Override
    public void onSerialClosed() {
        //strBuffer.delete(0, strBuffer.length());//清空strBuffer strBuffer.setLength(0);
        stringList.clear();
        parser.timeSpan=-1;
        parser.lastCoordinate=new GpsCoordinate(0, 0, 0);
        parser.getGpsProperties().init();
    }
}
