package com.michael.km3serialdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import android_km3serialport_api.attributeevent.AttrChangedEvent;
import android_km3serialport_api.attributeevent.IAttrChangedListener;
import android_km3serialport_api.gps.EGpsProtocolType;
import android_km3serialport_api.gps.GpsCoordinate;
import android_km3serialport_api.gps.GpsProperties;
import android_km3serialport_api.gps.GpsSerialPort;

/**
 * Gps协议解析
 */
public class GpsConsoleActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_gps_console);

        SharedPreferences sp = getSharedPreferences("com.michael.km3serialdemo_preferences", MODE_PRIVATE);
        String path = sp.getString("DEVICE_GPS", "");
        String gpsProtocl=sp.getString("GPS_PROTOCOL","");
        int baudrate = Integer.decode(sp.getString("BAUDRATE_GPS", "-1"));

        if ( (path.length() == 0) || (baudrate == -1)||(gpsProtocl.length()==0)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("错误");
            builder.setMessage(R.string.error_configuration);
            builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GpsConsoleActivity.this.finish();
                }
            });
            builder.show();
        }

        initControls();
        clearConsoleHandler = new Handler();
        clearConsoleHandler.postDelayed(clearConsoleRunnable, 8000);

        gpsProperties = new GpsProperties(new GpsCoordinate(31.86, 117.21,26.11));

        try {
            gpsSerialPort = new GpsSerialPort(path, baudrate, gpsProperties);
        } catch (IOException e) {
            displayMsg("new GpsSerialPort()时出错");
            e.printStackTrace();
        }

        switch (gpsProtocl){
                default:
                case "$GPGGA":
                    editGpsStatus.setText("当前GPS解析协议：GPGGA");
                    gpsSerialPort.getParser().setGpsProtocolType(EGpsProtocolType.GPS_PROTOCOL_GPGGA);
                    break;
                case "$GPRMC":
                    editGpsStatus.setText("当前GPS解析协议：GPRMC");
                    gpsSerialPort.getParser().setGpsProtocolType(EGpsProtocolType.GPS_PROTOCOL_GPRMC);
                    break;
            case "$All":
                    editGpsStatus.setText("当前GPS解析协议：GPGGA和GPRMC");
                     gpsSerialPort.getParser().setGpsProtocolType(EGpsProtocolType.GPS_PROTOCOL_ALL);
                String timeStyle = sp.getString("GPS_TIME_STYLE", "");
                String calSpeedStyle = sp.getString("GPS_CAL_SPEED", "");
                if(!timeStyle.isEmpty()){
                    if(timeStyle.equals("$GPGGA")){
                        gpsSerialPort.getParser().setTimeStyle(EGpsProtocolType.GPS_PROTOCOL_GPGGA);
                    }else if(timeStyle.equals("$GPRMC")){
                        gpsSerialPort.getParser().setTimeStyle(EGpsProtocolType.GPS_PROTOCOL_GPRMC);
                    }
                }
                if(!calSpeedStyle.isEmpty()){
                    if(calSpeedStyle.equals("$GPGGA")){
                        gpsSerialPort.getParser().setCalSpeedStyle(EGpsProtocolType.GPS_PROTOCOL_GPGGA);
                    }else if(calSpeedStyle.equals("$GPRMC")){
                        gpsSerialPort.getParser().setCalSpeedStyle(EGpsProtocolType.GPS_PROTOCOL_GPRMC);
                    }
                }
                    break;
            }

        gpsProperties.addListener(new IAttrChangedListener() {
            @Override
            public void onAttrChanged(final AttrChangedEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editGpsSensor.append(sensorName((Integer) event.getSource())+"  ");
                        editGpsSensor.append(event.toString());
                        editGpsSensor.append("\n");
                    }
                });
            }
        });

        try {
            gpsSerialPort.openSerialPort();
        } catch (IOException e) {
            displayMsg("打开GPS串口出错.");
            e.printStackTrace();
        }
    }

    private GpsProperties gpsProperties;
    private GpsSerialPort gpsSerialPort;

    private void initControls() {
        editGpsSensor = (EditText) this.findViewById(R.id.EditTextGpsSensor);
        editGpsStatus = (EditText) this.findViewById(R.id.EditTextGpsStatus);
    }

    private EditText editGpsSensor;
    private EditText editGpsStatus;
    private Handler clearConsoleHandler;

    final Runnable clearConsoleRunnable=new Runnable() {
        @Override
        public void run() {
            editGpsSensor.setText("");
            clearConsoleHandler.postDelayed(clearConsoleRunnable, 8000);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    protected void onDestroy() {
        if(gpsSerialPort.isOpened()){
            gpsSerialPort.closeSerialPort();
        }
        super.onDestroy();
    }

    private String sensorName(int source){
        switch (source){
            case 51:
                return "基准站坐标";
            case 52:
                return "卫星数量";
            case 53:
                return "当前坐标";
            case 54:
                return "当前时间";
            case 55:
                return "即时速度";
            case 56:
                return "定位质量";
            case 57:
                return "方位角";
            case 58:
                return "磁偏角";
            case 59:
                return "磁偏角方向";
            default:
                return "意外的source："+source;
        }
    }

    private void displayMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
