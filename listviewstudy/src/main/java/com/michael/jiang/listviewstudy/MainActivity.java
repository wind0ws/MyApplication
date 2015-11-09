package com.michael.jiang.listviewstudy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Jiang on 2014/12/29.
 */
public class MainActivity extends Activity {
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> mapList;

    static final String TAG="Thresh0ld";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        listView= (ListView) this.findViewById(R.id.list_item);

        String[] strings={"Michael","Threshold","All","Need","Study","You are prepare to walk"};
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strings);


        mapList=new ArrayList<Map<String,Object>>();
        generateData();//获取mapList数据
        simpleAdapter =new SimpleAdapter(this,mapList,R.layout.item_layout,new String[]{"pic","text"},new int[]{R.id.pic,R.id.text});

       // listView.setAdapter(arrayAdapter);
        listView.setAdapter(simpleAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch(scrollState)
                {
                    case SCROLL_STATE_FLING: //当手指离开屏幕前粗暴地用力往前滑了一下，所以视图当前正在依靠惯性滑动中
                        Map<String,Object> map=new HashMap<String, Object>();
                        map.put("pic",R.drawable.ic_launcher);
                        map.put("text","增加项");
                        mapList.add(map);
                        simpleAdapter.notifyDataSetChanged();
                        Log.i(TAG,"事件SCROLL_STATE_FLING");
                        break;
                    case SCROLL_STATE_IDLE://视图停止滑动
                        Log.i(TAG,"事件SCROLL_STATE_IDLE");
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL://手指还在屏幕上滑动
                        Log.i(TAG,"事件SCROLL_STATE_TOUCH_SCROLL");
                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i(TAG,"事件onScroll ————正在滚动。");

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text=listView.getItemAtPosition(position).toString();//获取点击那个Item的信息
                Toast.makeText(MainActivity.this,"Item Text："+text+" position:"+position,Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 生成mapList数据。 mapList是 'List<Map<String,Object>>
     */
    private void generateData()
    {
        for(int i=0;i<20;i++)
        {
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("pic",R.drawable.ic_launcher);
            map.put("text","Son,you need hurry"+i);
            mapList.add(map);

        }
    }




}
