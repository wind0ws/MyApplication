package com.michael.jiang.earthquake.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Switch;

import com.michael.jiang.earthquake.database.EarthQuakeSQLiteHelper;

/**
 * EarthQuake的ContentProvider
 */
public class EarthQuakeProvider extends ContentProvider {

    public static final int QUAKES=0;
    public static final int QUAKES_ID=1;
    public static final int SEARCH=3;

    public static final UriMatcher uriMatcher;
    private static final String AUTHORITIES="com.michael.provider.EarthQuakeProvider";//这个和manifest中的authorities是一样的

    public static final Uri CONTENT_URIS=Uri.parse("content://"+AUTHORITIES+"/earthquakes");

   static {
       uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
       uriMatcher.addURI(AUTHORITIES,"earthquakes",QUAKES);
       uriMatcher.addURI(AUTHORITIES,"earthquakes/#",QUAKES_ID);

   }

EarthQuakeSQLiteHelper dbHelper;
    @Override
    public boolean onCreate() {
        dbHelper = new EarthQuakeSQLiteHelper(getContext(), EarthQuakeSQLiteHelper.DB_NAME, null, EarthQuakeSQLiteHelper.DB_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder=new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(EarthQuakeSQLiteHelper.DB_TABLE);
        switch (uriMatcher.match(uri)){
            case QUAKES_ID:
                sqLiteQueryBuilder.appendWhere(EarthQuakeSQLiteHelper.KEY_ID+"="+uri.getPathSegments().get(1));
                break;
            default:break;
        }
        String sorterBy;
        if(TextUtils.isEmpty(sortOrder)){
            sorterBy=EarthQuakeSQLiteHelper.KEY_DATE;
        }else{
            sorterBy=sortOrder;
        }
        Cursor cursor = sqLiteQueryBuilder.query(database, projection, selection, selectionArgs, null, null, sorterBy);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case QUAKES: return "vnd.android.cursor.dir/vnd.michael.earthquake";
            case QUAKES_ID: return  "vnd.android.cursor.item/vnd.michael.earthquake";
            default:throw new IllegalArgumentException("UnSupport uri");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        long rowID = sqLiteDatabase.insert(EarthQuakeSQLiteHelper.DB_TABLE, null, values);
        if(rowID>0) {
            Uri uris = ContentUris.withAppendedId(CONTENT_URIS, rowID);
            getContext().getContentResolver().notifyChange(uris,null);//通知插入成功
            return uris;
        }
        throw new IllegalArgumentException("UnSupport uri");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        int count=0;
        switch (uriMatcher.match(uri)){
            case QUAKES:
                count=sqLiteDatabase.delete(EarthQuakeSQLiteHelper.DB_TABLE, selection, selectionArgs);
                break;
            case QUAKES_ID:
                String id = uri.getPathSegments().get(1);
                String where="";
                if(!TextUtils.isEmpty(selection)) {
                    where = " And (" + selection + ")";
                }
               count= sqLiteDatabase.delete(EarthQuakeSQLiteHelper.DB_TABLE, EarthQuakeSQLiteHelper.KEY_ID + "=" +
                        id +where,selectionArgs);
            default:throw new IllegalArgumentException("UnSupport uri");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        int count=0;
        switch (uriMatcher.match(uri)){
            case QUAKES:
               count= sqLiteDatabase.delete(EarthQuakeSQLiteHelper.DB_TABLE, selection, selectionArgs);
                break;
            case QUAKES_ID:
                String id = uri.getPathSegments().get(1);
                String where="";
                if(!TextUtils.isEmpty(selection)) {
                    where = " And (" + selection + ")";
                }
                count = sqLiteDatabase.delete(EarthQuakeSQLiteHelper.DB_TABLE, EarthQuakeSQLiteHelper.KEY_ID + "=" + id
                +where,selectionArgs);
            default:throw new IllegalArgumentException("UnSupport uri");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
