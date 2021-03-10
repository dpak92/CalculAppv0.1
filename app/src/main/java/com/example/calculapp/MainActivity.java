package com.example.calculapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.example.calculapp.database.DatabaseHelper;
import com.example.calculapp.database.DatabaseManager;
import com.example.calculapp.model.HistoryExp;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.calculapp.History.addCurrent;
import static com.example.calculapp.History.addHistory;
import static com.example.calculapp.History.clearCurrent;
import static com.example.calculapp.History.delLastInCurrent;
import static com.example.calculapp.History.done;
import static com.example.calculapp.History.getCurrent;
import static com.example.calculapp.History.getHistory;
import static com.example.calculapp.History.setCurrentExp;
import static com.example.calculapp.History.setDone;
import static com.example.calculapp.History.setHistory;

public class MainActivity extends AppCompatActivity implements KeysFragment.Keys {

    public static final String TAG_TRACE = "TAG_TRACE";
    public static final String KEY_ACTIVITY_CALC = "KEY_ACTIVITY_CALC";
    public static final String KEY_ACTIVITY_DEV = "KEY_ACTIVITY_DEV";

    DisplayFragment fragmentDisplay = new DisplayFragment();

    private DatabaseManager dbManager;
    SQLiteDatabase database;

    public static ArrayList<HistoryExp> historyArray = new ArrayList<HistoryExp>();
    public static String currentExp;
    public static Boolean done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.title_main));
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF1E272E")));
        //for image
        // bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.settings_icon));

        setContentView(R.layout.activity_main);

        initHistory();

        if (done == null) done = false;

        if (savedInstanceState != null) {
            ArrayList<HistoryExp> savedHistory = (ArrayList<HistoryExp>) savedInstanceState.getSerializable(KEY_ACTIVITY_CALC);
            setCurrentExp(getCurrent());
            setHistory (getHistory());
            setDone(savedHistory.getDone());
        }

        fragmentDisplay= (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        fragmentDisplay.displayCurrent (getCurrent());
        fragmentDisplay.displayHistory(getHistory());

    }

    private void initHistory (){
        dbManager = new DatabaseManager(this);
        dbManager.open();
        ArrayList <HistoryExp> historyExp = new ArrayList<HistoryExp>();
        String historyExpression = "";
        historyExp = (ArrayList<HistoryExp>) dbManager.readAllHistory();
        setHistory (historyExp);

        dbManager.close();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_ACTIVITY_CALC, history);
    }

    /**
     * Implements the Interface of KeysFragment.
     * * @param charTyped     the character that has been typed in the Keys Fragment.
     */
    @Override
    public void onKeyPressed(String charTyped) {
        fragmentDisplay= (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        if (done) {
            clearCurrent();
            fragmentDisplay.displayCurrent (getCurrent());
        }
        addCurrent(charTyped);
        done = false;
        fragmentDisplay.displayCurrent (getCurrent());
    }

    @Override
    public void onDelPressed (int duration) {
        fragmentDisplay= (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        Log.d (TAG_TRACE, "onDelPressed: "+duration);
        switch (duration) {
            case KeysFragment.CLICK_SHORT:
                try {
                    if (done) {
                        clearCurrent();
                        fragmentDisplay.displayCurrent(getCurrent());
                    }
                    delLastInCurrent();
                    fragmentDisplay.displayCurrent(getCurrent());
                } catch (Exception e) {
                    Log.d(TAG_TRACE, "onDelPressed: hort failed");
                }
                break;
            case KeysFragment.CLICK_LONG:
                clearCurrent();
                fragmentDisplay.displayCurrent(getCurrent());
            default:
        }
    }

    /**
     * Implements the Interface of KeysFragment.
     * * @param duration     indicates a short (=CONST_SHORT) or long press (=CONST_LONG)..
     */
    @Override
    public void onEqualsPressed(int duration) {
        fragmentDisplay= (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        switch (duration) {
            case KeysFragment.CLICK_SHORT:
                try {
                    Log.d (TAG_TRACE, "onEqualsPressed: expression= "+getCurrent());
                    Double dResult = ExpressionsLib.evalExp(getCurrent());
                    int iResult = dResult.intValue();
                    addCurrent("=");
                    String sResult = Integer.toString(iResult);
                    addCurrent(sResult);
                    fragmentDisplay.displayCurrent (getCurrent());
                    addHistory(getCurrent());
                    fragmentDisplay.displayHistory(getHistory());
                    done = true;
                } catch (Exception e) {
                    Log.d (TAG_TRACE, "onEqualsPressed:  evalExp failed");
                }
                break;
            case KeysFragment.CLICK_LONG:
                try {
                    Intent intent = new Intent (this, ConvertActivity.class);
                    intent.putExtra (KEY_ACTIVITY_DEV, history);
                    startActivityForResult (intent, 1000);
                } catch (Exception e) {
                    Log.d (TAG_TRACE, "onEqualsPressed LNG:  start activity failed");
                };
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK){
                history = (History) getIntent().getSerializableExtra(KEY_ACTIVITY_CALC);
                fragmentDisplay= (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
                fragmentDisplay.displayCurrent (getCurrent());
                fragmentDisplay.displayHistory(getHistory());
                Log.d (TAG_TRACE, "onActivityResult: OK");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d (TAG_TRACE, "onActivityResult: CANCELED");
            }
        }
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