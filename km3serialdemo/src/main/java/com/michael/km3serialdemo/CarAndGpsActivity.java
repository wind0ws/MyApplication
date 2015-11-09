package com.michael.km3serialdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidParameterException;

import android_km3serialport_api.attributeevent.AttrChangedEvent;
import android_km3serialport_api.attributeevent.IAttrChangedListener;
import android_km3serialport_api.car.CarProperties;
import android_km3serialport_api.car.CarSerialPort;
import android_km3serialport_api.gps.EGpsProtocolType;
import android_km3serialport_api.gps.GpsCoordinate;
import android_km3serialport_api.gps.GpsProperties;
import android_km3serialport_api.gps.GpsSerialPort;

/**
 * 车载设备检测
 */
public class CarAndGpsActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargps);

        initControls();
        initSerial();
        listenCarProperties();
        listenGpsProperties();

    }

    private CarProperties carProperties;
    private CarSerialPort carSerialPort;
    private GpsProperties gpsProperties;
    private GpsSerialPort gpsSerialPort;



    private TextView txtCarHorn;
    //private TextView carVelocity;
    private TextView txtCarDirection;
    private TextView txtLeftTurn;
    private TextView txtRightTurn;
    private TextView txtHighBeam;
    private TextView txtLowBeam;
    private TextView txtWidthLamp;
    private TextView txtWarningLamp;
    private TextView txtFogLamp;
    private TextView txtFootBrake;
    private TextView txtHandBrake;
    private TextView txtBrakePressure;
    private TextView txtLifeBelt;
    private TextView txtSbOnSeat;
    private TextView txtCarDoor;
    private TextView txtIgnitionSwitch;
    private TextView txtCarHeadState;
    private TextView txtCarTailState;
    private TextView txtEngineState;
    private EditText editEngineSpeed;
    private TextView txtCarClutch;
    private EditText editCarGear;
    private TextView txtCarAccelerator;
    private TextView txtViceBrake;
    private TextView txtSeatAdjust;

    private EditText editSatelliteAmount;
    private EditText editGpsCoordinate;
    private EditText editRealTime;
    private EditText editImmediatelySpeed;
    private EditText editPositioningQuality;
    private EditText editAzimuth;
    private EditText editMagneticDeclination;
    private EditText editMagneticDeclinationDirection;
    private TextView txtGpsProtocol;

    private void initControls() {
        txtCarHorn = (TextView) findViewById(R.id.txtCarHorn);
        txtCarDirection = (TextView) findViewById(R.id.txtCarDirection);
        txtLeftTurn=(TextView) findViewById(R.id.txtLeftTurn);
        txtRightTurn=(TextView) findViewById(R.id.txtRightTurn);
        txtHighBeam=(TextView) findViewById(R.id.txtHighBeam);
        txtLowBeam=(TextView) findViewById(R.id.txtLowBeam);
        txtWidthLamp=(TextView) findViewById(R.id.txtWidthLamp);
        txtWarningLamp=(TextView) findViewById(R.id.txtWarningLamp);
        txtFogLamp=(TextView) findViewById(R.id.txtFogLamp);
        txtFootBrake=(TextView) findViewById(R.id.txtFootBrake);
        txtHandBrake=(TextView) findViewById(R.id.txtHandBrake);
        txtBrakePressure=(TextView) findViewById(R.id.txtBrakePressure);
        txtLifeBelt=(TextView) findViewById(R.id.txtLifeBelt);
        txtSbOnSeat=(TextView) findViewById(R.id.txtSbOnSeat);
        txtCarDoor=(TextView) findViewById(R.id.txtCarDoor);
        txtIgnitionSwitch=(TextView) findViewById(R.id.txtIgnitionSwitch);
        txtCarHeadState=(TextView) findViewById(R.id.txtCarHeadState);
        txtCarTailState=(TextView) findViewById(R.id.txtCarTailState);
        txtEngineState=(TextView) findViewById(R.id.txtEngineState);
        editEngineSpeed = (EditText) findViewById(R.id.editEngineSpeed);
        txtCarClutch=(TextView) findViewById(R.id.txtCarClutch);
        editCarGear = (EditText) findViewById(R.id.editCarGear);
        txtCarAccelerator=(TextView) findViewById(R.id.txtCarAccelerator);
        txtViceBrake=(TextView) findViewById(R.id.txtViceBrake);
        txtSeatAdjust=(TextView) findViewById(R.id.txtSeatAdjust);

        editSatelliteAmount = (EditText) findViewById(R.id.editSatelliteAmount);
        editGpsCoordinate = (EditText) findViewById(R.id.txtGpsCoordinate);
        editRealTime = (EditText) findViewById(R.id.txtRealTime);
        editImmediatelySpeed = (EditText) findViewById(R.id.txtImmediatelySpeed);
        editPositioningQuality = (EditText) findViewById(R.id.txtPositioningQuality);
        editAzimuth= (EditText) findViewById(R.id.txtAzimuth);
        editMagneticDeclination= (EditText) findViewById(R.id.txtMagneticDeclination);
        editMagneticDeclinationDirection= (EditText) findViewById(R.id.txtMagneticDeclinationDirection);
        txtGpsProtocol = (TextView) findViewById(R.id.txtGpsProtocol);
    }


    private void initSerial(){
        SharedPreferences sp = getSharedPreferences("com.michael.km3serialdemo_preferences", MODE_PRIVATE);
        String carPath = sp.getString("DEVICE_CAR", "");
        int carBaudrate = Integer.decode(sp.getString("BAUDRATE_CAR", "-1"));
        String gpsPath = sp.getString("DEVICE_GPS", "");
        String gpsProtocl=sp.getString("GPS_PROTOCOL","");
        int gpsBaudrate = Integer.decode(sp.getString("BAUDRATE_GPS", "-1"));

        if ( (carPath.length() == 0) || (carBaudrate == -1)||(gpsPath.length() == 0) || (gpsBaudrate == -1)||(gpsProtocl.length()==0)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("错误");
            builder.setMessage(R.string.error_configuration);
            builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CarAndGpsActivity.this.finish();
                }
            });
            builder.show();
        }

        carProperties = new CarProperties();
        try {
            carSerialPort = new CarSerialPort(carPath, carBaudrate, carProperties);
        } catch (IOException e) {
            toastMsg(e.toString());
            e.printStackTrace();
            CarAndGpsActivity.this.finish();
        }
        try {
            carSerialPort.openSerialPort();
            toastMsg("车载串口 " + carPath + " 已打开");
        }catch (SecurityException e) {
            dialogErrorMsg(R.string.error_security);
        } catch (IOException e) {
            dialogErrorMsg(R.string.error_unknown);
        } catch (InvalidParameterException e) {
            dialogErrorMsg(R.string.error_configuration);
        }

        gpsProperties = new GpsProperties(new GpsCoordinate(31.86, 117.21,26.11));

        try {
            gpsSerialPort = new GpsSerialPort(gpsPath, gpsBaudrate, gpsProperties);
        } catch (IOException e) {
            toastMsg("new GpsSerialPort()时出错");
            e.printStackTrace();
        }

        if (gpsProtocl != null) {
            switch (gpsProtocl){
                default:
                case "$GPGGA":
                    txtGpsProtocol.setText("当前GPS解析协议：GPGGA");
                    gpsSerialPort.getParser().setGpsProtocolType(EGpsProtocolType.GPS_PROTOCOL_GPGGA);
                    break;
                case "$GPRMC":
                    txtGpsProtocol.setText("当前GPS解析协议：GPRMC");
                    gpsSerialPort.getParser().setGpsProtocolType(EGpsProtocolType.GPS_PROTOCOL_GPRMC);
                    break;
                case "$All":
                    txtGpsProtocol.setText("当前GPS解析协议：GPGGA和GPRMC");
                    gpsSerialPort.getParser().setGpsProtocolType(EGpsProtocolType.GPS_PROTOCOL_ALL);
                    String timeStyle = sp.getString("GPS_TIME_STYLE", "");
                    String calSpeedStyle = sp.getString("GPS_CAL_SPEED_STYLE", "");
                    if(timeStyle!=null&&!timeStyle.isEmpty()){
                        if(timeStyle.equals("$GPGGA")){
                            gpsSerialPort.getParser().setTimeStyle(EGpsProtocolType.GPS_PROTOCOL_GPGGA);
                        }else if(timeStyle.equals("$GPRMC")){
                            gpsSerialPort.getParser().setTimeStyle(EGpsProtocolType.GPS_PROTOCOL_GPRMC);
                        }
                    }
                    if(calSpeedStyle!=null&&!calSpeedStyle.isEmpty()){
                        if(calSpeedStyle.equals("$GPGGA")){
                            gpsSerialPort.getParser().setCalSpeedStyle(EGpsProtocolType.GPS_PROTOCOL_GPGGA);
                        }else if(calSpeedStyle.equals("$GPRMC")){
                            gpsSerialPort.getParser().setCalSpeedStyle(EGpsProtocolType.GPS_PROTOCOL_GPRMC);
                        }
                    }
                    break;
            }
        }

        try {
            gpsSerialPort.openSerialPort();
            toastMsg("GPS串口 " + gpsPath + " 已打开");
        }catch (SecurityException e) {
            dialogErrorMsg(R.string.error_security);
        } catch (IOException e) {
            dialogErrorMsg(R.string.error_unknown);
        } catch (InvalidParameterException e) {
            dialogErrorMsg(R.string.error_configuration);
        }
    }

    private void listenCarProperties(){
        carProperties.addListener(new IAttrChangedListener() {
            @Override
            public void onAttrChanged(final AttrChangedEvent event) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       int source= (int) event.getSource();
                       //int oldVal= (int) event.getOldValue();
                       int newVal= (int) event.getNewValue();
                       switch (source){
                           case CarProperties.CAR_HORN:
                               setViewBackgroundColor(txtCarHorn,newVal);
                               break;
                           case CarProperties.BRAKE_PRESSURE:
                               setViewBackgroundColor(txtBrakePressure,newVal);
                               break;
                           case CarProperties.CAR_CLUTCH:
                               setViewBackgroundColor(txtCarClutch,newVal);
                               break;
                           case CarProperties.CAR_ACCELERATOR:
                               setViewBackgroundColor(txtCarAccelerator,newVal);
                               break;
                           case CarProperties.CAR_DIRECTION:
                               if(newVal==1){
                                   txtCarDirection.setBackgroundColor(Color.GREEN);
                               }else if(newVal==-1){
                                   txtCarDirection.setBackgroundColor(Color.RED);
                               }else {
                                   txtCarDirection.setBackgroundColor(Color.WHITE);
                               }
                               break;
                           case CarProperties.CAR_DOOR:
                               setViewBackgroundColor(txtCarDoor,newVal);
                               break;
                           case CarProperties.CAR_GEAR:
                               editCarGear.setText(String.valueOf(newVal));
                               break;
                           case CarProperties.CAR_HEAD_STATE:
                               if(newVal==0){
                                   txtCarHeadState.setBackgroundColor(Color.RED);
                               }else {
                                   txtCarHeadState.setBackgroundColor(Color.WHITE);
                               }
                               break;
                           case CarProperties.CAR_TAIL_STATE:
                               if(newVal==0){
                                   txtCarTailState.setBackgroundColor(Color.RED);
                               }else {
                                   txtCarTailState.setBackgroundColor(Color.WHITE);
                               }
                               break;
                           case CarProperties.CAR_VELOCITY:
                               break;
                           case CarProperties.ENGINE_SPEED:
                               editEngineSpeed.setText(String.valueOf(newVal));
                               break;
                           case CarProperties.ENGINE_STATE:
                               setViewBackgroundColor(txtEngineState,newVal);
                               break;
                           case CarProperties.FOG_LAMP:
                               setViewBackgroundColor(txtFogLamp,newVal);
                               break;
                           case CarProperties.FOOT_BRAKE:
                               setViewBackgroundColor(txtFootBrake, newVal);
                               break;
                           case CarProperties.HAND_BRAKE:
                               if(newVal==0){
                                   txtHandBrake.setBackgroundColor(Color.RED);
                               }else {
                                   txtHandBrake.setBackgroundColor(Color.WHITE);
                               }
                               break;
                           case CarProperties.HIGH_BEAM:
                               setViewBackgroundColor(txtHighBeam,newVal);
                               break;
                           case CarProperties.IGNITION_SWITCH:
                               setViewBackgroundColor(txtIgnitionSwitch,newVal);
                               break;
                           case CarProperties.LEFT_TURN:
                               setViewBackgroundColor(txtLeftTurn,newVal);
                               break;
                           case CarProperties.LIFE_BELT:
                               setViewBackgroundColor(txtLifeBelt,newVal);
                               break;
                           case CarProperties.LOW_BEAM:
                               setViewBackgroundColor(txtLowBeam,newVal);
                               break;
                           case CarProperties.RIGHT_TURN:
                               setViewBackgroundColor(txtRightTurn,newVal);
                               break;
                           case CarProperties.SB_ON_SEAT:
                               setViewBackgroundColor(txtSbOnSeat,newVal);
                               break;
                           case CarProperties.SEAT_ADJUST:
                               setViewBackgroundColor(txtSeatAdjust,newVal);
                               break;
                           case CarProperties.VICE_BRAKE:
                               setViewBackgroundColor(txtViceBrake,newVal);
                               break;
                           case CarProperties.WARNING_LAMP:
                               setViewBackgroundColor(txtWarningLamp,newVal);
                               break;
                           case CarProperties.WIDTH_LAMP:
                               setViewBackgroundColor(txtWidthLamp,newVal);
                               break;
                       }
                   }
               });
            }
        });
    }

    private void setViewBackgroundColor(View view,int newVal){
        if(newVal==1){
            view.setBackgroundColor(Color.RED);
        }else {
            view.setBackgroundColor(Color.WHITE);
        }
    }

    private void listenGpsProperties(){
        gpsProperties.addListener(new IAttrChangedListener() {
            @Override
            public void onAttrChanged(final AttrChangedEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int source= (int) event.getSource();
                        switch (source){
                            case GpsProperties.AZIMUTH:
                                double azimuth= (double) event.getNewValue();
                                editAzimuth.setText(String.valueOf(azimuth));
                                break;
                            case GpsProperties.BASE_GPS_COORDINATE:
                                break;
                            case GpsProperties.GPS_COORDINATE:
                                GpsCoordinate gpsCoordinate= (GpsCoordinate) event.getNewValue();
                                editGpsCoordinate.setText(gpsCoordinate.toString());
                                break;
                            case GpsProperties.IMMEDIATELY_SPEED:
                                double immediatelySpeed= (double) event.getNewValue();
                                editImmediatelySpeed.setText(String.valueOf(immediatelySpeed));
                                break;
                            case GpsProperties.MAGNETIC_DECLINATION:
                                double magnetic= (double) event.getNewValue();
                                editMagneticDeclination.setText(String.valueOf(magnetic));
                                break;
                            case GpsProperties.MAGNETIC_DECLINATION_DIRECTION:
                                String direction= (String) event.getNewValue();
                                editMagneticDeclinationDirection.setText(direction);
                                break;
                            case GpsProperties.POSITIONING_QUALITY:
                                String quality= (String) event.getNewValue();
                                editPositioningQuality.setText(quality);
                                break;
                            case GpsProperties.REAL_TIME:
                                String time= (String) event.getNewValue();
                                editRealTime.setText(time);
                                break;
                            case GpsProperties.SATELLITE_AMOUNT:
                                int amount= (int) event.getNewValue();
                                editSatelliteAmount.setText(String.valueOf(amount));
                                break;
                        }
                    }
                });
            }
        });

    }


    @Override
    protected void onDestroy() {
        if(carSerialPort!=null&&carSerialPort.isOpened()){
            carSerialPort.closeSerialPort();
        }
        if(gpsSerialPort!=null&&gpsSerialPort.isOpened()){
            gpsSerialPort.closeSerialPort();
        }
        super.onDestroy();
    }

    private void toastMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void dialogErrorMsg(int resourceId){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CarAndGpsActivity.this.finish();
            }
        });
        b.show();
    }
    private void dialogInfoMsg(String msg){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Info");
        b.setMessage(msg);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CarAndGpsActivity.this.finish();
            }
        });
        b.show();
    }

}
