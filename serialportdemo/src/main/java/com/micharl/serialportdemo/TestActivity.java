package com.micharl.serialportdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.micharl.serialportdemo.attributeevent.AttrChangedEvent;
import com.micharl.serialportdemo.attributeevent.IAttrChangedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
    }

    CarSerialPort carSerialPort;
    CarProperties carProperties;

    private void init() {
        Button  btnTestCarSensor = (Button) this.findViewById(R.id.btnTestCarSensor);
        final EditText editText = (EditText) this.findViewById(R.id.editResult);
        carProperties=new CarProperties();
        carSerialPort = new CarSerialPort("/dev/ttymxc1", 38400, carProperties);
        carProperties.addListener(new IAttrChangedListener() {
            @Override
            public void onAttrChanged(final AttrChangedEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       editText.append(event.toString()+"\n");
                        //editText.append();
                    }
                });
            }
        });

        btnTestCarSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if( carSerialPort.openSerial()){
                   Toast.makeText(TestActivity.this,"串口打开成功",Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(TestActivity.this,"串口打开失败",Toast.LENGTH_SHORT).show();
               }

               /* String str="FD 88 02 01 00 00 00 00 00 00 00 EF 12 4C FC FD 88 01 01 00 00 98 79 FB 04 68 82 6B FC FD 88";
                String[] strings=str.split(" ");
                IntFIFO intFIFO=new IntFIFO(512);
                for(String s:strings){
                    try {
                        intFIFO.add(Integer.parseInt(s,16));
                    } catch (InterruptedException e) {
                        Log.d("Thresh0ld", "转换出错");
                        e.printStackTrace();
                    }
                }
                CarProperties carProperties=new CarProperties();
                CarDataParser parser = new CarDataParser(carProperties);
                while (!intFIFO.isEmpty()) {
                    Log.d("Thresh0ld", "intFIFO不为空，在执行");
                   int[] oneFrame=  parser.getOneFrameFromQueue(intFIFO,
                           CarDataParser.FRAME_HEAD_SERIAL2PC,CarDataParser.FRAME_TAIL_SERIAL2PC);
                    if(oneFrame!=null){
                       if(parser.getFunctionCode(oneFrame,CarDataParser.FRAME_HEAD_SERIAL2PC,0x88,0x01)==1){
                           int[] oneFrameWithoutSpecialChar=  parser.getOneFrameWithoutSpecialChar(oneFrame);
                           int[] firstUnit= parser.getFirstUnitFuncOneDataCode(oneFrameWithoutSpecialChar);
                           parser.analyzeFirstUnitFuncOne(firstUnit);
                       }
                    }
                }
                StringBuilder sb=new StringBuilder();
               sb.append("车喇叭："+carProperties.getCarHorn());
                sb.append("  档位："+carProperties.getCarGear());
                sb.append(" 方向:"+carProperties.getCarDirection());*/

           /*     GpsProperties gpsProperties = new GpsProperties(new GpsCoordinate(3151.7845732, 11712.5547029, 25.704));
                GpsDataParser parser = new GpsDataParser(gpsProperties);
                String oneGpsData="$GPGGA,015829.60,3151.7845503,N,11712.5538789,E,4,13,1.0,25.681,M,-3.305,M,1.6,0333*66";
                String secondGpsData="$GPGGA,015830.60,3151.7845513,N,11712.5538799,E,4,13,1.0,25.681,M,-3.305,M,1.6,0333*66";

                List<String> stringList = new ArrayList<String>();
                Collections.addAll(stringList,oneGpsData.split(","));
                parser.parseGpsProtocol(stringList);

                stringList.clear();
                Collections.addAll(stringList, secondGpsData.split(","));
                parser.parseGpsProtocol(stringList);
                String text = "速度："+gpsProperties.getImmediatelySpeed()+" 卫星数量："+gpsProperties.getSatelliteAmount()
                        +"  定位质量："+gpsProperties.getPositioningQuality()+" 经度:"+gpsProperties.getGpsCoordinate().longitude
                        +" 纬度："+gpsProperties.getGpsCoordinate().latitude+" 高度："+gpsProperties.getGpsCoordinate().altitude;*/
                // editText.setText(text);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
