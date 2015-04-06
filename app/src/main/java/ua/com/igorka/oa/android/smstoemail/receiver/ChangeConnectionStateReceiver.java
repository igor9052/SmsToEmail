package ua.com.igorka.oa.android.smstoemail.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ua.com.igorka.oa.android.smstoemail.service.SendEmailService;

/**
 * Created by Igor Kuzmenko on 06.04.2015.
 *
 */
public class ChangeConnectionStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context)) {
            context.startService(new Intent(context, SendEmailService.class).setAction(SendEmailService.ACTION_POSTPONE_EMAIL));
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
