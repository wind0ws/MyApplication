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
import android_km3serialport_api.car.CarProperties;
import android_km3serialport_api.car.CarSerialPort;

/**
 * 车辆传感器
 */
public class CarConsoleActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_car_console);
        SharedPreferences sp = getSharedPreferences("com.michael.km3serialdemo_preferences", MODE_PRIVATE);
        String path = sp.getString("DEVICE_CAR", "");
        int baudrate = Integer.decode(sp.getString("BAUDRATE_CAR", "-1"));

        if ( (path.length() == 0) || (baudrate == -1)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("错误");
            builder.setMessage(R.string.error_configuration);
            builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CarConsoleActivity.this.finish();
                }
            });
            builder.show();
        }
        initControls();
        clearConsoleHandler=new Handler();
        clearConsoleHandler.postDelayed(runnableClearConsole, 20000);
        carProperties = new CarProperties();
        try {
            carSerialPort = new CarSerialPort(path, baudrate, carProperties);
        } catch (IOException e) {
            displayMsg(e.toString());
            //e.printStackTrace();
            CarConsoleActivity.this.finish();
        }

        //carSerialPort.threadAnalyze.start();

        try {
            carSerialPort.openSerialPort();
        }
        catch (IOException e) {
            displayMsg(e.toString());
           // e.printStackTrace();
        }

        carProperties.addListener(new IAttrChangedListener() {
            @Override
            public void onAttrChanged(final AttrChangedEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editTextCarSensor.append(sensorName((Integer) event.getSource())+"  ");
                        editTextCarSensor.append(event.toString()+"\n");
                        //editTextStatus.setText("Receiving...");
                        //editText.append();
                    }
                });
            }
        });
    }


    private String sensorName(int source){
        switch (source){
            case 10:
                return  "车喇叭";
            case 11:
                return "车速";
            case 12:
                return "方向";
            case 13:
                return "左转";
            case 14:
                return "右转";
            case 15:
                return "远光灯";
            case 16:
                return "近光灯";
            case 17:
                return "示宽灯";
            case 18:
                return "警示灯";
            case 19:
                return "雾灯";
            case 20:
                return "脚刹";
            case 21:
                return "手刹";
            case 22:
                return "刹车气压";
            case 23:
                return "安全带";
            case 24:
                return "驾驶座有无人";
            case 25:
                return "车门";
            case 26:
                return "点火开关";
            case 27:
                return "车头有无人";
            case 28:
                return "车尾有无人";
            case 29:
                return "引擎状态";
            case 30:
                return "引擎速度";
            case 31:
                return "离合";
            case 32:
                return "档位";
            case 33:
                return "油门";
            case 34:
                return "副刹";
            case 35:
                return "座椅调节";
            default:
                return "意外的source："+source;
        }
    }

   /* @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        int[] ints = SerialPortBase.bytesToInts(buffer, size, 0);
        try {
            carSerialPort.intFIFO.add(ints);
        } catch (InterruptedException e) {
            displayMsg("intFIFO.add(ints)出错");
            e.printStackTrace();
        }
        *//*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int[] ints = SerialPortBase.bytesToInts(buffer, size, 0);
                try {
                   carSerialPort.intFIFO.add(ints);
//                    editTextStatus.append(SerialPortBase.hexToString(buffer, size, 0));
//                    editTextStatus.append(" ");
                    for(int i:ints){
                        editTextStatus.append(String.valueOf(i)+" ");
                    }
                } catch (Exception e) {
                    displayMsg("出错了:"+e.getMessage());
                    e.printStackTrace();
                }
            }
        });*//*

    }*/

    private CarSerialPort carSerialPort;
    private CarProperties carProperties;
    private Handler clearConsoleHandler;

    private EditText editTextCarSensor,editTextStatus;
    private void initControls() {
        editTextCarSensor = (EditText) this.findViewById(R.id.EditTextCarSensor);
        editTextStatus = (EditText) this.findViewById(R.id.EditTextStatus);
    }

    final Runnable runnableClearConsole=new Runnable() {
        @Override
        public void run() {
            editTextCarSensor.setText("");
            clearConsoleHandler.postDelayed(runnableClearConsole, 20000);
        }
    };

    private void displayMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() { super.onPause(); }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(carSerialPort.isOpened()){
            carSerialPort.closeSerialPort();
        }
        super.onDestroy();
    }
}
