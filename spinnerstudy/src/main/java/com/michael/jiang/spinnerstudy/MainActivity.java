package com.michael.jiang.spinnerstudy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    //1 新建数据源
    //2 新建适配器
    //3 将适配器与视图进行绑定

    private Spinner spinner;
    private TextView textView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner= (Spinner) this.findViewById(R.id.spinner);
        textView= (TextView) this.findViewById(R.id.txt);
        generateDataList();
        simpleAdapter=new SimpleAdapter(this,dataList,R.layout.spinner_item,new String[]{"Picture","Describe"},new int[]{R.id.image,R.id.text});
        spinner.setAdapter(simpleAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               HashMap<String,Object> map  = (HashMap<String, Object>) simpleAdapter.getItem(position);
                String txt= (String) map.get("Describe");
                textView.setText(txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("Thresh0ld","在Spinner里什么也没选择。");
            }
        });



    }


    private void generateDataList()
    {
        dataList=new ArrayList<>();
        String[] cities ={"北京","上海","广州","深圳","安徽"};
        for(int i=0;i< cities.length;i++)
        {
            Map<String,Object> map=new HashMap<>();
            map.put("Picture",R.drawable.ic_launcher);
            map.put("Describe", cities[i]);
            dataList.add(map);
        }
        Log.i("Thresh0ld","数据生成完毕，大小为="+dataList.size());
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
