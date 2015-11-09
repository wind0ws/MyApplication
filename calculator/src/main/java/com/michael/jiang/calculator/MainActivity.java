package com.michael.jiang.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {
    private Button btn_clear,btn_del,btn_divide,btn_multiply,btn_minus,btn_plus,btn_equal,btn_dot,btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();
    }

    private void initControls()
    {
        editText= (EditText) this.findViewById(R.id.edit_answer);
        btn_clear= (Button) this.findViewById(R.id.btn_clear);
        btn_del= (Button) this.findViewById(R.id.btn_del);
        btn_divide= (Button) this.findViewById(R.id.btn_divide);
        btn_multiply= (Button) this.findViewById(R.id.btn_multiply);
        btn_minus= (Button) this.findViewById(R.id.btn_minus);
        btn_plus= (Button) this.findViewById(R.id.btn_plus);
        btn_equal= (Button) this.findViewById(R.id.btn_equal);
        btn_dot= (Button) this.findViewById(R.id.btn_point);
        btn_0= (Button) this.findViewById(R.id.btn_0);
        btn_1= (Button) this.findViewById(R.id.btn_1);
        btn_2= (Button) this.findViewById(R.id.btn_2);
        btn_3= (Button) this.findViewById(R.id.btn_3);
        btn_4= (Button) this.findViewById(R.id.btn_4);
        btn_5= (Button) this.findViewById(R.id.btn_5);
        btn_6= (Button) this.findViewById(R.id.btn_6);
        btn_7= (Button) this.findViewById(R.id.btn_7);
        btn_8= (Button) this.findViewById(R.id.btn_8);
        btn_9= (Button) this.findViewById(R.id.btn_9);

        btn_clear.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_divide.setOnClickListener(this);
        btn_multiply.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        btn_equal.setOnClickListener(this);
        btn_dot.setOnClickListener(this);
        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);

    }



    private boolean isNextOperateNum=true;
    private boolean isFirstOperateNum=true;
    private double firstOperateNum=0.0d,secondOperateNum=0.0d;
    private String last_operate ="";

    @Override
    public void onClick(View v) {
        String text=editText.getText().toString();
        switch (v.getId())
        {
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
            case R.id.btn_point:
                if(isNextOperateNum)
                {
                    if(!text.equals("0")||v.getId()!=R.id.btn_point) {
                        text = "";
                    }
                    isNextOperateNum=false;
                }
                text+=((Button)v).getText().toString();
                editText.setText(text);
                break;
            case R.id.btn_plus:
            case R.id.btn_minus:
            case R.id.btn_multiply:
            case R.id.btn_divide:
                setOperate(((Button)v).getText().toString());
                break;
            case R.id.btn_clear:
                editText.setText("");
                break;
            case R.id.btn_del:
                if(text.equals(""))
                {
                    return;
                }
                editText.setText(text.substring(0,text.length()-1));
                break;
            case R.id.btn_equal:
                secondOperateNum=Double.parseDouble(text);//设置第二个操作数
                getResult();
                isNextOperateNum=true;
                isFirstOperateNum=true;
                break;
        }
    }

    /**
     * 设置第一、第二操作数以及操作符
     * @param operate
     * 操作符
     */
    private void setOperate(String operate)
    {
        String text=editText.getText().toString();

        if(text.contains("."))
        {
            if(text.substring(text.length()-1).equals("."))//如果最后一位是小数点，则去掉
            {
                text=text.substring(0,text.length()-1);
            }
            String[] strings=text.split("\\.");
            if(strings.length>2)
            {
                Toast.makeText(MainActivity.this, "小数点有问题", Toast.LENGTH_SHORT).show();
                return;
            }
            if(strings[0].isEmpty())
            {
                text="0"+text;
            }
        }
        isNextOperateNum=true;

        if( isFirstOperateNum)
        {
            try {
                firstOperateNum = Double.parseDouble(text);
            }catch (Exception err)
            {
                Log.i(TAG, err.toString());
            }
            isFirstOperateNum=false;
            this.last_operate=operate;
        }else
        {
            try {
                secondOperateNum = Double.parseDouble(text);
                getResult();
                firstOperateNum = Double.parseDouble(editText.getText().toString());
            }catch (Exception err)
            {
                Log.i(TAG,err.toString());
            }
            this.last_operate=operate;
        }
    }

    public static String TAG="Wind0ws";
    private void getResult()
    {
        Log.i(TAG,"第一个操作数："+firstOperateNum +" 第二个操作数："+secondOperateNum +"  操作符为："+ last_operate);
        double result=0.0d;
        switch (last_operate)
        {
            case "+":
                result=firstOperateNum+secondOperateNum;
                break;
            case "－":
                result=firstOperateNum-secondOperateNum;
                break;
            case "×":
                result=firstOperateNum*secondOperateNum;
                break;
            case "÷":
                if(secondOperateNum==0.0)
                {
                    result=0;
                    Toast.makeText(MainActivity.this,"错误：除数不能为0",Toast.LENGTH_SHORT).show();
                }else
                {
                    result=firstOperateNum/secondOperateNum;
                }
                break;
            default:
                break;
        }
        double pointPart=result-(int)result;
        if(pointPart==0.0d)
        {
            int res= (int) result;
            editText.setText(String.valueOf(res));
        }else
        {
            editText.setText(String.valueOf(result));
        }
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
