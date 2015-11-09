package com.michael.jiang.myapplication;


import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Jiang on 2014/12/16.
 */
public class MyService extends Service {

  public static  final  String TAG="Wind0ws";

   private MyBinder binder=new MyBinder();


    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate()启动");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e(TAG,"onStart()启动");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand()启动");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestory()启动");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,"onBind(),开始IBinder" );
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG,"onUnbind()启动。");
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {
        MyService getService()
        {
            return MyService.this;
        }
    }

}

