package com.example.calculapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.calculapp.database.DatabaseManager;
import com.example.calculapp.model.History;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.calculapp.model.History.historyArray;
import static com.example.calculapp.model.History.historyStrings;
import static com.example.calculapp.model.History.setDone;
import static com.example.calculapp.model.History.setHistory;

public class HistoryActivity extends AppCompatActivity {

    History history = new History();

    DatabaseManager dbManager;
    SQLiteDatabase database;

    HistoryAdapter hAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initHistory();

        hAdapter = new HistoryAdapter (this, R.layout.list_history,
                historyArray);

    }

    private void initHistory() {
        dbManager = new DatabaseManager(this);
        dbManager.open();
        setHistory(dbManager.readAllHistory(historyStrings));
        setDone(false);
        dbManager.close();
    }
}