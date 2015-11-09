package com.michael.jiang.gesturerecognise;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * 继承SimpleOnGestureListener的类，实现了IOnGestureListener接口方法
 */
public class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {

    private Context context;
    public MyOnGestureListener(Context context) {
        super();
        this.context=context;
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getX()-e2.getX()>200)
        {
            Toast.makeText(context,"向左滑动了",Toast.LENGTH_SHORT).show();
        }else if(e2.getX()-e1.getX()>200)
        {
            Toast.makeText(context,"向右滑动了",Toast.LENGTH_SHORT).show();
        }
        if(e1.getY()-e2.getY()>200){
            Toast.makeText(context,"向上滑动了",Toast.LENGTH_SHORT).show();
        }else if(e2.getY()-e1.getY()>200){
            Toast.makeText(context,"向下滑动了",Toast.LENGTH_SHORT).show();
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
