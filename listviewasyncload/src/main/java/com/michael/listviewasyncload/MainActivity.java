package com.michael.listviewasyncload;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private ListView mListView;
    private static final String mApiUrl = "http://www.imooc.com/api/teacher?type=4&num=30";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
    }

    private void initControls() {
        mListView = (ListView) findViewById(R.id.listView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NewsBean> newsBeanList=null;
                try {
                    newsBeanList = parseJsonData(getStringFromUrl(mApiUrl));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(newsBeanList!=null) {
                    NewsAdapter adapter = new NewsAdapter(MainActivity.this, newsBeanList, mListView);
                    mListView.setAdapter(adapter);
                }
            }
        }).start();

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


    private List<NewsBean> parseJsonData(String jsonData) {
        List<NewsBean> newsBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            NewsBean newsBean;
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                newsBean = new NewsBean();
                newsBean.iconUrl = obj.getString("picSmall");
                newsBean.title = obj.getString("name");
                newsBean.content = obj.getString("description");
                newsBeanList.add(newsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsBeanList;
    }

    private String getStringFromUrl(String apiUrl) throws IOException {
        String result = "";
        String singleLine="";
        InputStream inputStream = new URL(apiUrl).openStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        while ((singleLine=bufferedReader.readLine())!=null) {
            result += singleLine;
        }
        return result;
    }


}
