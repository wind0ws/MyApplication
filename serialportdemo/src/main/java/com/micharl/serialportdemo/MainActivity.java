package com.micharl.serialportdemo;



import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ieimobile.serial.SerialLib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android_serialport_api.SerialPortFinder;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, SerialLib.OnPortDataListener {
    private static final String TAG = "SerialPortActivity";

    private static final int DEFAULT_BUADRATE = 115200;
    private static final String DEFAULT_DEVICE = "/dev/ttyO1";

    private String mRTSs[] = {"CLR", "SET"};
    private String mDeviceNamePaths[];
    private String mBaudrates[];

    private byte mBaudrateIndex, mDeviceIndex, mSetRTSIndex;

    //	private TextView mTxMode;
    private EditText mReception, mCmdArea, mCmdArea2;
    private Button mBtnClear, mBtnSend, mBtnOpenClose, mBtnSend2;

    private CheckBox mCheckHexMode;
    private Spinner mSpDevice, mSpBaudRate, mSpMode;

    private final SerialLib mSerial = new SerialLib();

    private File fileLog;
    private FileOutputStream fileOutputStream;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"内置存储已挂载",Toast.LENGTH_SHORT).show();
            fileLog = new File( Environment.getExternalStorageDirectory().getPath()+"/serialLog.txt");

            if(!fileLog.exists()){
                Toast.makeText(this,"serialLog.txt文件不存在，现在为你创建",Toast.LENGTH_SHORT).show();
                try {
                    if( fileLog.createNewFile()){
                        Toast.makeText(this,"创建成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this,"创建失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(this,"创建文件时抛出异常:\n"+e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            try {
                fileOutputStream = new FileOutputStream(fileLog);
            } catch (FileNotFoundException e) {
                Toast.makeText(this,"创建FileOutputStream时发生错误，文件没有找到",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this,"内置存储尚未挂载",Toast.LENGTH_SHORT).show();
        }


        SerialPortFinder serialPortFinder = new SerialPortFinder();
        String[] entries = serialPortFinder.getAllDevices();

        mDeviceNamePaths = serialPortFinder.getAllDevicesPath();

//        mDeviceNamePaths = getResources().getStringArray(
//				R.array.device_name);

        mBaudrates = getResources().getStringArray(
                R.array.baudrates_name);

        mSpDevice = (Spinner) findViewById(R.id.deviceName);
        mSpBaudRate = (Spinner) findViewById(R.id.baudrateSelection);
        mSpMode = (Spinner) findViewById(R.id.rtsSelection);

//        mTxMode = (TextView) findViewById(R.id.mode);

        //Edit area
        mCmdArea = (EditText) findViewById(R.id.EditEmission);
        mCmdArea2 = (EditText) findViewById(R.id.EditEmission2);
        mReception = (EditText) findViewById(R.id.EditReception);

        //Buttons
        mBtnOpenClose = (Button) findViewById(R.id.btnOpenClose);
        mBtnOpenClose.setOnClickListener(this);

        mBtnSend = (Button) findViewById(R.id.btnSend);
        mBtnSend.setOnClickListener(this);

        mBtnSend2 = (Button) findViewById(R.id.btnSend2);
        mBtnSend2.setOnClickListener(this);

        mBtnClear = (Button) findViewById(R.id.btnClear);
        mBtnClear.setOnClickListener(this);

        //Check box
        mCheckHexMode = (CheckBox) findViewById(R.id.hexMode);

        //TODO: HEX mode
        mCheckHexMode.setChecked(false);

        initDeviceList();
        initBaudrateList();
        initRTSCtrl();

        mCmdArea.setText("0108000304FF0000");
        mCmdArea2.setText("010C00030410002101000000");

    }

    void initRTSCtrl()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mRTSs);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpMode.setAdapter(adapter);
        mSpMode.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {

                        mSetRTSIndex = (byte) (id);//id index based from 0

                        Log.d(TAG, "RTS Enable= "+ mSetRTSIndex);

                        //TODO Call Set RTS native API
                        mSerial.setRTS(mSetRTSIndex == 0 ? false : true);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


    }

    void initDeviceList()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mDeviceNamePaths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpDevice.setAdapter(adapter);
        mSpDevice.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {

                        mDeviceIndex = (byte) (id);//id index based from 0

                        Log.d(TAG, "Device ="+ mDeviceNamePaths[mDeviceIndex]);

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        mDeviceIndex = 0; //init

        //Look for DEFAULT_DEVICE
        for(int i=0; i < mDeviceNamePaths.length; i++) {
            if(mDeviceNamePaths[i].endsWith(DEFAULT_DEVICE)) {
                mDeviceIndex = (byte) i;
                break;
            }
        }
        mSpDevice.setSelection(mDeviceIndex);
    }

    void initBaudrateList()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mBaudrates);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpBaudRate.setAdapter(adapter);
        mSpBaudRate.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {

                        mBaudrateIndex = (byte) (id);//id index based from 0

                        Log.d(TAG, "Baudrate ="+ mBaudrates[mBaudrateIndex]);

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        //Look for baudrate
        for(int i=0; i < mBaudrates.length; i++) {
            if(Integer.valueOf(mBaudrates[i]) == DEFAULT_BUADRATE) {
                mBaudrateIndex = (byte) i;
                break;
            }
        }
        mSpBaudRate.setSelection(mBaudrateIndex);
    }
    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(),
     * for your activity to start interacting with the user.  This is a good
     * place to begin animations, open exclusive-access devices (such as the
     * camera), etc.
     *
     * Derived classes must call through to the super class's implementation
     * of this method.  If they do not, an exception will be thrown.
     */
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");

        super.onResume();

        if(mSerial!=null)
            mSerial.setPortDataListener(this);

    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();

        if(mSerial != null) {

            if(mSerial.isOpened()) {
                try {
                    mSerial.closeDevice();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.d(TAG, e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            Toast.makeText(this,"关闭FileOutputStream出错",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        //MUST CALL !!!
        super.onDestroy();
    }

    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {@link #onStart}, {@link #onDestroy}, or nothing,
     * depending on later user activity.
     */
    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();

    }

    public void onClick(View v) {
        // TODO Auto-generated method stub

        try {
            switch(v.getId()) {
                case R.id.btnClear:
                    mReception.setText("");//Clear
                    break;

                case R.id.btnOpenClose:

                    if(mSerial.isOpened()) {
                        mSerial.closeDevice();
                        mBtnOpenClose.setText(R.string.open);

                        mSpDevice.setEnabled(true);
                        mSpBaudRate.setEnabled(true);
                    }
                    else {
                        // 2) open port
                        boolean isOpened=false;
                        try {
                            isOpened= mSerial.openDevice(mDeviceNamePaths[mDeviceIndex],
                                    Integer.valueOf(mBaudrates[mBaudrateIndex]));
                        }catch (Exception err){
                            Toast.makeText(this,err.toString(),Toast.LENGTH_LONG).show();
                        }

                        //make sure
                        if(isOpened&&mSerial.isOpened()) {
                            mBtnOpenClose.setText(R.string.close);

                            mSpDevice.setEnabled(false);
                            mSpBaudRate.setEnabled(false);
                        }
                        else {
                            toastMsg(String.format("Couldn't open %s", mDeviceNamePaths[mDeviceIndex]));
                        }
                    }
                    break;

                case R.id.btnSend:
                case R.id.btnSend2:

                    if(mSerial.isOpened()) {
                        String cmd;

                        if(v.getId() == R.id.btnSend)
                            cmd = mCmdArea.getText().toString();
                        else
                            cmd = mCmdArea2.getText().toString();

                        if(cmd.length() != 0) {
                            byte cmds[];

                            if(!mCheckHexMode.isChecked()) {
                                cmds = cmd.getBytes();
                                mSerial.write(cmds, cmds.length);
                                mSerial.write((byte)0x0D); //return char
                            }
                            else {
                                cmd = cmd.trim();
                                String cmdArray[] = cmd.split(" ");

                                cmd="";
                                for(int i=0; i < cmdArray.length; i++) {
                                    cmd += cmdArray[i].toUpperCase();
                                }

                                cmds = stringToHex(cmd);

                                //TODO: Remove for loop
                                //for(int i=0; i < 1024; i++)//TEST
                                mSerial.write(cmds, cmds.length);
                            }

                            mReception.append("SEND > ");

                            if(!mCheckHexMode.isChecked()) {
                                mReception.append(cmd);
                                mReception.append("<CR>");
                            }
                            else {
                                mReception.append(hexToString(cmds, cmds.length, 0));
                            }

                            mReception.append("\n");
                        }
                    }
                    else {
                        toastMsg(R.string.portnotopened);
                    }

                    break;
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "onClick() exception!");
        }
    }

    private boolean openDevice(String device, int baudrate)
    {
        boolean ok = false;

        try {
            ok = mSerial.openDevice(device, baudrate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }

        if(!ok) {
            String msg = "Couldn't open device:" +
                    mSerial.getDeviceName() + "\n";
            Log.d(TAG, msg);

            //toastMsg(msg);
        }
        return ok;
    }

    private String hexToString(byte[] buf, int size, int start) {
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

    private byte[] stringToHex(String str) {
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

    HandlerThread handlerThread = new HandlerThread("WriterThread");
    Handler handler=new Handler(handlerThread.getLooper());

    public void onReceived(final byte[] buffer, final int size) {
        // TODO Auto-generated method stub
   /*     String receive=hexToString(buffer, size, 0);
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    fileOutputStream.write(buffer,0,size);
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this,"写入文件出错",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        mReception.append("Receiving\n");*/
        runOnUiThread(new Runnable() {
            public void run() {
                if (mReception != null) {
                    //mReception.append("RECV > ");

                    if(!mCheckHexMode.isChecked()) {
                        mReception.append(new String(buffer, 0, size));
                    }
                    else {
                        String receive=hexToString(buffer, size, 0);
                        mReception.append(receive);
                    }
                    mReception.append("\n");
                }
            }
        });
    }

    protected void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void toastMsg(int resId) {
        String msg = getResources().getString(resId);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_testActivity:
                startActivity(new Intent(this,TestActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
