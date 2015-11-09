package com.michael.jiang.webviewstudy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private WebView webView;
    private ProgressDialog progressDialog;
    private Button btnBrowse;
    private EditText editUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //openUriBySystem();
        init();
    }

    private void init() {
        btnBrowse= (Button) this.findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=editUrl.getText().toString();
                if(url!=null&&!url.equals("")){
                    if(!url.contains("http://"))
                    {
                        url="http://"+url;
                    }
                    if(!url.substring(url.length()-1).equals("/"))
                    {
                        url+="/";
                    }
                Log.i("Thresh0ld",String.format("EditText中的URL是：%s",url));
                MainActivity.this.webView.loadUrl(url);
                }
            }
        });
        editUrl= (EditText) this.findViewById(R.id.editURL);
        editUrl.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER)
                {
                    btnBrowse.callOnClick();
                    return true;
                }
                return false;
            }
        });
        webView= (WebView) this.findViewById(R.id.webView);
        webView.loadUrl("http://www.baidu.com/");
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用本地缓存
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;//返回值是true的时候控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器去打开
            }
          }
        );
        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                  if(newProgress==100)
                  {
                      closeProgressDialog();
                  }else
                  {
                      showProgressDialog(newProgress);
                  }
                  }
         }
        );
    }

  long firstBackOccurTime=0l;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                if(webView.canGoBack())
                {
                    webView.goBack();
                }else
                {
                   if(firstBackOccurTime==0l)
                   {
                       firstBackOccurTime=event.getEventTime();
                       Toast.makeText(MainActivity.this,"再按一次返回键退出程序。",Toast.LENGTH_SHORT).show();
                   }else
                   {
                       long timeSpan=event.getEventTime()-firstBackOccurTime;
                       Log.i("Thresh0ld",String.format("两次按返回键的时间间隔：%d",timeSpan));
                      if(timeSpan>2000l)
                      {
                          firstBackOccurTime=event.getEventTime();
                          Toast.makeText(MainActivity.this,"再按一次返回键退出程序。",Toast.LENGTH_SHORT).show();
                      }else
                      {
                          System.exit(0);
                      }
                   }
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showProgressDialog(int newProgress) {
        if(progressDialog==null)
        {
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setTitle("页面加载进度");
            progressDialog.setMessage("页面正在加载，请稍候");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        progressDialog.setProgress(newProgress);
    }

    private void closeProgressDialog() {
        if(progressDialog!=null&&progressDialog.isShowing())
        {
            progressDialog.dismiss();
            progressDialog=null;
        }
    }

    /**
     * 通过系统选择调用哪个程序打开Uri地址
     */
    private void openUriBySystem()
    {
        String uri="http://www.baidu.com/";
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
        startActivity(intent);
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
