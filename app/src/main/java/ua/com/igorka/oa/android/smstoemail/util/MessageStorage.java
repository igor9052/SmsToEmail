package ua.com.igorka.oa.android.smstoemail.util;

import android.content.Context;
import android.telephony.SmsMessage;

import java.util.List;

import ua.com.igorka.oa.android.smstoemail.db.dao.DAO;
import ua.com.igorka.oa.android.smstoemail.db.dao.impl.SmsDAOImpl;
import ua.com.igorka.oa.android.smstoemail.db.entity.Sms;

/**
 * Created by Igor Kuzmenko on 06.04.2015.
 * This class is for saving messages into the internal storage in case these messages
 * can't be delivered (Internet is absent or other send issues).
 */
public class MessageStorage {
    private static MessageStorage messageStorage = null;
    private DAO<Integer, Sms> dao;

    private MessageStorage(Context context) {
        dao = new SmsDAOImpl(context);
    }

    public static MessageStorage getInstance(Context context) {
        if (messageStorage == null) {
            messageStorage = new MessageStorage(context);
        }
        return messageStorage;
    }

    public void addMessage(SmsMessage smsMessage) {
        Sms sms = new Sms();
        sms.setOriginatingAddress(smsMessage.getOriginatingAddress());
        sms.setDisplayOriginatingAddress(smsMessage.getDisplayOriginatingAddress());
        sms.setDisplayMessageBody(smsMessage.getDisplayMessageBody());
        sms.setTimestamp(String.valueOf(smsMessage.getTimestampMillis()));
        dao.insert(sms);
    }

    public void addMessage(Sms sms) {
        dao.insert(sms);
    }

    public List<Sms> getMessages() {
        List<Sms> result = dao.selectAll();
        if (result != null) {
            dao.deleteAll();
        }
        return result;
    }
}
