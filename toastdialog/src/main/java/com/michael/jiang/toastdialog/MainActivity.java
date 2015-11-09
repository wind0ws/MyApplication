package com.michael.jiang.toastdialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import static com.michael.jiang.toastdialog.R.id.edit_content;


public class MainActivity extends ActionBarActivity {

    private Button btnToast1;
    private Button btnToast2;
    private Button btnToast3;
    private Button btnToast4;
    private Button btnDialog1;
    private Button btnDialog2;
    private Button btnDialog3;
    private Button btnDialog4;
    private Button btnDialog5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnToast1 = (Button) this.findViewById(R.id.btnToast1);
        btnToast2 = (Button) this.findViewById(R.id.btnToast2);
        btnToast3 = (Button) this.findViewById(R.id.btnToast3);
        btnToast4 = (Button) this.findViewById(R.id.btnToast4);
        btnDialog1 = (Button) this.findViewById(R.id.btnDialog1);
        btnDialog2 = (Button) this.findViewById(R.id.btnDialog2);
        btnDialog3 = (Button) this.findViewById(R.id.btnDialog3);
        btnDialog4 = (Button) this.findViewById(R.id.btnDialog4);
        btnDialog5 = (Button) this.findViewById(R.id.btnDialog5);


        btnToast1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"默认样式的Toast",Toast.LENGTH_SHORT).show();
            }
        });
        btnToast2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "带图片的Toast哦。", Toast.LENGTH_SHORT);
                LinearLayout linearLayout= (LinearLayout) toast.getView();
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setImageResource(R.drawable.ic_launcher);
                linearLayout.addView(imageView,0);
                toast.show();
            }
        });
        btnToast3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,-50,50);
                toast.show();
            }
        });
        btnToast4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View toast_view= layoutInflater.inflate(R.layout.toast_layout,null);
                Toast toast = new Toast(MainActivity.this);
                toast.setView(toast_view);
                toast.show();
            }
        });

        btnDialog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("确认取消对话框");
                builder.setIcon(R.drawable.ic_launcher);
                builder.setMessage("确认要退出吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"你点击了确定按钮.which参数="+which,Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "您点击了取消按钮了。which参数值为：" + which, Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        btnDialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] single_list={"男","女","程序猿","女博士"};
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("单选对话框");
                builder.setIcon(R.drawable.ic_launcher);
                builder.setMessage("请选择你的性别");
                builder.setSingleChoiceItems(single_list,0,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"你选择的性别是："+single_list[which],Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        btnDialog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] multi_list={"篮球","足球","男","女","跑步"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("多选对话框");
                builder.setMessage("请选择你的兴趣爱好，可多选。");
                builder.setIcon(R.drawable.ic_launcher);
                builder.setMultiChoiceItems(multi_list, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            Toast.makeText(MainActivity.this, "您喜欢：" + multi_list[which], Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "你不喜欢：" + multi_list[which], Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnDialog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] item_list={"董事长","副总","主任","研发部","工程部"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("列表对话框");
                builder.setMessage("请选择一个。。。。");
                builder.setIcon(R.drawable.ic_launcher);
                builder.setItems(item_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "你选中的是：" + item_list[which], Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    btnDialog5.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View view=inflater.inflate(R.layout.dialog_layout,null);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(view);
            Button btnSubmit = (Button) MainActivity.this.findViewById(R.id.btn_submit);
            final EditText editContent = (EditText) MainActivity.this.findViewById(edit_content);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,"msg:"+editContent.getText().toString(),Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();

        }
    });


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
