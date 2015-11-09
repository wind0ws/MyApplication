package com.michael.km3serialdemo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android_serialport_api.SerialPortFinder;

public class SerialPortPreferences extends PreferenceActivity {

    private Application mApplication;
    private SerialPortFinder mSerialPortFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplication = (Application) getApplication();
        mSerialPortFinder = mApplication.mSerialPortFinder;

        addPreferencesFromResource(R.xml.serial_port_preferences);

        //Display Device
        final ListPreference display_device = (ListPreference) findPreference("DISPLAY_DEVICE");
        display_device.setSummary(display_device.getValue());
        display_device.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String)newValue);
                return true;
            }
        });

        // Devices CarSerialPort
        final ListPreference devices = (ListPreference)findPreference("DEVICE_CAR");
        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
        devices.setEntries(entries);
        devices.setEntryValues(entryValues);
        devices.setSummary(devices.getValue());
        devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String)newValue);
                return true;
            }
        });

        // Baud rates CarSerialPort
        final ListPreference baudrates = (ListPreference)findPreference("BAUDRATE_CAR");
        baudrates.setSummary(baudrates.getValue());
        baudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String)newValue);
                return true;
            }
        });

        //ShowModeCar eg:hex or string
        final ListPreference showModeCar = (ListPreference) findPreference("SHOW_MODE_CAR");
        showModeCar.setSummary(showModeCar.getValue());
        showModeCar.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String)newValue);
                return true;
            }
        });

        // Devices GpsSerialPort
        final ListPreference devices_gps = (ListPreference)findPreference("DEVICE_GPS");
//        String[] entries = mSerialPortFinder.getAllDevices();
//        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
        devices_gps.setEntries(entries);
        devices_gps.setEntryValues(entryValues);
        devices_gps.setSummary(devices_gps.getValue());
        devices_gps.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String)newValue);
                return true;
            }
        });

        // Baud rates GpsSerialPort
        final ListPreference baudrates_gps = (ListPreference)findPreference("BAUDRATE_GPS");
        baudrates_gps.setSummary(baudrates_gps.getValue());
        baudrates_gps.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String)newValue);
                return true;
            }
        });

        //ShowModeGps eg:hex or string
        final ListPreference showModeGps = (ListPreference) findPreference("SHOW_MODE_GPS");
        showModeGps.setSummary(showModeGps.getValue());
        showModeGps.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                return true;
            }
        });

        //GPS 解析协议
        final ListPreference gpsProtocol = (ListPreference) findPreference("GPS_PROTOCOL");
        String[] protocolEntries=new String[]{"GPGGA","GPRMC","All"};
        String[] protocolEntryValues=new String[]{"$GPGGA","$GPRMC","$All"};
        gpsProtocol.setEntries(protocolEntries);
        gpsProtocol.setEntryValues(protocolEntryValues);
        gpsProtocol.setSummary(gpsProtocol.getValue());
        gpsProtocol.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String)newValue);
                return true;
            }
        });

        final ListPreference gpsTimeStyle = (ListPreference) findPreference("GPS_TIME_STYLE");
        gpsTimeStyle.setEntries(protocolEntries);
        gpsTimeStyle.setEntryValues(protocolEntryValues);
        gpsTimeStyle.setSummary(gpsTimeStyle.getValue());
        gpsTimeStyle.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(String.valueOf(newValue).equals("$All")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(SerialPortPreferences.this);
                    builder.setTitle("错误");
                    builder.setMessage("GPS显示时间方式协议不能选择All");
                    return false;
                }
                preference.setSummary((String)newValue);
                return true;
            }
        });


        final ListPreference gpsCalSpeedStyle = (ListPreference) findPreference("GPS_CAL_SPEED_STYLE");
        gpsCalSpeedStyle.setEntries(protocolEntries);
        gpsCalSpeedStyle.setEntryValues(protocolEntryValues);
        gpsCalSpeedStyle.setSummary(gpsCalSpeedStyle.getValue());
        gpsCalSpeedStyle.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(String.valueOf(newValue).equals("$All")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(SerialPortPreferences.this);
                    builder.setTitle("错误");
                    builder.setMessage("GPS速度计算方式协议不能选择All");
                    return false;
                }
                preference.setSummary((String)newValue);
                return true;
            }
        });


    }
}
