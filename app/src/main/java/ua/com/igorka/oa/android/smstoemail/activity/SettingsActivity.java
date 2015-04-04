package ua.com.igorka.oa.android.smstoemail.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import ua.com.igorka.oa.android.smstoemail.R;

/**
 * Created by Igor Kuzmenko on 01.04.2015.
 * Present Settings of the app
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
