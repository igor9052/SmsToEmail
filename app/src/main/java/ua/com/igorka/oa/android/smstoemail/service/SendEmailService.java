package ua.com.igorka.oa.android.smstoemail.service;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import ua.com.igorka.oa.android.smstoemail.util.AppPreferences;
import ua.com.igorka.oa.android.smstoemail.util.Utils;

/**
 * Created by Igor Kuzmenko on 01.04.2015.
 *
 */
public class SendEmailService extends IntentService {

    public static final String TAG = SendEmailService.class.getSimpleName();
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
                    e.printStackTrace();
                }
                Log.d(TAG, message.getOriginatingAddress() + " : " +
                        message.getDisplayOriginatingAddress() + " : " +
                        message.getDisplayMessageBody() + " : " +
                        message.getTimestampMillis());
            }
        }
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
}
