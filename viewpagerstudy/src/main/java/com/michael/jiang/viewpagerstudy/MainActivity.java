package com.michael.jiang.viewpagerstudy;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private PagerTabStrip pagerTabStrip;
    private PagerAdapter pagerAdapter;

    private List<View>viewList;
    private List<Fragment> fragmentList;
    private List<String>titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        viewPager= (ViewPager) this.findViewById(R.id.viewPager);
        pagerTabStrip= (PagerTabStrip) this.findViewById(R.id.tabStrip);
        viewList=new ArrayList<View>();
        View view_page1= View.inflate(this,R.layout.view_page1,null);
        View view_page2= View.inflate(this,R.layout.view_page2,null);
        View view_page3= View.inflate(this,R.layout.view_page3,null);
        View view_page4= View.inflate(this,R.layout.view_page4,null);
        viewList.add(view_page1);
        viewList.add(view_page2);
        viewList.add(view_page3);
        viewList.add(view_page4);
        titleList=new ArrayList<String>();
        titleList.add("第一页");
        titleList.add("第二页");
        titleList.add("第三页");
        titleList.add("第四页");

        pagerTabStrip.setDrawFullUnderline(false);//不显示TableStrip的长下划线
        pagerTabStrip.setTextColor(Color.BLUE);
        pagerTabStrip.setTabIndicatorColor(Color.GREEN);
        pagerTabStrip.setBackgroundColor(Color.LTGRAY);

//        pagerAdapter=new MyViewPagerAdapter(viewList,titleList);
//        viewPager.setAdapter(pagerAdapter);

        fragmentList=new ArrayList<Fragment>();
        Fragment fragment1=new FragmentPage1();
        Fragment fragment2=new FragmentPage2();
        Fragment fragment3=new FragmentPage3();
        Fragment fragment4=new FragmentPage4();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);

        viewPager.setOnPageChangeListener(this);

//        FragmentPagerAdapter fragmentPagerAdapter=new MyFragmentPagerAdapter(this.getSupportFragmentManager(),fragmentList,titleList);
//        viewPager.setAdapter(fragmentPagerAdapter);

        FragmentStatePagerAdapter fragmentStatePagerAdapter=new MyFragmentStatePagerAdapter(this.getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(fragmentStatePagerAdapter);

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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i("Thresh0ld",String.format("第%d个页面在onPageScrolled",++position));
    }

    @Override
    public void onPageSelected(int position) {
        Toast.makeText(this,String.format("当前选中的是第%d页面",++position),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i("Thresh0ld",String.format("当前页面onPageScrollStateChanged，状态是%d",state));

    }
}
