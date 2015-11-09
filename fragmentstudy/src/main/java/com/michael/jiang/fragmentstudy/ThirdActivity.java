package com.michael.jiang.fragmentstudy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Jiang on 2015/1/7.
 */
public class ThirdActivity extends ActionBarActivity implements MyFragmentListener {

    private Button btnChange;
    boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        btnChange= (Button) this.findViewById(R.id.button);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                if(flag)
                {
                    MyFragment2 myFragment2=new MyFragment2();
                    Bundle bundle=new Bundle();
                    bundle.putString("message","ThirdActivity按钮点击切换到MyFragment2的");
                    myFragment2.setArguments(bundle);
                    transaction.replace(R.id.linearLayout,new MyFragment2());
                    flag=!flag;
                }else {
                    MyFragment myFragment=new MyFragment();
                    myFragment.setMessage("切换到Fragment的消息，ThirdActivity设置的消息");
                    transaction.replace(R.id.linearLayout,new MyFragment());
                    flag=!flag;
                }
                transaction.commit();
            }
        });
    }

    @Override
    public void messageTransfer(String message) {
        Toast.makeText(ThirdActivity.this,"接收到的消息："+message,Toast.LENGTH_SHORT).show();
    }
}
