package com.michael.jiang.progressbarstudy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {


    private Button btnAdd;
    private Button btnReduce;
    private Button btnReset;
    private TextView text;
    private Button btnShowProgressDialog;
    private ProgressBar progressBarHorizontal;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 启用窗口特征，启用带进度和不带进度的进度条
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);

       setProgressBarVisibility(true);
       setProgressBarIndeterminateVisibility(true);
       setProgress(9000);

        init();
    }

    private void init() {
        btnAdd= (Button) this.findViewById(R.id.btnAddIncrement);
        btnReduce= (Button) this.findViewById(R.id.btnReduceIncrement);
        btnReset= (Button) this.findViewById(R.id.btnReset);
        text= (TextView) this.findViewById(R.id.text);
        progressBarHorizontal= (ProgressBar) this.findViewById(R.id.progressBarHorizontal);
        btnShowProgressDialog= (Button) this.findViewById(R.id.btnShowProgressDialog);
        btnAdd.setOnClickListener(this);
        btnReduce.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnShowProgressDialog.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnAddIncrement:{
                progressBarHorizontal.incrementProgressBy(10);
                progressBarHorizontal.incrementSecondaryProgressBy(10);
                break;}
            case R.id.btnReduceIncrement:{
                progressBarHorizontal.incrementProgressBy(-10);
                progressBarHorizontal.incrementSecondaryProgressBy(-12);
                break;}
            case R.id.btnReset:{
                progressBarHorizontal.setProgress(50);
                progressBarHorizontal.setProgress(70);
                break;}
            case R.id.btnShowProgressDialog:{
                showProgressDialog();
                return;}
        }
        int firstProgress=progressBarHorizontal.getProgress();
        int secondProgress=progressBarHorizontal.getSecondaryProgress();
        int firstPercent=(int)(1.00*firstProgress/progressBarHorizontal.getMax() *100);
        int secondPercent=(int)(1.00*secondProgress/progressBarHorizontal.getMax() *100);
        Log.i("Thresh0ld","第一进度百分比"+firstPercent+",第二进度百分比"+secondPercent);
        text.setText(String.format("当前进度%d%%，已加载进度%d%%",firstPercent,secondPercent));
    }

    private void showProgressDialog() {
        if(progressDialog==null)
        {
            progressDialog=new ProgressDialog(MainActivity.this);

            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            progressDialog.setTitle("对话框进度条");
            progressDialog.setMessage("这是进度条对话框的消息");
            progressDialog.setIcon(R.drawable.ic_launcher);

            progressDialog.setMax(100);
            progressDialog.incrementProgressBy(50);
            //progressDialog.setProgress(20);
            progressDialog.setIndeterminate(false);

            progressDialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this,"当前进度："+progressDialog.getProgress(),Toast.LENGTH_SHORT).show();
                            progressDialog=null;
                        }
                    }
            );

            progressDialog.setCancelable(false);
            progressDialog.show();

//            /**
//             * 页面显示风格
//             */
//            //新建ProgressDialog对象
//            progressDialog=new ProgressDialog(MainActivity.this);
//            //设置显示风格
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            //设置标题
//            progressDialog.setTitle("幕课网");
//            //设置对话框里的文字信息
//            progressDialog.setMessage("欢迎大家支持幕课网");
//            //设置图标
//            progressDialog.setIcon(R.drawable.ic_launcher);
//
//            /**
//             * 设定关于ProgressBar的一些属性
//             */
//            //设定最大进度
//            progressDialog.setMax(100);
//            //设定初始化已经增长到的进度
//            progressDialog.incrementProgressBy(50);
//            //进度条是明确显示进度的
//            progressDialog.setIndeterminate(false);
//
//            /**
//             * 设定一个确定按钮
//             */
//
//            progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(MainActivity.this, "欢迎大家支持幕课网", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            //是否可以通过返回按钮退出对话框
//            progressDialog.setCancelable(true);
//            //显示ProgressDialog
//            progressDialog.show();

        }
    }

}
