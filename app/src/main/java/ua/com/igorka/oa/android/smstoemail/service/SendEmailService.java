package ua.com.igorka.oa.android.smstoemail.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.List;

import ua.com.igorka.oa.android.smstoemail.R;
import ua.com.igorka.oa.android.smstoemail.activity.MainActivity;
import ua.com.igorka.oa.android.smstoemail.activity.SettingsActivity;
import ua.com.igorka.oa.android.smstoemail.db.entity.Sms;
import ua.com.igorka.oa.android.smstoemail.receiver.ChangeConnectionStateReceiver;
import ua.com.igorka.oa.android.smstoemail.util.App;
import ua.com.igorka.oa.android.smstoemail.util.AppPreferences;
import ua.com.igorka.oa.android.smstoemail.util.MessageStorage;
import ua.com.igorka.oa.android.smstoemail.util.Utils;

/**
 * Created by Igor Kuzmenko on 01.04.2015.
 * Service gets sms messages and resend them as emails
 */
public class SendEmailService extends IntentService {

    public static final String ACTION_POSTPONE_EMAIL = SendEmailService.class.getName() + ".POSTPONE_EMAIL";
    private static final String TAG = SendEmailService.class.getSimpleName();

    public SendEmailService() {
        super(SendEmailService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (!AppPreferences.SMTP.getInstance().isConfigured()) {
            smtpIsNotConfiguredNotification();
            return;
        }
        if (!AppPreferences.Email.getInstance().isConfigured()) {
            emailIsNotConfigured();
            return;
        }

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            List<Sms> smsList = Utils.getSmsMessagesFromIntent(intent);
            for (Sms sms : smsList) {
                sendSmsAsEmail(sms);
            }
        }

        /*Messages from storage are sent here*/
        if (intent.getAction().equals(ACTION_POSTPONE_EMAIL)) {
            List<Sms> smsList = MessageStorage.getInstance(App.getContext()).getMessages();
            for (Sms sms : smsList) {
                sendSmsAsEmail(sms);
            }
            /*if all postpone sms have been sent successfully we can disable ConnectionStateReceiver */
            if (MessageStorage.getInstance(App.getContext()).getMessages() == null) {
                disableConnectionStateReceiver();
            }
        }
    }

    /*Trying to send sms. If no internet connection the sms is adding to sms storage
    * and ConnectionStateReceiver becomes enabled. When internet connection is available
     * messages from storage will be sent once more*/
    private void sendSmsAsEmail(Sms sms) {
        try {
            Utils.sendEmail(createEmailIntent(sms));
        } catch (Exception e) {
            MessageStorage.getInstance(App.getContext()).addMessage(sms);
            enableConnectionStateReceiver();
            e.printStackTrace();
        }
        Log.d(TAG, sms.toString());
    }

    private void enableConnectionStateReceiver() {
        ComponentName receiver = new ComponentName(getApplicationContext(), ChangeConnectionStateReceiver.class);
        PackageManager pm = getApplicationContext().getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void disableConnectionStateReceiver() {
        ComponentName receiver = new ComponentName(getApplicationContext(), ChangeConnectionStateReceiver.class);
        PackageManager pm = getApplicationContext().getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void smtpIsNotConfiguredNotification() {
        createAndNotifyNotification(getResources().getString(R.string.notification_text_smtp_is_not_configured));
    }

    private void emailIsNotConfigured() {
        createAndNotifyNotification(getResources().getString(R.string.notification_text_email_is_not_configured));
    }

    //TODO: Implement notification for 4.0.3 android version
    private void createAndNotifyNotification(String contentText) {
        if (Build.VERSION.SDK_INT > 15) {
            Notification.Builder mBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(contentText)
                    .setAutoCancel(true);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
            stackBuilder.addNextIntent(new Intent(this, SettingsActivity.class));
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build());
        }
    }

    private Intent createEmailIntent(Sms message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, AppPreferences.Email.getInstance().getRecipients());
        intent.putExtra(Intent.EXTRA_SUBJECT, AppPreferences.Email.getInstance().getSubject() + " " + message.getOriginatingAddress());
        intent.putExtra(Intent.EXTRA_TEXT, message.getDisplayMessageBody());
        return intent;
    }

}
