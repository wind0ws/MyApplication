package com.michael.jiang.seekbarstudy;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class MainActivity extends ActionBarActivity {

    private SeekBar seekBar;
    private TextView textStatus;
    private TextView textProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


    }

    private void init() {
        seekBar = (SeekBar) this.findViewById(R.id.seekBar);
        textStatus = (TextView) this.findViewById(R.id.textStatus);
        textProgress = (TextView) this.findViewById(R.id.textProgress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textStatus.setText("正在滑动 SeekBar");
                textProgress.setText(String.format("当前进度：%d%%,是否fromUser：%b",progress,fromUser));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textStatus.setText("——*——开始拖动SeekBar——*——");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textStatus.setText("——*——停止拖动SeekBar——*——");

            }
        });
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
