package ua.com.igorka.oa.android.smstoemail.service;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.storage.StorageManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.List;

import ua.com.igorka.oa.android.smstoemail.db.entity.Sms;
import ua.com.igorka.oa.android.smstoemail.receiver.ChangeConnectionStateReceiver;
import ua.com.igorka.oa.android.smstoemail.util.App;
import ua.com.igorka.oa.android.smstoemail.util.AppPreferences;
import ua.com.igorka.oa.android.smstoemail.util.MessageStorage;
import ua.com.igorka.oa.android.smstoemail.util.Utils;

/**
 * Created by Igor Kuzmenko on 01.04.2015.
 *
 */
public class SendEmailService extends IntentService {

    public static final String TAG = SendEmailService.class.getSimpleName();
    public static final String ACTION_POSTPONE_EMAIL = SendEmailService.class.getName() + ".POSTPONE_EMAIL";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
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

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            SmsMessage[] messages = Utils.getMessagesFromIntent(intent);

            for (SmsMessage message : messages){
                try {
                    Utils.sendEmail(createEmailIntent(message));
                } catch (Exception e) {
                    MessageStorage.getInstance(App.getContext()).addMessage(message);
                    enableConnectionStateReceiver();
                    e.printStackTrace();
                }
                Log.d(TAG, message.getOriginatingAddress() + " : " +
                        message.getDisplayOriginatingAddress() + " : " +
                        message.getDisplayMessageBody() + " : " +
                        message.getTimestampMillis());
            }
        }

        if (intent.getAction().equals(ACTION_POSTPONE_EMAIL)) {
            List<Sms> smsList = MessageStorage.getInstance(App.getContext()).getMessages();
            for (Sms message : smsList){
                try {
                    Utils.sendEmail(createEmailIntent(message));
                    disableConnectionStateReceiver();
                } catch (Exception e) {
                    MessageStorage.getInstance(App.getContext()).addMessage(message);
                    enableConnectionStateReceiver();
                    e.printStackTrace();
                }
                Log.d(TAG, message.getOriginatingAddress() + " : " +
                        message.getDisplayOriginatingAddress() + " : " +
                        message.getDisplayMessageBody() + " : " +
                        message.getTimestamp());
            }
        }
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

    //TODO: Implement notification for user if Email (recipient, subject, etc) is not configured
    private void emailIsNotConfigured() {

    }

    //TODO: Implement notification for user that SMTP is not configured
    private void smtpIsNotConfiguredNotification() {

    }

    private Intent createEmailIntent(SmsMessage message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, AppPreferences.Email.getInstance().getRecipients());
        intent.putExtra(Intent.EXTRA_SUBJECT, AppPreferences.Email.getInstance().getSubject() + message.getOriginatingAddress());
        intent.putExtra(Intent.EXTRA_TEXT, message.getDisplayMessageBody());
        return intent;
    }
    private Intent createEmailIntent(Sms message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, AppPreferences.Email.getInstance().getRecipients());
        intent.putExtra(Intent.EXTRA_SUBJECT, AppPreferences.Email.getInstance().getSubject() + message.getOriginatingAddress());
        intent.putExtra(Intent.EXTRA_TEXT, message.getDisplayMessageBody());
        return intent;
    }


}
