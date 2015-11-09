package com.michael.jiang.gesturerecognise;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    ImageView myImage;
    GestureDetector gestureDetector;
    GestureOverlayView gestureOverlayView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        myImage = (ImageView) this.findViewById(R.id.imageView);
        gestureOverlayView = (GestureOverlayView) this.findViewById(R.id.gestureOverlayView);
        gestureDetector = new GestureDetector(this, new MyOnGestureListener(this));
        myImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);//转发触摸屏幕时的MotionEvent事件
                return true;
            }
        });

        //上面是GestureDetector的使用方法，主要是写GestureDetector的GestureDetector.OnGestureListener 监听事件，而这个一般继承
        //SimpleOnGestureListener类，这个类继承
        final GestureLibrary library = GestureLibraries.fromRawResource(this, R.raw.gestures);
        library.load();
        gestureOverlayView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                ArrayList<Prediction> recognize = library.recognize(gesture);
                for(Prediction prediction:recognize)
                {
                    if(prediction.score>5.0){
                        if(prediction.name.equals("exit")){
                            MainActivity.this.finish();
                        }else if(prediction.name.equals("next")){
                            Toast.makeText(MainActivity.this,"右箭头，下一个",Toast.LENGTH_SHORT).show();
                        }else if(prediction.name.equals("previous")){
                            Toast.makeText(MainActivity.this,"左箭头，上一个",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
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
