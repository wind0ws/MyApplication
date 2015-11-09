package com.michael.jiang.viewpagerstudy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jiang on 2015/1/8.
 */
public class FragmentPage1 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_page1,container,false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Thresh0ld","Fragment已销毁。");
    }
}
