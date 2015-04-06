package ua.com.igorka.oa.android.smstoemail.db.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ua.com.igorka.oa.android.smstoemail.db.SmsStorageDbContract;
import ua.com.igorka.oa.android.smstoemail.db.dao.DAO;
import ua.com.igorka.oa.android.smstoemail.db.entity.Sms;

/**
 * Created by Igor Kuzmenko on 06.04.2015.
 *
 */
public class SmsDAOImpl implements DAO<Integer, Sms> {

    private static final String TABLE_NAME = SmsStorageDbContract.SmsTable.TABLE_NAME;

    private SmsStorageDbContract.SmsDbHelper mDbHelper;
    private SQLiteDatabase db;

    public SmsDAOImpl(Context context) {
        mDbHelper = SmsStorageDbContract.SmsDbHelper.getInstance(context);
    }

    @Override
    public void insert(Sms entity) {
        db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SmsStorageDbContract.SmsTable.COLUMN_NAME_ORIGINATING_ADDRESS, entity.getOriginatingAddress());
        contentValues.put(SmsStorageDbContract.SmsTable.COLUMN_NAME_DISPLAY_ORIGINATING_ADDRESS, entity.getDisplayOriginatingAddress());
        contentValues.put(SmsStorageDbContract.SmsTable.COLUMN_NAME_DISPLAY_MESSAGE_BODY, entity.getDisplayMessageBody());
        contentValues.put(SmsStorageDbContract.SmsTable.COLUMN_NAME_TIMESTAMP, entity.getTimestamp());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    @Override
    public Sms select(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Sms> selectAll() {
        db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        List<Sms> result = null;

        if (cursor != null && cursor.moveToFirst()) {
            result = new ArrayList<>();
            do {
                Sms sms = new Sms();
                sms.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SmsStorageDbContract.SmsTable._ID)));
                sms.setOriginatingAddress(cursor.getString(cursor.getColumnIndexOrThrow(SmsStorageDbContract.SmsTable.COLUMN_NAME_ORIGINATING_ADDRESS)));
                sms.setDisplayOriginatingAddress(cursor.getString(cursor.getColumnIndexOrThrow(SmsStorageDbContract.SmsTable.COLUMN_NAME_DISPLAY_ORIGINATING_ADDRESS)));
                sms.setDisplayMessageBody(cursor.getString(cursor.getColumnIndexOrThrow(SmsStorageDbContract.SmsTable.COLUMN_NAME_DISPLAY_MESSAGE_BODY)));
                sms.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SmsStorageDbContract.SmsTable.COLUMN_NAME_TIMESTAMP)));
                result.add(sms);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return result;
    }

    @Override
    public void delete(Integer id) {
        db = mDbHelper.getWritableDatabase();
        db.delete(TABLE_NAME,
                SmsStorageDbContract.SmsTable._ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void deleteAll() {
        db = mDbHelper.getWritableDatabase();
        db.delete(TABLE_NAME,
                null,
                null);
        db.close();
    }

    @Override
    public void update(Sms entity) {
        throw new UnsupportedOperationException();
    }

}
