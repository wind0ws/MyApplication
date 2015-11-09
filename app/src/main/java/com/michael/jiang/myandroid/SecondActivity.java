package com.michael.jiang.myandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;


public class SecondActivity extends Activity {

    private Button btnFirstActivity;
    private Button btnThirdActivity;
    private EditText editContent;
    private TextView txtShow;
    private Button btnReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btnFirstActivity= (Button) findViewById(R.id.btn_first);
        btnThirdActivity= (Button) findViewById(R.id.btn_third);
        editContent= (EditText) findViewById(R.id.editTextContent);
        txtShow= (TextView) findViewById(R.id.txtViewreceiveMsg);
        btnReturn = (Button) findViewById(R.id.btn_return);

        final Bundle bundle=this.getIntent().getExtras();
        String msg=bundle.getString("FirstActivity");
        if(msg==null||msg.equals(""))
        {
            msg=bundle.getString("ThirdActivity");
        }

        txtShow.setText(msg);

        btnFirstActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(IFinalActivity.RQUST_1ACTVTY);
            }
        });

        btnThirdActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(IFinalActivity.RQUST_3ACTVTY);
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                Bundle bundle1=new Bundle();
                bundle1.putString("SecondActivity",String.valueOf(editContent.getText()));
                intent.putExtras(bundle1);
               SecondActivity.this.setResult(IFinalActivity.RESULT_OK, intent);
                SecondActivity.this.finish();
            }
        });
    }


    private void startNewActivity(int requestCode)
    {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        String content= String.valueOf(editContent.getText());
        switch (requestCode)
        {
            case IFinalActivity.RQUST_1ACTVTY:
                 intent.setClass(SecondActivity.this,MainActivity.class);

                break;
            case IFinalActivity.RQUST_3ACTVTY:
                intent.setClass(SecondActivity.this,ThirdActivity.class);

                break;
        }
        bundle.putString("SecondActivity",content);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
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
        Bundle bundle= data.getExtras();
        String message="";
        switch (requestCode)
        {
            case IFinalActivity.RQUST_1ACTVTY:
                message=bundle.getString("FirstActivity");
                break;
            case IFinalActivity.RQUST_3ACTVTY:
                message=bundle.getString("ThirdActivity");
                break;
        }
        txtShow.setText("Result Code:"+Integer.toString(resultCode)+message);
    }

}
