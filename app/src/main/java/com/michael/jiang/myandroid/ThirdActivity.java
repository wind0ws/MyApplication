package com.michael.jiang.myandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ThirdActivity extends Activity {

    private Button btnReturn;
    private  Button btnFirstActivity;
    private Button btnSecondActivity;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        btnReturn= (Button) findViewById(R.id.btn_return);
        btnFirstActivity= (Button) findViewById(R.id.btn_firstactivity);
        btnSecondActivity=(Button)findViewById(R.id.btn_secondactivity);
        editText=(EditText)findViewById(R.id.editTextContent);
        textView= (TextView) findViewById(R.id.txt_msg);

        Intent intent=ThirdActivity.this.getIntent();
        final Bundle bundle=intent.getExtras();
        String message=bundle.getString("FirstActivity");
        Log.i("HuangDebug",message);
        if(message==null||message.equals(""))
        {
            message=bundle.getString("SecondActivity");
        }

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent1=getIntentFromUI();
                ThirdActivity.this.setResult(IFinalActivity.RESULT_CANCEL,intent1);
                ThirdActivity.this.finish();
            }
        });

        btnFirstActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityByUser(IFinalActivity.RQUST_1ACTVTY);
            }
        });

        btnSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityByUser(IFinalActivity.RQUST_2ACTVTY);
            }
        });

    }

    private Intent getIntentFromUI()
    {
        Intent intent1=new Intent();
        Bundle bundle1=new Bundle();
        bundle1.putString("ThirdActivity",String.valueOf(editText.getText()));
        Log.i("HuangDebug","ThirdActivity中输入的信息"+String.valueOf(editText.getText()));
        intent1.putExtras(bundle1);
        return  intent1;
    }

    private void startActivityByUser(int requestCode)
    {
        Intent intent=getIntentFromUI();
        switch (requestCode)
        {
            case IFinalActivity.RQUST_1ACTVTY:
                intent.setClass(ThirdActivity.this,MainActivity.class);
                break;
            case IFinalActivity.RQUST_2ACTVTY:
                intent.setClass(ThirdActivity.this,SecondActivity.class);
                break;
        }
        startActivityForResult(intent,requestCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_third, menu);
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
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        Bundle bundle=data.getExtras();
        String message="";
        switch (requestCode)
        {
            case IFinalActivity.RQUST_1ACTVTY:
                message=bundle.getString("FirstActivity");
                break;
            case IFinalActivity.RQUST_2ACTVTY:
                message=bundle.getString("SecondActivity");
                break;
        }
        editText.setText("ResultCode:"+String.valueOf(resultCode)+message);
    }

}
