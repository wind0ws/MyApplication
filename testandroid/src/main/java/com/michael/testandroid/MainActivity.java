package com.michael.testandroid;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends Activity {

    private Button button;
    private EditText editText;

//    boolean isInitSpeech;
//    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
//
//        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                Toast.makeText(MainActivity.this,"status:"+ status,Toast.LENGTH_SHORT).show();
//                isInitSpeech = status == TextToSpeech.SUCCESS;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        button.setEnabled(isInitSpeech);
//                    }
//                });
//            }
//        });

        final AssetManager assetManager = this.getAssets();
      /*  try {
            String[] list = assetManager.list("");
            int length = list.length;
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                AssetsUtils.copyAssetsFiles(assetManager, "wltlib/base.dat", Environment.getExternalStorageDirectory() + "/Km3/base",true);
                                AssetsUtils.copyAssetsFiles(assetManager, "wltlib/license.lic", Environment.getExternalStorageDirectory() + "/Km3/wltlib/", true);
                                AssetsUtils.copyAssetsFiles(assetManager, "wltlib/threshold/SysOrder.txt", Environment.getExternalStorageDirectory() + "/Km3/threshold/order.txt", true);
//                                AssetsUtils.copyAssetsFiles(assetManager,"threshold/Chris Medina - What Are Words.mp3",Environment.getExternalStorageDirectory()+"/Km3/",true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                thread.start();


                //assetManager.close();
                //Toast.makeText(MainActivity.this,"完成",Toast.LENGTH_SHORT).show();
                //editText.append();
                // textToSpeech.speak(editText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

       //Person p = new Person("张三", 23);
       /* try {
            FileOutputStream outputStream = openFileOutput("person.out", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(p);

        } catch (IOException e) {
            Toast.makeText(this,"写入失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/

      /*  try {
            FileInputStream fileInputStream = openFileInput("person.out");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object readObject = objectInputStream.readObject();
            if(readObject!=null){
                Person p= (Person) readObject;
                textView.setText(p.toString());
            }
        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(this,"读取失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/

     //  textView.setText(this.getFilesDir().toString());

        //SerializeHelper serializeHelper = new SerializeHelper();
       /* File file=new File(this.getFilesDir()+"//person.serialize");
        if(file.exists()) {
            Person person= null;
            try {
                person = (Person) serializeHelper.deserializeFromAppFile("person.serialize");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(person!=null) {
                textView.append(person.toString());
            }
        }*/

        //File file = getFilesDir();


      /*  File files = new File(Environment.getExternalStorageDirectory() + "//Test//哈哈哈//xxx.jpg");
        if(!files.exists()) {
            try {
                File dir=files.getParentFile();
                if(!dir.exists()) {
                    dir.mkdirs();
                }
                files.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(files.isDirectory()){
            Toast.makeText(this,"是目录",Toast.LENGTH_SHORT).show();
        }
        if(files.isFile()){
            Toast.makeText(this,"是文件",Toast.LENGTH_SHORT).show();
        }*/

      /*  if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            try {
                File personFile=new File(Environment.getExternalStorageDirectory()+"//TestAndroid//Serialize//person.serialize");
                if(personFile.exists()) {
                  Person onePerson=(Person)  SerializeHelper.deserializeFromFile(personFile.getPath());
                    if(onePerson!=null){
                        textView.append("\n"+onePerson.toString());
                    }
                }

                //serializeHelper.serializeObjectToAppFile(p,"person.serialize");
               *//* File dir=new File(Environment.getExternalStorageDirectory()+"//TestAndroid//Serialize");
                if(!dir.exists()){
                   if(! dir.mkdirs()){
                       Toast.makeText(this,"创建文件夹失败",Toast.LENGTH_SHORT).show();
                       return;
                   }
                }
                serializeHelper.serializeObjectToFile(p,dir.getPath()+"//person.serialize");*//*
            } catch (Exception e) {
                Toast.makeText(this,"序列化出错",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this,"外部存储不可读写",Toast.LENGTH_SHORT).show();
        }

        deleteDir(new File(Environment.getExternalStorageDirectory()+"//crash"),true);
*/


    }




    private void deleteDir(File file,boolean deleteSelf){
        if(!file.exists()){
            return;
        }
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            for(File f:files) {
                if (f.isFile()) {
                    f.delete();
                } else if (f.isDirectory()) {
                    File[] childFiles = f.listFiles();
                    if(childFiles==null||childFiles.length==0) {
                        f.delete();
                    }else {
                        deleteDir(f,true);
                    }
                }
            }
        }
        if(deleteSelf) {
            file.delete();
        }
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
    protected void onDestroy() {
//        if(textToSpeech!=null){
//            textToSpeech.shutdown();
//        }
        super.onDestroy();
    }
}
