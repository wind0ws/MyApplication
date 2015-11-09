package com.michael.jiang.fragmentstudy;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FourthActivity extends ActionBarActivity implements MyFragmentListener {

    private Button btnSend;
    private Button staticFragmentButton;
    private EditText editText;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        init();
    }

    private void init() {
        btnSend= (Button) this.findViewById(R.id.buttonSend);
        staticFragmentButton= (Button) this.findViewById(R.id.button);
        btnSend.setText("动态加载Fragment并发送消息");
        editText= (EditText) this.findViewById(R.id.edit);
        textView= (TextView) this.findViewById(R.id.text);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragment2 myFragment2=new MyFragment2();
                Bundle bundle=new Bundle();
                bundle.putString("message",editText.getText().toString()+"");
                myFragment2.setArguments(bundle);
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.linearLayout,myFragment2,"fourthDynamicFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        staticFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= FourthActivity.this.getFragmentManager().findFragmentByTag("fourthDynamicFragment");
                if(fragment!=null)
                {
                    MyFragment2 myFragment2= (MyFragment2) fragment;
                    Toast.makeText(FourthActivity.this,"从MyFragment2中提取的消息："+myFragment2.getArguments().getString("message"),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fourth, menu);
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
    public void messageTransfer(String message) {
        Toast.makeText(FourthActivity.this,"接收到Fragment传递的消息:"+message,Toast.LENGTH_SHORT).show();
        textView.setText("Fragment传递消息："+message);
    }
}
