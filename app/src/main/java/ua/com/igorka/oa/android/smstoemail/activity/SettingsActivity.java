package ua.com.igorka.oa.android.smstoemail.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.text.InputType;
import android.util.Log;

import ua.com.igorka.oa.android.smstoemail.R;

/**
 * Created by Igor Kuzmenko on 01.04.2015.
 * Presents Settings of the app
 */
public class SettingsActivity extends Activity {

    public static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "ON_DESTROY");
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Log.i(TAG, "TASK_ROOT");
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            Log.i(TAG, "NOT_TASK_ROOT");
            super.onBackPressed();
        }
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        public static final String TAG = SettingsFragment.class.getSimpleName();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.i(TAG, "ON_DESTROY");
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            initSummary();
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePrefsSummary(findPreference(key));
        }

        protected void initSummary() {
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
                initPrefsSummary(getPreferenceScreen().getPreference(i));
            }
        }

        /*
         * Init single Preference
         */
        protected void initPrefsSummary(Preference p) {
            if (p instanceof PreferenceCategory) {
                PreferenceCategory pCat = (PreferenceCategory) p;
                for (int i = 0; i < pCat.getPreferenceCount(); i++) {
                    initPrefsSummary(pCat.getPreference(i));
                }
            } else {
                updatePrefsSummary(p);
            }
        }

        protected void updatePrefsSummary(Preference pref) {
            if (pref == null)
                return;

            if (pref instanceof ListPreference) {
                // List Preference
                ListPreference listPref = (ListPreference) pref;
                listPref.setSummary(listPref.getEntry());

            } else if (pref instanceof EditTextPreference) {
                // EditPreference
                EditTextPreference editTextPref = (EditTextPreference) pref;
                if ((editTextPref.getEditText().getInputType() & 0x80)  == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    if (editTextPref.getEditText().getText().toString().length() != 0) {
                        editTextPref.setSummary("********");
                    }
                    return;
                }
                editTextPref.setSummary(editTextPref.getText());
            }

        }
    }


}
