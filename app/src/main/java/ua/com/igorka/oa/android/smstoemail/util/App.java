package ua.com.igorka.oa.android.smstoemail.util;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import ua.com.igorka.oa.android.smstoemail.R;

/**
 * Created by Igor Kuzmenko on 02.04.2015.
 *
 */
public class App extends Application {

    private static Context mContext;
    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        PreferenceManager.setDefaultValues(mContext, R.xml.preferences, false);
        Log.i("App", "ON_CREATE");
    }
}
