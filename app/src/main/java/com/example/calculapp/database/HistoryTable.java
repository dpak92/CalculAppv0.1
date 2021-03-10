package com.example.calculapp.database;

import android.content.ContentValues;

import com.example.calculapp.model.HistoryExp;

public class HistoryTable {

    // Table Name
    public static final String TABLE_HISTORY = "TABLE_HISTORY";

    // Table columns
    public static final String _ID = "_id";
    public static final String EXPRESSION = "expression";
    public static final String TIMESTAMP  = "timestamp";
    public static final String[] ALL_COLUMNS = {_ID, EXPRESSION, TIMESTAMP};

    // Creating table create query
    public static final String CREATE_TABLE_HISTORY = "create table " + TABLE_HISTORY +
            "(" +_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
            "," + EXPRESSION + " TEXT"+
            "," + TIMESTAMP + " TEXT"+
            ")";

    public static ContentValues setValues (HistoryExp inValue) {;
        ContentValues values = new ContentValues();
        values.put(EXPRESSION, inValue.getExpression());
        values.put(TIMESTAMP, inValue.getTimestamp().toString());
        return  values;
    }
}
