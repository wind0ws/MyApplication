package com.example.mylibrary;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class LibraryActivity extends ActionBarActivity {

    private TextView deviceId;
    private TextView phoneModule;
    private TextView serialNumber;
    private TextView phoneNumber;
    private TextView macAddress;
    private TextView cpuInfo;
    private TextView totalMemory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        Toast.makeText(this,"Welcome  库中的MainActivity现已启动",Toast.LENGTH_SHORT).show();
        showDeviceInfo();
    }

    private void showDeviceInfo() {
        deviceId = (TextView) this.findViewById(R.id.txtDeviceId);
        phoneModule = (TextView) this.findViewById(R.id.txtPhoneModule);
        serialNumber = (TextView) this.findViewById(R.id.txtSerialNumber);
        phoneNumber = (TextView) this.findViewById(R.id.txtPhoneNumber);
        macAddress = (TextView) this.findViewById(R.id.txtMacAddress);
        cpuInfo = (TextView) this.findViewById(R.id.txtCpuInfo);
        totalMemory = (TextView) this.findViewById(R.id.txtTotalMemory);

        PhoneInfo phoneInfo = new PhoneInfo(this);
        deviceId.setText("设备ID: "+phoneInfo.getDeviceId());
        phoneModule.setText("设备型号："+phoneInfo.getPhoneModule());
        serialNumber.setText("设备序列号："+phoneInfo.getSerialNumber());
        phoneNumber.setText("设备手机号："+phoneInfo.getPhoneNumber());
        macAddress.setText("WIFI MAC地址: "+phoneInfo.getWiFiMacAddress());
        String[] cpu=phoneInfo.getCpuInfo();
        String cpuString="";
        for(String s:cpu){
            cpuString=cpuString+"  "+s;
        }
        cpuInfo.setText("cpu信息:"+cpuString);
        totalMemory.setText("系统内存:"+phoneInfo.getTotalMemory());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
