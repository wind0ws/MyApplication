package com.michael.jiang.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mylibrary.LibraryActivity;
import com.example.mylibrary.LibraryClass;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Button btnStartService;
    private Button btnStopService;
    private Button btnBindService;
    private Button btnUnbindService;
    private Button btnNotification;
    private Context mContext;

    /**
     * ServiceConnection在绑定服务时需要提供。startService不需要。
     */
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(MyService.TAG,"ServiceConnected触发");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(MyService.TAG,"ServiceDisconncted触发");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
       btnStartService= (Button) this.findViewById(R.id.startservice);
        btnStopService = (Button) this.findViewById(R.id.stopservice);
        btnBindService= (Button) this.findViewById(R.id.bindservice);
        btnUnbindService= (Button) this.findViewById(R.id.unbindservice);
        btnNotification= (Button) this.findViewById(R.id.notification);
        btnStartService.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
        btnBindService.setOnClickListener(this);
        btnUnbindService.setOnClickListener(this);
        btnNotification.setOnClickListener(this);
        Button btnTestLibrary = (Button) this.findViewById(R.id.btnTestLibrary);
        btnTestLibrary.setOnClickListener(this);

        mContext=MainActivity.this;
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
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,MyService.class);
        if(v==btnStartService)
        {
            mContext.startService(intent);
        }else if(v==btnStopService)
        {
            mContext.stopService(intent);
        }
        else if(v==btnBindService)
        {
            bindService(intent,connection,Context.BIND_AUTO_CREATE);
        }
        else if(v==btnUnbindService)
        {
            unbindService(connection);
        } else if(v==btnNotification)
        {
            NotificationManager mNotificationManger= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int icon =R.drawable.ic_launcher;
            long when=System.currentTimeMillis();
            Notification notification=new Notification(icon,"title,suddenly show on statusbar",when);
            PendingIntent contentIntent= PendingIntent.getActivity(this,0,intent,0);
            notification.setLatestEventInfo(this,"标题","内容",contentIntent);
            mNotificationManger.notify(0,notification);
        }
        if(v.getId()==R.id.btnTestLibrary) {
            LibraryClass libraryClass = new LibraryClass(mContext, "哈哈哈哈哈哈，我是来自其他库里的.即将显示库里的Activity");
           libraryClass.showMsg();
            Intent intent1 = new Intent(this, LibraryActivity.class);
            startActivity(intent1);
            //this.finish();
//            Intent mIntent = new Intent( );
//            String packageName="com.example.mylibrary";
//            String activityName="LibraryActivity";
//            ComponentName comp = new ComponentName(packageName, activityName);
//            mIntent.setComponent(comp);
//            mIntent.setAction("android.intent.action.VIEW");
//            startActivity(mIntent);
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
