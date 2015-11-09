package com.michael.jiang.earthquake.preference;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import com.michael.jiang.earthquake.MainActivity;
import com.michael.jiang.earthquake.R;

import java.util.List;

/**
 * 真正显示Preference的Activity，加载headers
 */
public class EarthQuakePreferenceActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.preference_headers,target);//加载Headers
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
/*        Log.i("Thresh0ld",fragmentName);
        Log.i("Thresh0ld",EarthQuakePreferenceFragment.class.getName());
        String myFragmentName=EarthQuakePreferenceFragment.class.getName();*/

        return fragmentName.equals(EarthQuakePreferenceFragment.class.getName());
    }
}
