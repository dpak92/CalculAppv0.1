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

    public void close () {
        dbHelper.close();
    }

    public void createHistory (HistoryExp inItem) {
        ContentValues values = HistoryTable.setValues(inItem);
        database.insert(HistoryTable.TABLE_HISTORY, null, values);
        Log.d (TAG_TRACE, "createHistory: "+inItem.toString());
    }

    public long countHistory (){
        return DatabaseUtils.queryNumEntries(database,HistoryTable.TABLE_HISTORY);
    }

    public ArrayList <HistoryExp> readAllHistory  () {
        ArrayList<HistoryExp> items = new ArrayList<>();
        Cursor cursor = database.query(HistoryTable.TABLE_HISTORY, HistoryTable.ALL_COLUMNS,
                null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                HistoryExp item = new HistoryExp();
                item.set_id(cursor.getString(cursor.getColumnIndex(HistoryTable._ID)));
                item.set_id(cursor.getString(cursor.getColumnIndex(HistoryTable.EXPRESSION)));
                item.set_id(cursor.getString(cursor.getColumnIndex(HistoryTable.TIMESTAMP)));
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

}
