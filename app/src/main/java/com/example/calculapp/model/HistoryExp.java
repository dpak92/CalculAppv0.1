package com.example.calculapp.model;

import java.io.Serializable;

public class HistoryExp implements Serializable {

    public static final String TIMESTAMP_FORMAT = "DD-MM-YYYY-hh-mm-ss";

    long _id;
    String expression;
    String timestamp;

    public HistoryExp () {
    }

    public static String getTimestampFormat() {
        return TIMESTAMP_FORMAT;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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
