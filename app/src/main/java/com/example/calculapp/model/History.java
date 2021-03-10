package com.example.calculapp.model;

import com.example.calculapp.database.DatabaseManager;

import java.io.Serializable;
import java.util.ArrayList;

public class History implements Serializable {
    public static ArrayList<HistoryExp> historyArray = new ArrayList<HistoryExp>();
    public static ArrayList<String> historyStrings = new ArrayList<String>();
    public static String currentExp;
    public static Boolean done;

    public static void setDone (Boolean inValue) {
        done = inValue;
    }

    public static Boolean getDone () { return done; }

    public static void clearCurrent () {
        currentExp = "";
    }

    public static void setCurrentExp (String inString) {
        currentExp = inString;
    }

    public static void addCurrent (String inString) {
        if (currentExp != null )
            currentExp = currentExp+inString;
        else
            currentExp = inString;
    }

    public static void delLastInCurrent () {
        if (currentExp.length() > 0) {
            currentExp = currentExp.substring(0, currentExp.length()-1);
        }
    }

    public static String getCurrent(){
        return currentExp;
    }

    public static void addHistory (String inExpression) {
        HistoryExp exp = new HistoryExp();
        exp.setTimestamp("NO TIMESTAMP");
        exp.setExpression(inExpression);
        historyArray.add(0, exp);
    }

    public static void setHistory (ArrayList<HistoryExp> inHistory) {
        historyArray = inHistory;
    }

    public static ArrayList<HistoryExp> getHistory () {
        return historyArray;
    }

    public static void clearHistory () {
        historyArray.clear();
    }
}

