package ua.com.igorka.oa.android.smstoemail.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ua.com.igorka.oa.android.smstoemail.service.SendEmailService;

/**
 * Created by Igor Kuzmenko on 01.04.2015.
 * This broadcast receiver gets SMS messages and sends them to SendEmailService class
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent.setClass(context, SendEmailService.class);
        context.startService(intent);
    }
}
