package ua.com.igorka.oa.android.smstoemail.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Igor Kuzmenko on 06.04.2015.
 * Contract and Helper for sms db
 */
public final class SmsStorageDbContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_PRIMARY_KEY_TYPE = " INTEGER PRIMARY KEY";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_SMS_TABLE =
            "CREATE TABLE " + SmsTable.TABLE_NAME + " (" +
                    SmsTable._ID + INTEGER_PRIMARY_KEY_TYPE + COMMA_SEP +
                    SmsTable.COLUMN_NAME_ORIGINATING_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    SmsTable.COLUMN_NAME_DISPLAY_ORIGINATING_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    SmsTable.COLUMN_NAME_DISPLAY_MESSAGE_BODY + TEXT_TYPE + COMMA_SEP +
                    SmsTable.COLUMN_NAME_TIMESTAMP + TEXT_TYPE + ")";

    private SmsStorageDbContract() {
    }

    public static abstract class SmsTable implements BaseColumns {
        public static final String TABLE_NAME = "sms";
        public static final String COLUMN_NAME_ORIGINATING_ADDRESS = "originating_address";
        public static final String COLUMN_NAME_DISPLAY_ORIGINATING_ADDRESS = "display_originating_address";
        public static final String COLUMN_NAME_DISPLAY_MESSAGE_BODY = "display_message_body";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }

    public static class SmsDbHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "sms_storage.db";
        private static SmsDbHelper dbHelper = null;

        private SmsDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public static SmsDbHelper getInstance(Context context) {
            if (dbHelper == null) {
                dbHelper = new SmsDbHelper(context);
            }
            return dbHelper;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_SMS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
