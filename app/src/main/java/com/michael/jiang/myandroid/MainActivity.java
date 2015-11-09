package com.michael.jiang.myandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.view.View;


public class MainActivity extends Activity {

    private Button btnSecond;
     private Button btnThird;
    private TextView txtVewMsg;
    private  EditText editContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSecond= (Button) findViewById(R.id.btnSecondActivity);
        btnThird = (Button)findViewById(R.id.btnthirdActivity);
        editContent=(EditText)findViewById( R.id.editTextContent);
        txtVewMsg= (TextView)findViewById(R.id.txtVewInfo);


        btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(IFinalActivity.RQUST_2ACTVTY);
            }
        });

        btnThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(IFinalActivity.RQUST_3ACTVTY);
            }
        });
    }

    private void startNewActivity(int requestCode )
    {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,SecondActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("FirstActivity",String.valueOf(editContent.getText()));
        intent.putExtras(bundle);
        startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode ,int resultCode ,Intent data)
    {
        if(data==null)
        {
            Log.i("MichaelDebug","参数data为空");
            return;
        }
        String message=null;
        switch (requestCode) //分离Bundle信息头
        {
            case IFinalActivity.RQUST_2ACTVTY:
                message=data.getExtras().getString("SecondActivity");
                break;
            case IFinalActivity.RQUST_3ACTVTY:
                message=data.getExtras().getString("ThirdActivity");
                break;
        }


        if(message==null)
        {
             Log.i("HuangDebug","message is null");
            return;
        }
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        txtVewMsg.setText(message);

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
