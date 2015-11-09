package com.michael.jiang.fragmentstudy;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jiang on 2015/1/6.
 */
public class MyFragment extends Fragment {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    //layout布局文件转换成View对象
    /**
     * resource:Fragment需要加载的布局文件
     * root：加载layout的父ViewGroup
     * attactToRoot：false，不返回父ViewGroup
     */
    private Button button;
    private TextView textView;

    @Override
    public void onAttach(Activity activity) {
        logInfo("onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        logInfo("onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        logInfo("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logInfo("onCreateView");
       View view= inflater.inflate(R.layout.fragment,container,false);
        button= (Button) view.findViewById(R.id.button);
        textView= (TextView) view.findViewById(R.id.text);
        textView.setText("动态加载的Fragment");
        button.setText("获取内容");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=getMessage();
                if(msg==null)
                {
                    msg="空消息。";
                }
                textView.setText("Fragment收取到消息："+message);
                Toast.makeText(getActivity(),String.format("Fragment收取到的信息：%s",msg),Toast.LENGTH_SHORT).show();
            }
        });
        return  view;
    }

    @Override
    public void onStart() {
        logInfo("onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        logInfo("onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        logInfo("onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        logInfo("onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        logInfo("onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        logInfo("onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        logInfo("onDetach");
        super.onDetach();
    }


    private void logInfo(String info)
    {
       logInfo("Thresh0ld",info,"XX方法已经被调用了:");
    }

    private void logInfo( String tag,String info,String infoPostfix)
    {
        Log.i(tag,String.format("%s,%s",info,infoPostfix));
    }

}
