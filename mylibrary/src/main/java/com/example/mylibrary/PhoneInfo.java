package com.example.mylibrary;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 获取手机信息
 */
public class PhoneInfo {
    private String TAG = "PhoneInfo";
    private Context mContext;
    private TelephonyManager mPhoneManager;

    public PhoneInfo(Context context) {
        mContext = context;
        mPhoneManager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取设备ID
     * @return 返回设备ID号，String类型
     */
    public String getDeviceId() {
        return mPhoneManager.getDeviceId();
    }

    /**
     * 获取设备的型号名称
     * @return 型号名称，String类型
     */
    public String getPhoneModule() {
        return Build.MODEL;
    }

    /**
     * 获取设备的序列号
     * @return 返回设备序列号
     */
    public String getSerialNumber() {
        return Build.SERIAL;
    }

    /**
     * 获取设备的电话号码
     * @return 电话号码，String类型
     */
    public String getPhoneNumber() {
        return mPhoneManager.getLine1Number();
    }

    /**
     * 获取Wifi的MAC地址
     * @return MAC地址，String类型
     */
    public String getWiFiMacAddress(){
        String result = "";
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        Log.i(TAG, "macAdd:" + result);
        return result;
    }

    /**
     * 获取CPU信息
     * @return 数组包含两个元素，第一个是CPU型号，第二个是CPU频率
     */
    public String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
            Log.d(TAG,"捕获到IOException异常，在getCpuInfo（）中");
            e.printStackTrace();
        }
        Log.i(TAG, "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
        return cpuInfo;
    }

    /**
     * 获取系统总内存（RAM）
     * @return 内存大小，可能为MB或KB（系统自动转换）
     */
    public String getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

       try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            //initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            initial_memory = Integer.valueOf(arrayOfString[1]) * 1024;
            localBufferedReader.close();

        } catch (IOException e) {
           Log.d(TAG,"捕获IOException，在getTotalMemory（）中");
           e.printStackTrace();
       }
        return Formatter.formatFileSize(mContext, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }
}
