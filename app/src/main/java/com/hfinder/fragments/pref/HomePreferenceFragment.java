package com.hfinder.fragments.pref;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.hfinder.R;

/**
 * Created by root on 01/06/18.
 */

public class HomePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_home);
        Preference c = findPreference(getString(R.string.pref_notif_key));
        c.setOnPreferenceChangeListener(this);
    }



    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast.makeText(getActivity(), "change", Toast.LENGTH_SHORT).show();

        return true;
    }
}
