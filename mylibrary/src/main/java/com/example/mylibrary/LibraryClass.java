package com.example.mylibrary;

import android.content.Context;
import android.widget.Toast;

/**
 * 自定义库中的类
 */
public class LibraryClass {
    private String msg;
    private Context context;
    public LibraryClass(Context context,String msg){
        this.context=context;
        this.msg=msg;
    }
    public void showMsg(){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
