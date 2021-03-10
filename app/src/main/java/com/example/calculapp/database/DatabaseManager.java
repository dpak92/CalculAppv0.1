package com.example.calculapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

    private Context context;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context c) {
        context = c;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert() {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.EXPRESSION, desc);
        database.insert(DatabaseHelper.TABLE_HISTORY, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.EXPRESSION };
        Cursor cursor = database.query(DatabaseHelper.TABLE_HISTORY, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String exp) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.EXPRESSION, exp);
        int i = database.update(DatabaseHelper.TABLE_HISTORY, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_HISTORY, DatabaseHelper._ID + "=" + _id, null);
    }

}
