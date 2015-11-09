package com.michael.jiang.viewpagerstudy;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Jiang on 2015/1/8.
 */
public class MyViewPagerAdapter extends PagerAdapter {
    private List<View> viewList;
    private List<String> titleList;

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public MyViewPagerAdapter( List<View> viewList,List<String >titleList)
    {
        this.viewList=viewList;
        this.titleList=titleList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    //实例化一个页卡
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=viewList.get(position);
        container.addView(view,position);
        return view;
    }
 //销毁一个页卡
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
