package com.michael.jiang.sqlitedemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //createAndSelectTable1();
        createAndSelectTable2();
    }

    /**
     * 创建并查询表（采用exexSQL方法）
     */
    private void createAndSelectTable1()
    {
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("user.db", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("Create table if not exists usertb(_id integer primary key autoincrement, name text not null,age integer not null,sex text)");
        sqLiteDatabase.execSQL("insert into usertb(name,sex,age) values('张三','女',18)");
        sqLiteDatabase.execSQL("insert into usertb(name,sex,age) values('李四','男',28)");
        Cursor cursor = sqLiteDatabase.rawQuery("select * from usertb", null);
        if(cursor!=null)
        {
            while (cursor.moveToNext())
            {
                Log.i("Thresh0ld","_id:"+cursor.getInt(cursor.getColumnIndex("_id")));
                Log.i("Thresh0ld","name:"+cursor.getString(cursor.getColumnIndex("name")));
                Log.i("Thresh0ld","age:"+cursor.getInt(cursor.getColumnIndex("age")));
                Log.i("Thresh0ld","sex:"+cursor.getString(cursor.getColumnIndex("sex")));
                Log.i("Thresh0ld","这一次的循环结束。");
            }
        }
        cursor.close();
        sqLiteDatabase.close();
    }

    private  void createAndSelectTable2()
    {
        MyDbHelper dbHelper=new MyDbHelper(this,MyDbHelper.DB_NAME,null,MyDbHelper.DB_VERSION);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MyDbHelper.KEY_NAME,"黄守江");
        contentValues.put(MyDbHelper.KEY_AGE,24);
        contentValues.put(MyDbHelper.KEY_SEX,"男");
        contentValues.put(MyDbHelper.KEY_ADDRESS,"环湖东路四十五号");
        writableDatabase.insert(MyDbHelper.TABLE_NAME,null,contentValues);
        Cursor query = writableDatabase.query(MyDbHelper.TABLE_NAME, new String[]{MyDbHelper.KEY_ID, MyDbHelper.KEY_NAME, MyDbHelper.KEY_AGE}, MyDbHelper.KEY_ID + ">?", new String[]{"1"}, null, null, MyDbHelper.KEY_ID);
        if(query!=null)
        {
            while (query.moveToNext())
            {
                Log.i("Thresh0ld",String.format("ID:%d",query.getInt(query.getColumnIndex(MyDbHelper.KEY_ID))));
                Log.i("Thresh0ld",String.format("Name:%s",query.getString(query.getColumnIndex(MyDbHelper.KEY_NAME))));
                Log.i("Thresh0ld",String.format("Age:%d",query.getInt(query.getColumnIndex(MyDbHelper.KEY_AGE))));
//                String address=query.getString(query.getColumnIndex(MyDbHelper.KEY_ADDRESS));
//                Log.i("Thresh0ld",String.format("Address:%s",address==null?"":address));
            }
        }
        query.close();
        writableDatabase.close();
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
