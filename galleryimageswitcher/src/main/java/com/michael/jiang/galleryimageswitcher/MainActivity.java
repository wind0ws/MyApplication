package com.michael.jiang.galleryimageswitcher;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private Gallery gallery;
    private ImageSwitcher imageSwitcher;
    private int[] resId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        gallery = (Gallery) this.findViewById(R.id.gallery);
        imageSwitcher = (ImageSwitcher) this.findViewById(R.id.imageSwitcher);
        resId=new int[]{
                R.drawable.item1,R.drawable.item2,R.drawable.item3,R.drawable.item4,R.drawable.item5
                ,R.drawable.item6,R.drawable.item7,R.drawable.item8,R.drawable.item9,R.drawable.item10
                ,R.drawable.item11,R.drawable.item12
        };
        Bundle bundle=new Bundle();
       // int width = gallery.getWidth() * 2 / 3;
        bundle.putInt("Width",800);
        //int height = gallery.getHeight() * 2 / 3;
        bundle.putInt("Height",350);
       // Log.i("Thresh0ld", String.format("待传递的width：%d ,height:%d", width, height));

        ImageAdapter imageAdapter=new ImageAdapter(this,resId,bundle);
        gallery.setAdapter(imageAdapter);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageSwitcher.setBackgroundResource(resId[position%resId.length]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;
            }
        });
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_out));

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
