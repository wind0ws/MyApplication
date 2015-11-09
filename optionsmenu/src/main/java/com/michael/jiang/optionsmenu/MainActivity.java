package com.michael.jiang.optionsmenu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShowListView();
    }

    private void ShowListView() {
        ListView listView = (ListView) this.findViewById(R.id.list_item);
        String[] objects={"文件一","文件二","文件三","文件四"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,objects);
        listView.setAdapter(adapter);
        this.registerForContextMenu(listView);//注册上下文菜单
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //添加菜单
        menu.setHeaderTitle("文件操作菜单");
        menu.setHeaderIcon(R.drawable.ic_launcher);
        menu.add(1, 1, 1, "剪切");
        menu.add(1, 2, 1, "复制");
        menu.add(1, 3, 1, "粘贴");
        menu.add(1, 4, 1, "移动");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getGroupId()==1) {
            switch (item.getItemId()) {
                case 1:
                    Toast.makeText(this,"你点击了剪切",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(this,"你点击了复制",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(this,"你点击了粘贴",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(this,"你点击了移动",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.add(1, 103, 1, "菜单一");//第二个参数是itemID这个是唯一的
        menuItem.setTitle("title");
        menuItem.setIcon(R.drawable.ic_launcher);
        menu.add(1,104,1,"菜单二");
        menu.add(1,105,1,"菜单三");

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
            Toast.makeText(this,"你点击了设置菜单",Toast.LENGTH_SHORT).show();
            //return true;
        }else if (id==R.id.action_file)
        {
            Toast.makeText(this,"你点击了文件菜单",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
