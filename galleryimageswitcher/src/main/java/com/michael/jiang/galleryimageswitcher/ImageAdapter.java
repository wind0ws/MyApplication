package com.michael.jiang.galleryimageswitcher;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Created by Jiang on 2015/1/12.
 */
public class ImageAdapter extends BaseAdapter {

    private int[]resId;
    private Context context;
    private int galleryPicWidth=200;
    private int galleryPicHeight=150;
    public ImageAdapter(Context context, int[] resId, Bundle bundle) {
        this.resId=resId;
        this.context=context;
        galleryPicWidth=bundle.getInt("Width",200);
        galleryPicHeight=bundle.getInt("Height",150);
        Log.i("Thresh0ld",String.format("Width:%d,Height:%d",galleryPicWidth,galleryPicHeight));
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE; //循环切换图片就需要告诉gallery的数量是无限大的
//        return resId.length;
    }

    @Override
    public Object getItem(int position) {
        return resId[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView=new ImageView(context);
        imageView.setBackgroundResource(resId[position%resId.length]);
        imageView.setLayoutParams(new Gallery.LayoutParams(galleryPicWidth,galleryPicHeight));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }
}
