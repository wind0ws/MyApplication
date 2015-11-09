package com.michael.jiang.viewflipperstudy;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ViewFlipper;


public class MainActivity extends ActionBarActivity {

    private ViewFlipper flipper;
    private int[] pictures;
    private float startX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        flipper= (ViewFlipper) this.findViewById(R.id.flipper);
        pictures=new int[] {
                R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4
        };
        for (int i:pictures)
        {
            flipper.addView(getImageView(i));
        }
       /* flipper.setInAnimation(this,R.anim.left_in);
        flipper.setOutAnimation(this, R.anim.left_out);
        flipper.setFlipInterval(3000);
        flipper.startFlipping();*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: //手指按到屏幕
                if(flipper.isFlipping())
                {
                    flipper.stopFlipping();
                }
                startX=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getX()-startX>50)//向右滑动
                {
                    flipper.setInAnimation(this,R.anim.left_in);
                    flipper.setOutAnimation(this,R.anim.left_out);
                    flipper.showPrevious();
                }else if(startX-event.getX()>50)//向左滑动
                {
                    flipper.setInAnimation(this,R.anim.right_in);
                    flipper.setOutAnimation(this,R.anim.right_out);
                    flipper.showNext();
                }break;
            case MotionEvent.ACTION_UP://手指离开屏幕
               break;

        }
        return super.onTouchEvent(event);
    }

    private ImageView getImageView(int resID)
    {
        ImageView image=new ImageView(this);
        image.setImageResource(resID);
        return image;
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
