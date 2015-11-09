package com.michael.jiang.fragmentstudy;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jiang on 2015/1/7.
 */
public class MyFragment2 extends Fragment implements MyFragmentListener {

    private MyFragmentListener listener;
    private Button btn;
    private TextView txt;


    @Override
    public void onAttach(Activity activity) {
        listener= (MyFragmentListener) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment,container,false);
        btn= (Button) view.findViewById(R.id.button);
        txt= (TextView) view.findViewById(R.id.text);
        txt.setText("动态加载的Fragment");
        btn.setText("MyFragment2按钮");
        Bundle bundle= this.getArguments();
        String message="null，没有值";
        try {
             message=bundle.getString("message","defaultValue");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"这个Fragment未设置收取消息机制。",Toast.LENGTH_SHORT).show();
            }
        });
        listener.messageTransfer(message);
        return view;
    }

    //实现接口方法
    @Override
    public void messageTransfer(String message) {
        Toast.makeText(getActivity(),"Fragment接收到Activity发送来的消息："+message,Toast.LENGTH_SHORT).show();
    }
}
