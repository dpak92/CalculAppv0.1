package com.example.calculapp;

import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.calculapp.database.DatabaseManager;
import com.example.calculapp.model.History;

import static com.example.calculapp.MainActivity.TAG_TRACE;
import static com.example.calculapp.model.History.historyArray;
import static com.example.calculapp.model.History.historyStrings;
import static com.example.calculapp.model.History.setDone;
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
        setDone(false);
        dbManager.close();
    }

    @Override
    public void onModify(int position) {

    }

    @Override
    public void onDelete(int position) {

    }
}