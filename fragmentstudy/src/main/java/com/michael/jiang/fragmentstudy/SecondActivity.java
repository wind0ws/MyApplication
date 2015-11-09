package com.michael.jiang.fragmentstudy;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jiang on 2015/1/6.
 */
public class SecondActivity extends ActionBarActivity {

    private Button fragmentButton;
    private TextView fragmentTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        fragmentButton= (Button) this.findViewById(R.id.button);
        fragmentTextView= (TextView) this.findViewById(R.id.text);
        fragmentButton.setText("静态加载的FragmentButton");
      fragmentTextView.setText("静态跳转Activity到这里的Fragment，虽然是静态加载，但是这个Fragment仍然有与其关联的Fragment类，有自己的响应事件方法。" +
              "当然你也可以在这个Fragment的宿主Activity中获取Button覆盖其Fragment中的方法");
       /* fragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTextView.setText("SecondActivity静态加载Fragment，就没有与Fragment类绑定了，就不能使用类里的方法了");
                Toast.makeText(SecondActivity.this,"欢迎Fragment",Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
