package ua.com.igorka.oa.android.smstoemail.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
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

    public static class SettingsFragment extends PreferenceFragment {

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

    }


}
