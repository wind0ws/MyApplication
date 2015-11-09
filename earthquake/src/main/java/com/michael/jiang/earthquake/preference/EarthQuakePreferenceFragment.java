package com.michael.jiang.earthquake.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.michael.jiang.earthquake.R;

/**
 * preference_fragment 对应的Fragment类
 */
public class EarthQuakePreferenceFragment extends PreferenceFragment {

    public static final String PREF_AUTO_UPDATE="PREF_AUTO_UPDATE";
    public static final String PREF_FREQ = "PREF_FREQ";
    public static final String PREF_MIN_MAGNITUDE="PREF_MIN_MAGNITUDE";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_fragment);
    }


}
