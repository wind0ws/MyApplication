package com.michael.km3serialdemo;

import java.io.IOException;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import android_km3serialport_api.SerialPortBase;

public class ConsoleActivity extends SerialPortActivity {

    EditText mReception;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_console);

//		setTitle("Loopback test");
        mReception = (EditText) findViewById(R.id.EditTextReception);

        EditText Emission = (EditText) findViewById(R.id.EditTextEmission);
        Emission.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int i;
                CharSequence t = v.getText();
                char[] text = new char[t.length()];
                for (i=0; i<t.length(); i++) {
                    text[i] = t.charAt(i);
                }
                try {
                    mOutputStream.write(new String(text).getBytes());
                    mOutputStream.write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (mReception != null) {
                    switch (mShowMode){
                        case "hex":
                            mReception.append(hexToString(buffer, size, 0));
                            break;
                        case "string":
                            mReception.append(new String(buffer, 0, size));
                            //mReception.append(getBufferString(buffer,0,size));
                           // mReception.append(buffer2String(buffer,0,size));
                            break;
                    }
                    //mReception.append(new String(buffer, 0, size));
                   // mReception.append(hexToString(buffer, size, 0));

                   /* int[] ints = SerialPortBase.bytesToInts(buffer, size, 0);
                    for(int i:ints){
                        mReception.append(String.valueOf(i)+" ");
                    }*/
                    mReception.append("\n");
                }
            }
        });
    }

   /* private String getBufferString(byte[] buf, int start, int size) {
        int[] ints = SerialPortBase.bytesToInts(buf, size, start);
        StringBuilder stringBuilder=new StringBuilder();
        for(int i:ints){
            stringBuilder.append(new String(int2bytes(i)));
        }
       *//*for(int i=0;i<ints.length;i++){
           byte tmp=(byte)(ints[i]-128);
           bytes[i]=tmp;
      }*//*
        return stringBuilder.toString();
    }

    public static byte[] int2bytes(int n) {
        byte[] ab = new byte[4];
        ab[0]=(byte)(0xff&n);
        ab[1]=(byte)((0xff00&n)>>8);
        ab[2] = (byte) ((0xff0000) & n >> 16);
        ab[3]=(byte)((0xff000000&n)>>24);
        return ab;
    }

    private String buffer2String(byte[] buf, int start, int size) {
        String hexString = hexToString(buf, size, start).trim().replace(" ", "");
        int temp=0;
        String str="";
        int[] ints = new int[hexString.length() / 2];
        for(int i=0;i<hexString.length()/2;i=i+2) {
            str = hexString.substring(i, i + 2);
            temp = Integer.valueOf(str, 16);
            ints[i / 2] = temp;
        }
        StringBuilder stringBuilder=new StringBuilder();
        for(int i:ints) {
            stringBuilder.append((char) i);
        }
        return stringBuilder.toString();
    }
*/
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


}
