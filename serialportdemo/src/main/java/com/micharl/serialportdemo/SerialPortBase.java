package com.micharl.serialportdemo;

import android.os.Handler;
import android.util.Log;

import com.ieimobile.serial.SerialLib;
import com.micharl.serialportdemo.attributeevent.IAttrChangedListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 串口基类
 */
public abstract class SerialPortBase extends SerialLib implements IHandleSerialMsgListener,
        SerialLib.OnPortDataListener,IAnalyzeSerialData,IAttrChangedListener,IOnSerialClosed {

    public static final String TAG="SerialPort";
    protected ArrayList<Handler> handlerArrayList;

    private String serialDevice="/dev/ttyO1";
    private int serialBaudRate=38400;
    protected Thread threadAnalyze;//分析数据线程，在打开串口时调用start


    public  SerialPortBase(){
        //this.context=context;
        handlerArrayList = new ArrayList<>();
        this.setPortDataListener(this);//设置串口接收数据处理方法，在子类中要实现onReceive()方法
        threadAnalyze = new Thread(parserRunnable);
    }

    public SerialPortBase(java.lang.String path, int baudRate){
        this();
        initSerial(path, baudRate);
    }


    public void initSerial(java.lang.String path, int baudRate) {
         this.serialDevice=path;
        this.serialBaudRate=baudRate;
        this.setRTS(false);//设置RTS
    }

   public boolean openSerial()  {
       if(!this.isOpened()) {
           try {
               this.openDevice(serialDevice, serialBaudRate);
               needAnalyze=true;
               threadAnalyze.start();
           } catch (Exception e) {
               e.printStackTrace();
               Log.d(TAG,"无法打开串口:"+serialDevice+" | "+serialBaudRate);
           }
       }
       return this.isOpened();
   }

    public boolean closeSerial(){
        if(this.isOpened()){
            try {
                this.closeDevice();
                needAnalyze=false;//分析数据线程不需要继续分析了
                this.onSerialClosed();//调用当串口关闭后的回调方法，可以在这里处理废弃的数据
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG,"无法关闭串口:"+serialDevice+"  "+serialBaudRate);
            }
        }
        return !this.isOpened();
    }

    public void resetSerial(){
        this.closeSerial();
        this.openSerial();
    }

    @Override
    public boolean addListener(Handler handler) {
        return handlerArrayList.contains(handler)||handlerArrayList.add(handler);
    }

    @Override
    public boolean removeListener(Handler handler) {
        return !handlerArrayList.contains(handler) || handlerArrayList.remove(handler);
    }

    protected boolean needAnalyze=true;

    /**
     * 解析串口接收数据的runnable
     */
    protected Runnable parserRunnable=new Runnable() {
        @Override
        public void run() {
            while (needAnalyze){
                //在此分析数据
                Log.d(TAG, "解析器线程在分析数据");
                analyze();
            }
        }
    };



//    public String[] getAllDeviceNames(){
//        return serialFinder.getAllDevices();
//    }
//
//    public String[] getAllDevicePaths(){
//        return serialFinder.getAllDevicesPath();
//    }

    /**
     * 将buffer数组转换为int数组
     * @param buffer
     * @return 返回int类型数组
     */
    public  static  int[] bytesToInts(byte[] buffer,int size,int start){
        int[] ints=new int[size];
        for(int i=start;i<start+size;i++){
            ints[i]=buffer[i]&0xff;
        }
        return ints;
    }

    /**
     * 16进制转String
     * @param buf byte[] 16进制
     * @param size 大小
     * @param start 从哪里开始
     * @return
     */
    protected static String hexToString(byte[] buf, int size, int start) {
        StringBuffer strBuf = new StringBuffer(size * 2);
        int i;

        for (i = start; i < start+size; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strBuf.append("0");
            }
            strBuf.append(Integer.toString((int) buf[i] & 0xff, 16)
                    .toUpperCase());

        }
        return strBuf.toString();
    }

    protected static String hexToString(int[] buf,int size ,int start){
        StringBuffer stringBuffer=new StringBuffer(size*2);
        int i;
        for(i=start;i<start+size;i++){
            if((buf[i]&0xff)<0x10) {
                stringBuffer.append("0");
            }
            stringBuffer.append(Integer.toString(buf[i] & 0xff, 16).toUpperCase());
        }
        return stringBuffer.toString();
    }

    /**
     * 将String转为16进制byte
     * @param str 要转换的string
     * @return 返回
     */
    protected static byte[] stringToHex(String str) {
        byte[]  bb= str.getBytes();
        byte[]  strHex=new  byte[2];

        byte[] target;

        if(str.length() % 2 == 0)
            target = new byte[str.length()/2];
        else
            target = new byte[str.length()/2 +1];

        int j = 0;

        for(int  i=0; i < str.length() ; i+=2)   {

            if(i+1 < bb.length) {
                strHex[0]= bb[i];
                strHex[1]= bb[i+1];
            }
            else if(i < bb.length) {
                strHex[0]= '0';
                strHex[1] = bb[i];
            }

            target[j++]=(byte)Integer.parseInt(new  String(strHex), 16);
        }
        return target;
    }

    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
           // ret[i] = iterator.next().intValue();
            ret[i] = iterator.next();
        }
        return ret;
    }


}
