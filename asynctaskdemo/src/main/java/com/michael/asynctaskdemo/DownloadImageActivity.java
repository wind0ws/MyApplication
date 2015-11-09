package com.michael.asynctaskdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class DownloadImageActivity extends ActionBarActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private ImageView imageView;
    private MyAsyncTask myAsyncTask;

    private static final String imageUrl = "http://img.mukewang.com/554abe150001e04f06000338-280-160.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image);
        initControls();
        myAsyncTask=new MyAsyncTask();
        myAsyncTask.execute(imageUrl);
    }

    private void initControls() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download_image, menu);
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

    }

    @Override
    protected void onDestroy() {
        if(myAsyncTask!=null&&myAsyncTask.getStatus()== AsyncTask.Status.RUNNING) {
            myAsyncTask.cancel(true);
        }
        super.onDestroy();
    }

    private class MyAsyncTask extends AsyncTask<String,Integer,Bitmap> {

        public static final String TAG = "Thresh0ld";
        @Override
        protected Bitmap doInBackground(String... params) {
            for(int i=0;i<100;i++){
                if(isCancelled()) {
                    return null;
                }
                publishProgress(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Bitmap bitmap=null;
            URLConnection urlConnection=null;
            String imagePath=null;
            InputStream inputStream;
            BufferedInputStream bufferedInputStream;
            if(params.length>0) {
                imagePath = params[0];
            }
            try {
                urlConnection = new URL(imagePath).openConnection();
                inputStream = urlConnection.getInputStream();
                bufferedInputStream = new BufferedInputStream(inputStream);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute()");
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute()");
            super.onPostExecute(bitmap);
            if(bitmap!=null) {
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d(TAG, "onProgressUpdate()");
            if(isCancelled()){
                progressBar.setProgress(0);
                return;
            }
            progressBar.setProgress(values[0]);
            super.onProgressUpdate(values);
        }
    }


}
