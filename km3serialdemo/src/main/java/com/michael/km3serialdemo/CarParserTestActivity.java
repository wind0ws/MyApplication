package com.michael.km3serialdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import android_km3serialport_api.SerialPortBase;
import android_km3serialport_api.attributeevent.AttrChangedEvent;
import android_km3serialport_api.attributeevent.IAttrChangedListener;
import android_km3serialport_api.car.CarProperties;
import android_km3serialport_api.car.CarSerialPort;

/**
 * 车辆传感器检测
 */
public class CarParserTestActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_car_console);

        /*SharedPreferences sp = getSharedPreferences("com.michael.km3serialdemo_preferences", MODE_PRIVATE);
        String path = sp.getString("DEVICE", "");
        int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));

			*//* Check parameters *//*
        if ( (path.length() == 0) || (baudrate == -1)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("错误");
            builder.setMessage(R.string.error_configuration);
            builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CarActivity.this.finish();
                }
            });
            builder.show();
        }

        initControls();
        carProperties = new CarProperties();
        try {
            carSerialPort = new CarSerialPort(path, baudrate, carProperties);
        } catch (IOException e) {
            Toast.makeText(CarActivity.this,"new CarSerialPort时发生IO错误",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            CarActivity.this.finish();
        }
        try {
            carSerialPort.openSerialPort();
        }
        catch (IOException e) {
            Toast.makeText(CarActivity.this,"openSerialPort()时发生IO错误",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Toast.makeText(CarActivity.this,"串口打开过程执行完毕",Toast.LENGTH_SHORT).show();
        carProperties.addListener(new IAttrChangedListener() {
            @Override
            public void onAttrChanged(final AttrChangedEvent event) {
                Toast.makeText(CarActivity.this,"收到数据",Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editTextCarSensor.append(event.toString()+"\n");
                        editTextStatus.setText("Receiving...");
                        //editText.append();
                    }
                });
            }
        });
        Toast.makeText(CarActivity.this,"carProperties的监听器创建完毕",Toast.LENGTH_SHORT).show();*/
     /*   Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                editTextCarSensor.append(msg.what+"  "+msg.arg1+"  "+msg.arg2);
                //super.handleMessage(msg);
            }
        };*/

       // carSerialPort.addListener(handler);

        initControls();
        carProperties=new CarProperties();
        try {
            carSerialPort = new CarSerialPort("/dev/ttys1", 38400, carProperties);
        } catch (IOException e) {
            displayMsg("new carSerialPort发生IO错误 ");
            e.printStackTrace();
        }
        generateData();
        threadHandler=new Handler();//启动一个runable定时加数据
        //carSerialPort.threadAnalyze.start();
        displayMsg("分析线程启动");

        carProperties.addListener(new IAttrChangedListener() {
            @Override
            public void onAttrChanged(final AttrChangedEvent event) {
                //Toast.makeText(CarActivity.this,"收到数据",Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editTextCarSensor.append(event.toString()+"\n");
                        //editTextStatus.setText("Receiving...");
                        //editText.append();
                    }
                });
            }
        });
        threadHandler.postDelayed(runnable, 200);
    }

    boolean isNextData2=false;
    final Runnable runnable=new Runnable() {
        @Override
        public void run() {
            //try {
                if(isNextData2){
                    //carSerialPort.intFIFO.add(data2);
                    for(int i:data2){
                        editTextStatus.append(String.valueOf(i)+" ");
                    }
                }else{
                   // carSerialPort.intFIFO.add(data);
                    for(int i:data){
                        editTextStatus.append(String.valueOf(i)+" ");
                    }
                }
                isNextData2=!isNextData2;
                threadHandler.postDelayed(runnable, 200);
 //          }
// catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    };

    private Handler threadHandler;
    private int[] data;
    private int[] data2;
    private CarSerialPort carSerialPort;
    private CarProperties carProperties;

    private EditText editTextCarSensor,editTextStatus;
    private void initControls() {
        editTextCarSensor = (EditText) this.findViewById(R.id.EditTextCarSensor);
        editTextStatus = (EditText) this.findViewById(R.id.EditTextStatus);
    }

    private void generateData(){
        String str="08 FD 89 02 01 02 10 20 00 50 00 00 EF 12 4C FC FD 88 01 01 20 12 98 79 FB 04 68 82 6B FC FD 88";
        String str2="FD 88 01 01 11 33 01 42 01 04 10 EF 12 4C FC FD 88 01 01 08 43 97 79 FB 04 68 82 6B FC FD 88 01";
        String[] strings=str.split(" ");
        final ArrayList<Integer> serialData=new ArrayList<>();
        for(String s:strings) {
            //carSerialPort.intFIFO.add(Integer.parseInt(s,16));
            int i = Integer.parseInt(s, 16);
            serialData.add(i);
        }
        data = SerialPortBase.convertIntegers(serialData);
        serialData.clear();
        for(String s:str2.split(" ")){
            serialData.add(Integer.parseInt(s, 16));
        }
        data2 = SerialPortBase.convertIntegers(serialData);
    }

    private void displayMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        threadHandler.removeCallbacks(null);
        super.onDestroy();
    }
}
