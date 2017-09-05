package com.charmingwong.cwimage.settings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.charmingwong.cwimage.R;
import com.charmingwong.cwimage.util.ApplicationUtils;

/**
 * Created by CharmingWong on 2017/7/4.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        init();
    }

    private void init() {
        ListPreference enginePreference = (ListPreference) findPreference("default_engine");
        enginePreference.setSummary(enginePreference.getEntry());
        enginePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference.getKey().equals("default_engine")) {
                    ListPreference listPreference = ((ListPreference) preference);
                    listPreference.setSummary(listPreference.getEntries()[listPreference.findIndexOfValue((String) newValue)]);
                    preference.getEditor().putString("default_engine", (String) newValue).apply();
                }
                return true;
            }
        });

        ListPreference themePreference = (ListPreference) findPreference("theme");
        themePreference.setSummary(enginePreference.getEntry());
        themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference.getKey().equals("theme")) {
                    ListPreference listPreference = ((ListPreference) preference);
                    listPreference.setSummary(listPreference.getEntries()[listPreference.findIndexOfValue((String) newValue)]);
                    preference.getEditor().putString("theme", (String) newValue).apply();

                    getActivity().getApplicationContext().setTheme(ApplicationUtils.getThemeId(Integer.parseInt((String) newValue)));
                }
                return true;
            }
        });
    }

}
