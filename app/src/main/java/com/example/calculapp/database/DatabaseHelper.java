package com.example.calculapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_HISTORY = "TABLE_HISTORY";

    // Table columns
    public static final String _ID = "_id";
    public static final String EXPRESSION = "expression";

    // Database Information
    static final String DB_NAME = "CalculApp.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE_HISTORY = "create table " + TABLE_HISTORY + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EXPRESSION + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }
}
