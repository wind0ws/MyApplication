package com.michael.jiang.earthquake.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.michael.jiang.earthquake.service.EarthQuakeUpdateService;

/**
 * 广播接收器
 */
public class EarthQuakeAlarmReceiver extends BroadcastReceiver {

    public static final String ALARM_ACTION="com.michael.earthquake.ACTION_REFRESH_EARTHQUAKE_ALARM";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentToStartService = new Intent(context, EarthQuakeUpdateService.class);
        context.startService(intentToStartService);
    }
}
