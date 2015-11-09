package com.michael.jiang.earthquake;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.michael.jiang.earthquake.preference.EarthQuakePreferenceActivity;
import com.michael.jiang.earthquake.preference.EarthQuakePreferenceFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = this.getFragmentManager();
       earthQuakeListFragment = (EarthQuakeListFragment) fragmentManager.findFragmentById(R.id.earthQuakeFragment);
    }

    private static final int MENU_PREFERENCE=Menu.FIRST+1;
    
    private static final int SHOW_PREFERENCE=1;

    public static final String DELIVER_MSG_TO_PREF="DeliverMsgToPref";

    public boolean autoUpdateChecked=false;
    public int minMagnitude=3;
    public int refreshFreq=60;

    private EarthQuakeListFragment earthQuakeListFragment;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add(1, MENU_PREFERENCE, 1, "首选项");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       switch (id){
           case R.id.action_settings:
               toastMyText("你点击了设置，但是我并未做任何处理",Toast.LENGTH_SHORT);
               toastByMyXml("自定义XML的Toast",Toast.LENGTH_SHORT);
               return true;
           case MENU_PREFERENCE:
               Intent intent=new Intent(this, EarthQuakePreferenceActivity.class);
/*               Bundle bundle=new Bundle();
               bundle.putString(DELIVER_MSG_TO_PREF,"测试父Activity传递给子Activity消息");
               intent.putExtras(bundle);*/
               this.startActivityForResult(intent,SHOW_PREFERENCE);
               return true;
       }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SHOW_PREFERENCE:
                if(resultCode==RESULT_OK){
                    updateAppPreference();
                }
                break;
        }
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
               earthQuakeListFragment.refreshEarthQuakes();
            }
        });
        thread.start();

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateAppPreference() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
       autoUpdateChecked= prefs.getBoolean(EarthQuakePreferenceFragment.PREF_AUTO_UPDATE, false);
        minMagnitude = Integer.parseInt(prefs.getString(EarthQuakePreferenceFragment.PREF_MIN_MAGNITUDE, "3"));
        refreshFreq = Integer.parseInt( prefs.getString(EarthQuakePreferenceFragment.PREF_FREQ, "60"));

    }


    private void toastMyText(String msg, int length) {
        Toast toast = Toast.makeText(this, msg, length);
        LinearLayout linearLayout= (LinearLayout) toast.getView();
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(android.R.drawable.ic_dialog_map);
        linearLayout.addView(imageView, 0);
       toast.setGravity(Gravity.CENTER_HORIZONTAL,0,100);
        toast.show();
    }
    private void toastByMyXml(String msg,int displayLength) {
        Toast toast = new Toast(this);
        View view = this.getLayoutInflater().inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.toastText);
        textView.setText(msg);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,-500);
        toast.show();

    }
}
