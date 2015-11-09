package com.michael.jiang.handlerstudy;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

  private   static class MainHandler extends Handler{
        WeakReference<MainActivity> weakReference;
        public MainHandler(MainActivity activity){
            weakReference=new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity=weakReference.get();
            if(mainActivity==null) {
                Log.d(TAG, "the mainActivity has been destroyed");
                return;
            }
            switch (msg.what){
                case 1:

                    break;
                case 2:
                    mainActivity.textView.setText(msg.obj.toString());
                    break;
            }
            Log.i(TAG,"ThreadId:"+Thread.currentThread());
            mainActivity.threadHandler.sendEmptyMessageDelayed(1, 1000);
            super.handleMessage(msg);
        }
    }

    private static class ThreadHandler extends Handler{
        private WeakReference<MainActivity> weakReference;

        public ThreadHandler(Looper looper,MainActivity mainActivity) {
            super(looper);
            this.weakReference=new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = weakReference.get();
            if(mainActivity==null)
            {
                Log.d("Threshold","the mainActivity has been destoryed");
                return;
            }
            Log.i(TAG,"ThreadId:"+Thread.currentThread());
            mainActivity.mainHandler.sendEmptyMessageDelayed(1, 1000);
            super.handleMessage(msg);
        }
    }

   /* private  Handler mainHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==2){
                textView.setText(msg.obj.toString());}
            Log.i(TAG,"ThreadId"+Thread.currentThread());
            threadHandler.sendEmptyMessageDelayed(1, 1000);
            super.handleMessage(msg);
        }
    };*/

    private  Handler mainHandler=new MainHandler(this);
    private Handler threadHandler;
    private TextView textView;
    public static final String TAG="Thresh0ld";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView= (TextView) this.findViewById(R.id.txtView);
        Button btnSend = (Button) this.findViewById(R.id.btnSend);
        Button btnCancel = (Button) this.findViewById(R.id.btnCancel);
        btnSend.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        HandlerThread handlerThread = new HandlerThread("handler thread");
        handlerThread.start();

        threadHandler = new ThreadHandler(handlerThread.getLooper(), this);

       /* threadHandler=new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG,"ThreadId"+Thread.currentThread());
                mainHandler.sendEmptyMessageDelayed(1, 1000);
                super.handleMessage(msg);
            }
        };*/
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
        Message message=mainHandler.obtainMessage();
        message.what=2;
        message.obj="你好";
        switch (v.getId()){
            case R.id.btnSend:
                mainHandler.sendMessage(message);
                break;
            case R.id.btnCancel:
                mainHandler.removeMessages(message.what);
                mainHandler.removeMessages(1);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        mainHandler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }
}

