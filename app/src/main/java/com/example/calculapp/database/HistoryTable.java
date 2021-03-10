package com.example.calculapp.database;

import java.text.SimpleDateFormat;

public class HistoryTable {

    // Table Name
    public static final String TABLE_HISTORY = "TABLE_HISTORY";

    // Table columns
    public static final String _ID = "_id";
    public static final String EXPRESSION = "expression";
    public static final String TIMESTAMP  = "timestamp";
    public static final String TIMESTAMP_FORMAT = "DD-MM-YYYY-hh-mm-ss";

    // Creating table create query
    public static final String CREATE_TABLE_HISTORY = "create table " + TABLE_HISTORY +
            "(" +_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
            "," + EXPRESSION + " TEXT"+
            "," + TIMESTAMP + " TEXT"+
            ")";

    String _id;
    String expression;
    SimpleDateFormat timestamp;

    public HistoryTable(String _id, String expression, SimpleDateFormat timestamp) {
        this._id = _id;
        this.expression = expression;
        this.timestamp = timestamp;
    }

    public static String getTimestampFormat() {
        return TIMESTAMP_FORMAT;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public SimpleDateFormat getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(SimpleDateFormat timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "HistoryTable{" +
                "_id='" + _id + '\'' +
                ", expression='" + expression + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
