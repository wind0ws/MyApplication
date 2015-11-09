package com.michael.jiang.sqlitedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jiang on 2015/3/20.
 */
public class MyDbHelper extends SQLiteOpenHelper {

    /**
     * 构造函数
     * @param context 上下文
     * @param name 数据库名称
     * @param factory 工厂类
     * @param version 数据库版本号
     */
    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static final int DB_VERSION=2;
    public static  final  String DB_NAME="user.db";
    public static final String TABLE_NAME="usertb";
    public static final String KEY_ID="_id";
    public static final String KEY_NAME="name";
    public static final String KEY_AGE="age";
    public static final String KEY_SEX="sex";
    public static final String KEY_ADDRESS="address";
    public static final String SQL_CREATE_TABLE =String.format("Create Table if not exists %s(%s integer primary key autoincrement,%s text not null,%s integer not null,%s text)",TABLE_NAME,KEY_ID,KEY_NAME,KEY_AGE,KEY_SEX);
    @Override
    public void onCreate(SQLiteDatabase db) {
          Log.i("Thre0ld",String.format("数据库不存在，正创建新数据库usertb"));
           db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Thre0ld",String.format("老版本：%d,新版本：%d",oldVersion,newVersion));
        db.execSQL(upGrageTo2());
    }

    /**
     * 将数据库版本升级到2
     * @return 返回升级数据库的SQL语句
     */
    private String upGrageTo2(){
        return String.format("ALTER Table %s Add Column %s text",TABLE_NAME,KEY_ADDRESS);
    }

}
