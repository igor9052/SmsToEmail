package ua.com.igorka.oa.android.smstoemail.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ua.com.igorka.oa.android.smstoemail.R;

/**
 * Created by Igor Kuzmenko on 02.04.2015.
 *
 */
public final class AppPreferences {

    private static Context context = App.getContext();
    private static AppPreferences appPreferencesInstance = null;
    private static SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    private static final String EMPTY_STRING = "";

    private AppPreferences() {
    }

    public static AppPreferences getInstance() {
        if (appPreferencesInstance == null) {
            appPreferencesInstance = new AppPreferences();
        }
        return appPreferencesInstance;
    }

    public static class SMTP {
        private static final String SMTP_SERVER = context.getResources().getString(R.string.pref_key_name_smtp_server);
        private static final String SMTP_PORT = context.getResources().getString(R.string.pref_key_name_smtp_port);
        private static final String SMTP_LOGIN = context.getResources().getString(R.string.pref_key_name_smtp_login);
        private static final String SMTP_PASSWORD = context.getResources().getString(R.string.pref_key_name_smtp_password);


        //private SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        private static SMTP smtpHelper = null;
        private String smtpServer;
        private String smtpPort;
        private String smtpLogin;
        private String smtpPassword;

        private SMTP() {
            updateSMTPSettings();
            sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    updateSMTPSettings();
                }
            });
        }

        private void updateSMTPSettings() {
            smtpServer = sharedPreferences.getString(SMTP_SERVER, EMPTY_STRING);
            smtpPort = sharedPreferences.getString(SMTP_PORT, EMPTY_STRING);
            smtpLogin = sharedPreferences.getString(SMTP_LOGIN, EMPTY_STRING);
            smtpPassword = sharedPreferences.getString(SMTP_PASSWORD, EMPTY_STRING);
        }

        public static SMTP getInstance() {
            if (smtpHelper == null) {
                smtpHelper = new SMTP();
            }
            return smtpHelper;
        }

        public boolean isConfigured() {
            if (EMPTY_STRING.equals(getSmtpServer())) {
                return false;
            }
            if (EMPTY_STRING.equals(getSmtpPort())) {
                return false;
            }
            if (EMPTY_STRING.equals(getSmtpLogin())) {
                return false;
            }
            else if (EMPTY_STRING.equals(getSmtpPassword())) {
                return false;
            }
            return true;
        }

        public String getSmtpServer() {
            return smtpServer;
        }

        public String getSmtpPort() {
            return smtpPort;
        }

        public String getSmtpLogin() {
            return smtpLogin;
        }

        public String getSmtpPassword() {
            return smtpPassword;
        }
    }

    public static class Email {

        private static final String EMAIL_TO = context.getResources().getString(R.string.pref_key_name_recipients);
        private static final String EMAIL_SUBJECT = context.getResources().getString(R.string.pref_key_name_subject);

        private static Email emailInstance = null;
        private String[] recipients = null;
        private String subject;

        public String[] getRecipients() {
            return recipients;
        }

        public String getSubject() {
            return subject;
        }

        private Email() {
            updateEmailSettings();
            sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    updateEmailSettings();
                }
            });
        }

        private void updateEmailSettings() {
            recipients = getArrayOfRecipients(sharedPreferences.getString(EMAIL_TO, EMPTY_STRING));
            subject = sharedPreferences.getString(EMAIL_SUBJECT, EMPTY_STRING);
        }

        private String[] getArrayOfRecipients(String str) {
            String[] result = str.split(context.getString(R.string.recipients_delimiter));
            for (int i = 0; i < result.length; i++) {
                result[i] = result[i].trim();
            }
            return result;
        }

        public static Email getInstance() {
            if (emailInstance == null) {
                emailInstance = new Email();
            }
            return emailInstance;
        }

        public boolean isConfigured() {
            if (getRecipients() == null) {
                return false;
            }
            else {
                for (String r : getRecipients()) {
                    if (EMPTY_STRING.equals(r)) {
                        return false;
                    }
                }
            }
            return !(EMPTY_STRING.equals(getSubject()));
        }
    }
}

