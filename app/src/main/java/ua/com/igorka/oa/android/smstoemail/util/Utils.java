package ua.com.igorka.oa.android.smstoemail.util;

import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import org.apache.commons.net.smtp.AuthenticatingSMTPClient;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import ua.com.igorka.oa.android.smstoemail.db.entity.Sms;

/**
 * Created by Igor Kuzmenko on 01.04.2015.
 * Some useful methods
 */
public final class Utils {

    private static SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];
        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }

        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;

        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }

    public static List<Sms> getSmsMessagesFromIntent(Intent intent) {
        SmsMessage[] msgs = getMessagesFromIntent(intent);
        List<Sms> result = new ArrayList<>();
        for (int k,i = 0; i < msgs.length; i = i + k + 1) {
            k = 0;
            result.add(convertSmsMessageToSms(msgs[i]));
            for (int j = i + 1; j < msgs.length; j++) {
                if (msgs[i].getDisplayOriginatingAddress().equals(msgs[j].getDisplayOriginatingAddress())
                        && msgs[i].getTimestampMillis() == msgs[j].getTimestampMillis()) {
                    result.get(i).setDisplayMessageBody(result.get(i).getDisplayMessageBody() + msgs[j].getDisplayMessageBody());
                    k++;
                }
                else {
                    break;
                }
            }
        }
        return result;
    }

    private static Sms convertSmsMessageToSms(SmsMessage message) {
        Sms sms = new Sms();
        sms.setOriginatingAddress(message.getOriginatingAddress());
        sms.setDisplayOriginatingAddress(message.getDisplayOriginatingAddress());
        sms.setDisplayMessageBody(message.getDisplayMessageBody());
        sms.setTimestamp(String.valueOf(message.getTimestampMillis()));
        return sms;
    }



    /**
     * This method sends email.
     * intent MUST contains the following extras:
     * Intent.EXTRA_EMAIL
     * Intent.EXTRA_SUBJECT
     * Intent.EXTRA_TEXT
     *
     * @param intent contains emails
     * @throws Exception
     */
    public static void sendEmail(Intent intent) throws Exception {
        String hostname = AppPreferences.SMTP.getInstance().getSmtpServer();
        int port = Integer.parseInt(AppPreferences.SMTP.getInstance().getSmtpPort());
        String password = AppPreferences.SMTP.getInstance().getSmtpPassword();
        String login = AppPreferences.SMTP.getInstance().getSmtpLogin();

        String subject = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);

        AuthenticatingSMTPClient client = new AuthenticatingSMTPClient("TLS", "UTF-8");
        try {
            String[] to = intent.getStringArrayExtra(Intent.EXTRA_EMAIL);
            // optionally set a timeout to have a faster feedback on errors
            client.setDefaultTimeout(10 * 1000);
            // you connect to the SMTP server
            client.connect(hostname, port);
            // you say ehlo  and you specify the host you are connecting from, could be anything
            client.ehlo("localhost");
            // if your host accepts STARTTLS, we're good everything will be encrypted, otherwise we're done here
            if (client.execTLS()) {

                client.auth(AuthenticatingSMTPClient.AUTH_METHOD.LOGIN, login, password);
                checkReply(client);

                client.setSender(login);
                checkReply(client);

                for (String to_ : to) {
                    client.addRecipient(to_);
                }
                checkReply(client);

                Writer writer = client.sendMessageData();

                if (writer != null) {
                    SimpleSMTPHeader header = new SimpleSMTPHeader(login, to[0], subject);
                    writer.write(header.toString());
                    writer.write(text);
                    writer.close();
                    if (!client.completePendingCommand()) {// failure
                        throw new Exception("Failure to send the email " + client.getReply() + client.getReplyString());
                    }
                } else {
                    throw new Exception("Failure to send the email " + client.getReply() + client.getReplyString());
                }
            } else {
                throw new Exception("STARTTLS was not accepted " + client.getReply() + client.getReplyString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.logout();
            client.disconnect();
        }
    }

    private static void checkReply(SMTPClient sc) throws Exception {
        if (SMTPReply.isNegativeTransient(sc.getReplyCode())) {
            throw new Exception("Transient SMTP error " + sc.getReply() + sc.getReplyString());
        } else if (SMTPReply.isNegativePermanent(sc.getReplyCode())) {
            throw new Exception("Permanent SMTP error " + sc.getReply() + sc.getReplyString());
        }
    }

}
