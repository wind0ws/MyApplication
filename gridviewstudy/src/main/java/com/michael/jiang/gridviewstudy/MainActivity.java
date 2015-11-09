package com.michael.jiang.gridviewstudy;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {


    //三要素：新建数据源（集合、数组等），新建适配器（Adapter），View视图绑定适配器


    private GridView gridView;
    String[] iconNames;

    private List<Map<String,Object>> dataList;

    private SimpleAdapter simpleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView= (GridView) this.findViewById(R.id.grid);
        generateDataList();
        simpleAdapter=new SimpleAdapter(MainActivity.this,dataList,R.layout.grid_item,new String[]{"Picture","Describe"},new int[]{R.id.image,R.id.text});
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,iconNames[position],Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void generateDataList()
    {
        dataList=new ArrayList<>();
        iconNames=new String[] {"联系人","日历","照相机","时钟","游戏中心","信息","铃声","设置",
                "语音","天气","地图","Youtube"};
        int[] icons={R.drawable.address_book,R.drawable.calendar,R.drawable.camera,R.drawable.clock,
        R.drawable.games_control,R.drawable.messenger,R.drawable.ringtone,R.drawable.settings,R.drawable.speech_balloon,
        R.drawable.weather,R.drawable.world,R.drawable.youtube};

        for(int i=0;i<iconNames.length;i++)
        {
            Map<String,Object> map=new HashMap<>();
            map.put("Picture",icons[i]);
            map.put("Describe",iconNames[i]);
            dataList.add(map);
        }
        Log.i("Thresh0ld","生成数据源完成，dataList Size="+dataList.size());
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
