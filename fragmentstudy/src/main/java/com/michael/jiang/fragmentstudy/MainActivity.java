package com.michael.jiang.fragmentstudy;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    private Button btnAfterDynamicAddFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup= (RadioGroup) this.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        btnAfterDynamicAddFragment= (Button) this.findViewById(R.id.btnAfterDynamicAdd);
        btnAfterDynamicAddFragment.setVisibility(View.GONE);
        btnAfterDynamicAddFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btnInDynamicFragment= (Button) MainActivity.this.findViewById(R.id.button);
                btnInDynamicFragment.setText("动态加载的Fragment，宿主Activity也可以获取到");
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(group==radioGroup)
        {
            FragmentManager fragmentManager = getFragmentManager();
            switch (checkedId)
            {
                case R.id.btnFirst:
                    btnAfterDynamicAddFragment.setVisibility(View.GONE);
                    Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnSecond:
                    FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                    beginTransaction.replace(R.id.frame, new MyFragment(), "DynamicFragment");
                    beginTransaction.addToBackStack("DynamicFragment");
                    beginTransaction.commit();
                    btnAfterDynamicAddFragment.setVisibility(View.VISIBLE);
                    break;
                case R.id.btnThird:
                    btnAfterDynamicAddFragment.setVisibility(View.GONE);
                    Intent intent1=new Intent(MainActivity.this,ThirdActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.btnFourth:
                    btnAfterDynamicAddFragment.setVisibility(View.GONE);
                    Intent intent2=new Intent(MainActivity.this,FourthActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    }
}
