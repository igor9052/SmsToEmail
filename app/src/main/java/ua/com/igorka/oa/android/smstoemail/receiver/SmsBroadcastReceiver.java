package ua.com.igorka.oa.android.smstoemail.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ua.com.igorka.oa.android.smstoemail.service.SendEmailService;

/**
 * Created by Igor Kuzmenko on 01.04.2015.
 * This broadcast receiver gets SMS messages and sends them to SendEmailService class
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = SmsBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        intent.setClass(context, SendEmailService.class);
        Log.d(TAG, "ON_RECEIVE");
        context.startService(intent);
    }
}
