package com.michael.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Jiang on 2015/7/13.
 */
public class ImageTxtButton extends LinearLayout {

    private ImageView imageView;
    private TextView textView;

    public ImageTxtButton(Context context) {
        this(context, null);
    }

    public ImageTxtButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.image_text_button, this, true);
        imageView = (ImageView) findViewById(R.id.iv);
        textView = (TextView) findViewById(R.id.tv);
    }

    public void setTextViewText(String text)
    {
        textView.setText(text);
    }

    public void setImageViewImageResource(int resID)
    {
        imageView.setImageResource(resID);
    }



}
