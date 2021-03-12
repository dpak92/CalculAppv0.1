package com.example.calculapp;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.calculapp.database.DatabaseManager;
import com.example.calculapp.model.History;

import static com.example.calculapp.MainActivity.KEY_ACTION;
import static com.example.calculapp.MainActivity.KEY_DELETE;
import static com.example.calculapp.MainActivity.KEY_ID;
import static com.example.calculapp.MainActivity.KEY_MODIFY;
import static com.example.calculapp.MainActivity.RESULT_NOK;
import static com.example.calculapp.MainActivity.TAG_TRACE;
import static com.example.calculapp.model.History.historyArray;
import static com.example.calculapp.model.History.historyStrings;
import static com.example.calculapp.model.History.setHistory;

public class HistoryActivity extends ListActivity implements HistoryAdapter.HistoryInterface {

    History history = new History();

    DatabaseManager dbManager;
    SQLiteDatabase database;

    HistoryAdapter hAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d (TAG_TRACE, "HistoryActivity onCreate.");

        initHistory();
        Log.d (TAG_TRACE, "HistoryActivity initHistory done.");

        hAdapter = new HistoryAdapter (this, R.layout.list_history,
                historyArray);
        Log.d (TAG_TRACE, "HistoryActivity Inflate View done.");
        setListAdapter(hAdapter);

    }

    private void initHistory() {
        dbManager = new DatabaseManager(this);
        dbManager.open();
        setHistory(dbManager.readAllHistory(historyStrings));
    }

    @Override
    public void onModify(long id) {
        Log.d (TAG_TRACE, "onModify id= "+id);
        Intent intent = new Intent();
        intent.putExtra(KEY_ACTION, KEY_MODIFY);
        intent.putExtra(KEY_ID, id);
        setResult (RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDelete(long id) {
        Log.d (TAG_TRACE, "onDelete id= "+id);
        dbManager.deleteHistory(Long.toString(id));
        Intent intent = new Intent();
        intent.putExtra(KEY_ACTION, KEY_DELETE);
        intent.putExtra(KEY_ID, id);
        setResult (RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d (TAG_TRACE, "ActivityHistory: onStop");
        Intent intent = new Intent();
        setResult (RESULT_NOK, intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbManager.close();;
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbManager.open();
    }

}