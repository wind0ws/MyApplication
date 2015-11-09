package com.michael.jiang.tweenanimation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Button btn_alpha;//渐变动画
    Button btn_translate;//转移动画
    Button btn_scale;//缩放动画
    Button btn_rotate;//旋转动画
    Button btn_set;//综合动画
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btn_alpha = (Button) this.findViewById(R.id.btn_alpha);
        btn_translate = (Button) this.findViewById(R.id.btn_translate);
        btn_scale = (Button) this.findViewById(R.id.btn_scale);
        btn_rotate = (Button) this.findViewById(R.id.btn_rotate);
        btn_set = (Button) this.findViewById(R.id.btn_set);
        imageView = (ImageView) this.findViewById(R.id.image);
        btn_alpha.setOnClickListener(this);
        btn_translate.setOnClickListener(this);
        btn_scale.setOnClickListener(this);
        btn_rotate.setOnClickListener(this);
        btn_set.setOnClickListener(this);
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

        Animation animation=null;
        switch(v.getId())
        {
            case R.id.btn_alpha:
                //animation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
                animation=getAlpha();
                break;
            case R.id.btn_translate:
               // animation = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
                animation = getTranslate();
                break;
            case R.id.btn_rotate:
//                animation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
                animation = getRotate();
                break;
            case R.id.btn_scale:
//                animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
                animation = getScale();
                break;
            case R.id.btn_set:
//                animation = AnimationUtils.loadAnimation(this, R.anim.anim_set);
                animation = getAnimationSet();
                break;

        }
      //  imageView.setAnimation(animation);
        imageView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Toast.makeText(MainActivity.this,"开始动画",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(MainActivity.this,"结束动画",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Toast.makeText(MainActivity.this,"重复动画",Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * 渐变动画
     * @return 返回渐变动画对象
     */
    private Animation getAlpha()
    {
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.1f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(5000);
        alphaAnimation.setRepeatCount(2);//设成-1动画一直循环
        alphaAnimation.setInterpolator(this,android.R.anim.bounce_interpolator);
        return  alphaAnimation;
    }

    /**
     * 转移动画
     * @return
     */
    private  Animation getTranslate()
    {
        TranslateAnimation animation=new TranslateAnimation(0,50,0,-100);
        animation.setDuration(1000);
        animation.setInterpolator(this,android.R.anim.accelerate_interpolator);
        animation.setFillAfter(true);
        return  animation;

    }

    /**
     * 缩放动画
     * @return
     */
    private  Animation getScale()
    {
        ScaleAnimation animation=new ScaleAnimation(1,2,1,2,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        animation.setFillAfter(true);
        animation.setDuration(1000);
        animation.setInterpolator(this,android.R.anim.cycle_interpolator);
        return  animation;
    }

    /**
     * 旋转动画
     * @return
     */
    private Animation getRotate()
    {
        RotateAnimation animation=new RotateAnimation(0,270,0.5f,0.5f);
        animation.setFillAfter(true);
        animation.setDuration(5000);
        animation.setInterpolator(this,android.R.anim.overshoot_interpolator);
        //animation.setRepeatCount(-1);//一直循环
        return animation;
    }


    private Animation getAnimationSet()
    {
        AnimationSet animationSet=new AnimationSet(false);
        animationSet.setDuration(2000);
        ScaleAnimation scaleAnimation=new ScaleAnimation(1,0.5f,1,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setInterpolator(this, android.R.anim.accelerate_decelerate_interpolator);
        scaleAnimation.setFillAfter(true);

        TranslateAnimation translateAnimation=new TranslateAnimation(0,-100,0,-100);
        translateAnimation.setDuration(1000);
        translateAnimation.setInterpolator(this,android.R.anim.linear_interpolator);
        translateAnimation.setFillAfter(true);
        translateAnimation.setStartOffset(1000);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);

        return animationSet;
    }


}
