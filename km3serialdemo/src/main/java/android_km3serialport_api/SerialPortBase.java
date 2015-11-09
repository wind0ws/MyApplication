package android_km3serialport_api;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android_km3serialport_api.attributeevent.IAttrChangedListener;
import android_serialport_api.SerialPort;

/**
 * 串口接收基类
 */
public abstract class SerialPortBase  implements IHandleSerialMsgListener,
        IAnalyzeSerialData,IOnSerialClosed,IAttrChangedListener {

    public SerialPortBase(String devicePath, int baudRate, int flags) throws SecurityException, IOException {
        /* Check parameters */
        if ( (devicePath.length() == 0) || (baudRate == -1)) {
            throw new InvalidParameterException();
        }
        this.device = new File(devicePath);
        this.baudRate=baudRate;

        handlerArrayList = new ArrayList<>();
        initField();
    }


    protected static final String TAG = "SerialPort";
    private File device;
    private int baudRate;
    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private boolean needStopReadSerialData;
    private boolean isOpened;
    protected ArrayList<Handler> handlerArrayList;
    protected Thread threadAnalyze;//分析数据线程，在打开串口时调用start  ！！！！！！测试完记得改为protected

    protected abstract void onDataReceived(final byte[] buffer, final int size);


    public void openSerialPort() throws SecurityException,IOException {
        if(!isOpened()&&mSerialPort==null){
            needStopReadSerialData=false;
            isOpened=true;
            needStopAnalyze=false;

            /* Create a receiving thread */
            mReadThread = new ReadThread();
            threadAnalyze = new ParserThread();
             /* Open the serial port */
            mSerialPort = new SerialPort(device, baudRate, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
			if(!mReadThread.isAlive()) {
                mReadThread.start();
            }

            if(!threadAnalyze.isAlive()){
                threadAnalyze.start();
            }
        }
    }

    public void closeSerialPort() {
        if (isOpened()&&mSerialPort != null) {
            needStopReadSerialData=true;
            mSerialPort.close();
            mSerialPort = null;
            isOpened=false;
            needStopAnalyze=true;
            this.onSerialClosed();
        }
    }


    protected void initField(){
        needStopReadSerialData=false;
        needStopAnalyze=false;
        isOpened=false;
    }


    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while(!needStopReadSerialData) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }


    /**
     * 判断串口是否已经打开
     * @return 串口是否打开
     */
    public boolean isOpened() {
        return isOpened;
    }


    @Override
    public boolean addListener(Handler handler) {
        return handlerArrayList.contains(handler)||handlerArrayList.add(handler);
    }

    @Override
    public boolean removeListener(Handler handler) {
        return !handlerArrayList.contains(handler) || handlerArrayList.remove(handler);
    }

    protected boolean needStopAnalyze=false;

   private class ParserThread extends Thread{
       @Override
       public void run() {
           super.run();
           while (!needStopAnalyze){
               analyze();
           }
       }
   }




    /**
     * 将buffer数组转换为int数组
     * @param buffer byte[]数组
     * @param size buffer的实际拥有数据的大小
     * @param start 从哪里开始转换
     * @return 返回int类型数组
     */
    public  static  int[] bytesToInts(byte[] buffer,int size,int start) {
        ArrayList<Integer> integers = new ArrayList<>();
        int temp=-1;
        for (int i = start; i < start + size; i++) {
            //ints[i-start]=buffer[i]&0xff;
            temp = -1;
            try {
                temp = buffer[i]&0xff;
                //temp = (int) buffer[i];
            } catch (Exception e) {
                Log.d(TAG, "byte转int出错。");
                e.printStackTrace();
            }
            //ints[i - start] = buffer[i];
            if(temp>=0) {
                integers.add(temp);
            }
        }
        return convertIntegers(integers);
    }

  /*  public static int[] bytesToInts(byte[] buffer,int size,int start) {
        String hexString = hexToString(buffer, size, start).replace(" ","");
        String tmp="";
        int parseInt=-1;
        ArrayList<Integer> intList = new ArrayList<>();
        if(hexString.length()%2==0){
            for(int i=0;i<hexString.length();i=i+2){
                tmp="";
                parseInt=-1;
                try {
                    tmp=hexString.substring(i,i+2);
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
                if(!tmp.equals("")){
                    try {
                        parseInt=Integer.parseInt(tmp, 16);
                    }catch (Exception e){
                        e.printStackTrace();
                        parseInt=-1;
                    }
                    if(parseInt!=-1) {
                        intList.add(parseInt);
                    }
                }
            }
        }else {
            return null;
        }
        if(intList.size()>0) {
            return convertIntegers(intList);
        }else {
            return null;
        }

    }*/

    /**
     * 16进制转String
     * @param buf byte[] 16进制
     * @param size 大小
     * @param start 从哪里开始
     * @return
     */
    public static String hexToString(byte[] buf, int size, int start) {
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

    public static String hexToString(int[] buf,int size ,int start){
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
