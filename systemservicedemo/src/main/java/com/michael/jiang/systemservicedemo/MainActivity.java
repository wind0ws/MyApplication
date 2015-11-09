package com.michael.jiang.systemservicedemo;

import android.app.ActivityManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNetwork:
                ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiNetworkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo bluetoothNetworkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
                String toastMsg="当前蜂窝数据网络：";
                if(mobileNetworkInfo!=null){
                if(mobileNetworkInfo.isConnectedOrConnecting())
                {
                    toastMsg+="已断开";
                }else {
                    toastMsg+="已开启";
                } }
                toastMsg+=" 当前wifi：";
                if(wifiNetworkInfo!=null){
                if(wifiNetworkInfo.isConnectedOrConnecting())
                {
                    toastMsg+="已开启";

                }else{
                    toastMsg+="已断开";
                }}
                toastMsg+="  当前蓝牙：";
                if(bluetoothNetworkInfo!=null)
                {
                if(bluetoothNetworkInfo.isConnectedOrConnecting())
                {
                    toastMsg+="已开启";
                }else
                {
                    toastMsg+="已断开";
                }}
                Toast.makeText(this,toastMsg,Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnOpenCloseWiFi:
                WifiManager wifiManager = (WifiManager) this.getSystemService(WIFI_SERVICE);
                int wifiState = wifiManager.getWifiState();
                if(wifiState==WifiManager.WIFI_STATE_ENABLED)
                {
                    Toast.makeText(this,"当前Wifi已打开，即将为您关闭Wifi",Toast.LENGTH_SHORT).show();
                    wifiManager.setWifiEnabled(false);

                }else if(wifiState==WifiManager.WIFI_STATE_DISABLED){
                    Toast.makeText(this,"当前Wifi已关闭，即将为您打开Wifi",Toast.LENGTH_SHORT).show();
                    wifiManager.setWifiEnabled(true);
                }
                break;
            case R.id.btnVolumeState:
                AudioManager audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
                int maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
                int currentMediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                Toast.makeText(this,"当前系统最大音量："+maxVolume+" 当前媒体音量："+currentMediaVolume,Toast.LENGTH_SHORT ).show();
                break;
            case R.id.btnGetPkgName:
                ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
               String packageName= activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
                Toast.makeText(this,packageName,Toast.LENGTH_SHORT).show();
                break;
        }
    }
}