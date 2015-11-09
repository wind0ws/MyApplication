package com.michael.jiang.preferencesdemo;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    EditText editUserName;
    EditText editPasswords;
    Button btnLogin;
    Button btnExit;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        SharedPreferences sharedPreferences = this.getSharedPreferences("myConfig", MODE_PRIVATE);
       String userName=sharedPreferences.getString("UserName","");
        String passwords=sharedPreferences.getString("Passwords","");
        if(!userName.isEmpty()&&!passwords.isEmpty())
        {
            editUserName.setText(userName);
            editPasswords.setText(passwords);
            checkBox.setChecked(true);
        }

    }

    private void init() {
        editUserName = (EditText) this.findViewById(R.id.editTextUserName);
        editPasswords = (EditText) this.findViewById(R.id.editTextPasswords);
        btnLogin = (Button) this.findViewById(R.id.btnLogin);
        btnExit = (Button) this.findViewById(R.id.btnExit);
        checkBox = (CheckBox) this.findViewById(R.id.cBxNamePws);

        btnLogin.setOnClickListener(this);
        btnExit.setOnClickListener(this);

        editUserName.setInputType(InputType.TYPE_NULL);
        editUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Toast.makeText(MainActivity.this,"获取焦点："+hasFocus,Toast.LENGTH_SHORT).show();
            }
        });
        editUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"弹出键盘了吗？Click事件仍然可以",Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnLogin:
                String userName=editUserName.getText().toString().trim();
                String passwords=editPasswords.getText().toString().trim();
                if(userName.equals("admin")&&passwords.equals("123456")) {
                    SharedPreferences sharedPreferences = this.getSharedPreferences("myConfig", MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    if (checkBox.isChecked()) {
                        editor.putString("UserName",userName);
                        editor.putString("Passwords",passwords);
                    }else
                    {
                        editor.remove("UserName");
                        editor.remove("Passwords");
                    }
                    editor.commit();
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(this,"登录失败！用户名或密码错误。",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnExit:
                Toast.makeText(this,"程序即将退出",Toast.LENGTH_SHORT).show();
                System.exit(0);
                break;
        }

    }
}
