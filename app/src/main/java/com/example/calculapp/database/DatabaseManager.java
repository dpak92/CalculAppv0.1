package com.example.calculapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.calculapp.model.HistoryExp;

import java.util.ArrayList;

import static com.example.calculapp.MainActivity.TAG_TRACE;
import static com.example.calculapp.database.HistoryTable.ALL_COLUMNS;
import static com.example.calculapp.database.HistoryTable.EXPRESSION;
import static com.example.calculapp.database.HistoryTable.TABLE_HISTORY;
import static com.example.calculapp.database.HistoryTable.TIMESTAMP;
import static com.example.calculapp.database.HistoryTable._ID;

public class DatabaseManager {

    private Context context;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context c)
    {
        context = c;
        dbHelper = new DatabaseHelper(context);
        Log.d (TAG_TRACE, "DatabaseManager: new.");
    }

    public DatabaseManager open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.d (TAG_TRACE, "DatabaseManager: open().");
        return this;
    }

    public void close () {
        Log.d (TAG_TRACE, "DatabaseManager: close().");
        dbHelper.close();
    }

    public void delete() {
        Log.d (TAG_TRACE, "DatabaseManager: delete TABLE_HISTORY.");
        database.execSQL(HistoryTable.DELETE_TABLE_HISTORY);
    }

    public void createHistory (HistoryExp inItem) {
        ContentValues values = HistoryTable.setValues(inItem);
        long ret = database.insert(HistoryTable.TABLE_HISTORY, null, values);
        Log.d (TAG_TRACE, "createHistory: "+inItem.toString());
        Log.d (TAG_TRACE, "createHistory: return= "+ret);
    }

    public long countHistory (){
        return DatabaseUtils.queryNumEntries(database,HistoryTable.TABLE_HISTORY);
    }

    public ArrayList <HistoryExp> readAllHistory  (ArrayList<String> strings) {
        ArrayList<HistoryExp> items = new ArrayList<>();
        Log.d (TAG_TRACE, "readAllHistory: query= "+HistoryTable.TABLE_HISTORY+ " "+HistoryTable.ALL_COLUMNS);
        try {
            Cursor cursor = database.query(HistoryTable.TABLE_HISTORY, HistoryTable.ALL_COLUMNS,
                null, null, null, null, null);

            while (cursor.moveToNext()) {
                HistoryExp item = new HistoryExp();
                item.set_id(cursor.getLong(cursor.getColumnIndex(HistoryTable._ID)));
                item.setExpression(cursor.getString(cursor.getColumnIndex(HistoryTable.EXPRESSION)));
                item.setTimestamp(cursor.getString(cursor.getColumnIndex(HistoryTable.TIMESTAMP)));
                items.add(0,item);
                strings.add (0, item.getExpression());
                Log.d (TAG_TRACE, "readAllHistory: item= "+item.toString());
            }
        } catch (Exception e) {
            Log.d (TAG_TRACE, "readAllHistory: Exception");
        }
        return items;
    }

    public int updateHistory (HistoryExp inExpression, String itemId) {
        ContentValues values = HistoryTable.setValues(inExpression);
        String[] whereClause = {itemId};
        int result = database.update (HistoryTable.TABLE_HISTORY,
                values,
                values.getAsString(HistoryTable._ID)+" = ?",
                whereClause);
        Log.d (TAG_TRACE, "updateHistory: item & result = "+inExpression.toString()+ "& "+result);
        return result;
    }

    public int deleteHistory (String itemId) {
        String[] whereClause = {itemId};
        int result = database.delete (HistoryTable.TABLE_HISTORY,
                HistoryTable._ID+" = ?",
                whereClause);
        Log.d (TAG_TRACE, "deleteHistory: result = "+result);
        return result;
    }

    public HistoryExp queryHistory (long id) {
        HistoryExp exp = new HistoryExp();
        String where = _ID + " = ?";
        String[] whereArgs = { Long.toString(id)};
        Log.d (TAG_TRACE, "queryHistory: where: " + where+"whereArgs: "+whereArgs);
        Cursor cursor = database.query(TABLE_HISTORY, ALL_COLUMNS, where, whereArgs, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                exp.set_id(cursor.getLong(cursor.getColumnIndex(_ID)));
                exp.setExpression(cursor.getString(cursor.getColumnIndex(EXPRESSION)));
                exp.setTimestamp(cursor.getString(cursor.getColumnIndex(TIMESTAMP)));
                Log.d (TAG_TRACE, "queryHistory: cursor = "+exp.toString());
            }
        } finally {
            cursor.close();
        }
        return exp;
        }
}
