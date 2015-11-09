package com.michael.jiang.listviewloaddemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.michael.jiang.listviewloaddemo.adapter.ApkListAdapter;
import com.michael.jiang.listviewloaddemo.entity.ApkEntity;
import com.michael.jiang.listviewloaddemo.view.LoadListView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class MainActivity extends ActionBarActivity implements ILoadListener,IRefreshListener {

    List<ApkEntity> apkEntityList=new ArrayList<ApkEntity>();
    ApkListAdapter apkListAdapter;
    LoadListView loadListView;
    IRefreshListener iRefreshListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadListView = (LoadListView) this.findViewById(R.id.load_listView);
        getData();
        showListView();
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

    private void showListView() {

        if (apkListAdapter == null) {
            apkListAdapter = new ApkListAdapter(this, apkEntityList);
            loadListView.setILoadListener(this);
            loadListView.setIRefreshListener(this);
            loadListView.setAdapter(apkListAdapter);
        } else {
            apkListAdapter.onDataChanged(apkEntityList);
        }
    }
    private void getData()
    {
        for(int i=0;i<15;i++){
            ApkEntity apkEntity =new ApkEntity();
            apkEntity.setName("神器App");
            apkEntity.setInfo("安徽三联交通应用技术股份有限公司");
            apkEntity.setDescription("这是一个神奇的应用。");
            apkEntityList.add(apkEntity);
        }
    }

    private void getMoreData()
    {
        for(int i=0;i<3;i++){
            ApkEntity apkEntity =new ApkEntity();
            apkEntity.setName("更多神器");
            apkEntity.setInfo("Com.Michael.Jiang");
            apkEntity.setDescription("找房子，买家具，我只用神器App");
            apkEntityList.add(apkEntity);
        }
    }
    private void getRefreshData()
    {
        for(int i=0;i<10;i++){
            ApkEntity apkEntity =new ApkEntity();
            apkEntity.setName("刷新神器");
            apkEntity.setInfo("Com.Michael.Jiang");
            apkEntity.setDescription("刷新 神器App");
            apkEntityList.add(0,apkEntity);
        }
    }

    @Override
    public void onLoad() {
        android.os.Handler handler=new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMoreData();
                showListView();
                loadListView.loadComplete();
            }
        }, 2000);


    }

    @Override
    public void onRefreshing() {
        android.os.Handler handler=new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getRefreshData();
                showListView();
                loadListView.refreshComplete();
            }
        },2000);

    }
}
