package com.michael.jiang.filedemo;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Button btnReadFile;
    Button btnWriteFile;
    EditText editText;
    File file;
    FileOutputStream fileOutputStream;
    FileInputStream fileInputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnReadFile = (Button) this.findViewById(R.id.btnReadFile);
        btnWriteFile = (Button) this.findViewById(R.id.btnWriteFile);
        editText = (EditText) this.findViewById(R.id.edit_content);
        btnReadFile.setOnClickListener(this);
        btnWriteFile.setOnClickListener(this);
        file = new File(Environment.getExternalStorageDirectory().getPath() + "/text.txt");
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            toastMsg("内置存储已挂载");
            if(!file.exists()){
                try {
                   if( file.createNewFile()){
                       toastMsg("创建文件成功");
                   }
                } catch (IOException e) {
                    toastMsg("创建失败");
                    e.printStackTrace();
                }
            }
        }else {
            toastMsg("内置存储还没有挂载");
        }
        editText.append("\n内置存储路径："+Environment.getExternalStorageDirectory().getPath());
        editText.append("\n数据路径："+Environment.getDataDirectory());
        editText.append("\n内置存储状态"+Environment.getExternalStorageState());
        editText.append("\nDownloadService路径："+Environment.getExternalStoragePublicDirectory(DOWNLOAD_SERVICE));
        if(file.exists()){
            try {
                fileOutputStream = new FileOutputStream(file);

                byte[] bytes=editText.getText().toString().getBytes();

                fileOutputStream.write(bytes,0,bytes.length);
            } catch (FileNotFoundException e) {
                toastMsg("创建fileOutputStream失败");
                e.printStackTrace();
            } catch (IOException e) {
                toastMsg("写入文件出错");
                e.printStackTrace();
            }
        }
    }

    private void toastMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnWriteFile:


                break;
            case R.id.btnReadFile:
                try {
                    fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    String readResult =new String(buffer);// EncodingUtils.getString(buffer, "UTF-8")
                    editText.setText(readResult);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    @Override
    protected void onDestroy() {
        if(fileOutputStream!=null){
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                toastMsg("关闭FileOutputStream出错");
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
