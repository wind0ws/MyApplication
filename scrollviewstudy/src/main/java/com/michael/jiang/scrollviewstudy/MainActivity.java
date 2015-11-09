package com.michael.jiang.scrollviewstudy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private Button btnUp;
    private Button btnDown;
    private ScrollView scrollView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnUp= (Button) this.findViewById(R.id.btnUp);
        btnDown= (Button) this.findViewById(R.id.btnDown);
        scrollView= (ScrollView) this.findViewById(R.id.scroll);
        textView= (TextView) this.findViewById(R.id.text);
        textView.setText(this.getResources().getString(R.string.content));
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_MOVE:
                        if(scrollView.getScrollY()<=0)
                        {
                            Log.i("Thresh0ld","滑动在顶部");
                        }else if(scrollView.getChildAt(0).getMeasuredHeight()<=scrollView.getHeight()+scrollView.getScrollY())
                        {
                            Log.i("Thresh0ld","滑动到底部");
                            textView.append(MainActivity.this.getResources().getString(R.string.content));
                        }
                        break;
                }
                return false;
            }
        });


        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scrollView.scrollTo(0,-50);
                scrollView.scrollBy(0,-30);
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // scrollView.scrollTo(0,50);
               scrollView.scrollBy(0,30);
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
