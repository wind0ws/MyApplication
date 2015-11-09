package com.michael.jiang.earthquake.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库帮助类
 */
public class EarthQuakeSQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME="earthquakes.db";
    public static final String DB_TABLE="earthquakes";
    public static final int DB_VERSION=1;
    public static final String KEY_ID="_id";
    public static final String KEY_DATE="date";
    public static final String KEY_DETAILS="details";
    public static final String KEY_MAGNITUDE="magnitude";
    public static final String CREATE_TABLE="create table if not exists "+DB_TABLE+
            " ("+KEY_ID+" integer primary key autoincrement, "+KEY_DATE+" integer, "+
            KEY_DETAILS+" text, "+KEY_MAGNITUDE+" float);";


    public EarthQuakeSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Thresh0ld","update db from "+oldVersion+ " to "+newVersion+" will destroy all data");
        db.execSQL("drop table if exits "+DB_TABLE);
        onCreate(db);
    }
}
