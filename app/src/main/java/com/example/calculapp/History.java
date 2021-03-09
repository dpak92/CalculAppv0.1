package com.example.calculapp;

import java.io.Serializable;
import java.util.ArrayList;

public class History implements Serializable {
    static ArrayList<String> historyArray = new ArrayList<String>();
    static String currentExp;
    static Boolean done;

    public History() {
    }

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

    public static void addHistory (String inString) {
        historyArray.add(0, inString);
    }

    public static void setHistory (ArrayList<String> inArray) {
        historyArray = inArray;
    }

    public static ArrayList<String> getHistory () {
        return historyArray;
    }

    public static void clearHistory () {
        historyArray.clear();
    }
}
