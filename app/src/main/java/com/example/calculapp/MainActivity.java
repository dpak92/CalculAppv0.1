package com.example.calculapp;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.example.calculapp.database.DatabaseManager;
import com.example.calculapp.model.History;
import com.example.calculapp.model.HistoryExp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.calculapp.model.History.addCurrent;
import static com.example.calculapp.model.History.addHistory;
import static com.example.calculapp.model.History.clearCurrent;
import static com.example.calculapp.model.History.clearHistory;
import static com.example.calculapp.model.History.clearStrings;
import static com.example.calculapp.model.History.delLastInCurrent;
import static com.example.calculapp.model.History.getCurrent;
import static com.example.calculapp.model.History.getDone;
import static com.example.calculapp.model.History.getHistory;
import static com.example.calculapp.model.History.historyStrings;
import static com.example.calculapp.model.History.setDone;
import static com.example.calculapp.model.History.setHistory;

public class MainActivity extends AppCompatActivity implements KeysFragment.Keys {

    public static final String TAG_TRACE = "TAG_TRACE";
    public static final String KEY_ACTIVITY_CALC = "KEY_ACTIVITY_CALC";
    public static final String KEY_ACTIVITY_DEV = "KEY_ACTIVITY_DEV";
    private static final String KEY_ACTIVITY_HIS = "KEY_ACTIVITY_HIS";
    public static final String KEY_ACTION = "KEY_ACTION";
    public static final String KEY_MODIFY = "KEY_MODIFY";
    public static final String KEY_DELETE = "KEY_DELETE";
    public static final String KEY_POSITION = "KEY_POSITION";
    public static final String KEY_ID = "KEY_ID";
    public static final int RESULT_OK = 1;
    public static final int RESULT_NOK = 0;


    DisplayFragment fragmentDisplay = new DisplayFragment();

    History history = new History();

    DatabaseManager dbManager;
    SQLiteDatabase database;

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

        if (getDone() == null) setDone (false);

        if (savedInstanceState != null) {
            history  = (History) savedInstanceState.getSerializable(KEY_ACTIVITY_CALC);
            setHistory(history.historyArray);
            Log.d (TAG_TRACE, "savedInstanceState: done= "+history.done);
            Log.d (TAG_TRACE, "savedInstanceState: currentExp= "+history.currentExp);
        }

        fragmentDisplay= (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        fragmentDisplay.displayCurrent (getCurrent());
        fragmentDisplay.displayHistory (getHistory());

    }

    void initHistory (){
        dbManager = new DatabaseManager(this);
        dbManager.open();
        setHistory(dbManager.readAllHistory(historyStrings));
        setDone(false);
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
        if (getDone()) {
            clearCurrent();
            fragmentDisplay.displayCurrent (getCurrent());
        }
        addCurrent(charTyped);
        setDone(false);
        fragmentDisplay.displayCurrent (getCurrent());
    }

    @Override
    public void onDelPressed (int duration) {
        fragmentDisplay= (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        Log.d (TAG_TRACE, "onDelPressed: "+duration);
        switch (duration) {
            case KeysFragment.CLICK_SHORT:
                try {
                    if (getDone()) {
                        clearCurrent();
                        fragmentDisplay.displayCurrent(getCurrent());
                    }
                    delLastInCurrent();
                    fragmentDisplay.displayCurrent(getCurrent());
                } catch (Exception e) {
                    Log.d(TAG_TRACE, "onDelPressed: short failed");
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
                    HistoryExp exp = addHistory(getCurrent());
                    dbManager.createHistory(exp);
                    fragmentDisplay.displayHistory(getHistory());
                    setDone(true);
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
    public void onACPressed(int duration) {
     if (duration == KeysFragment.CLICK_LONG){
         dbManager.delete();
         clearHistory ();
         clearCurrent ();
         clearStrings ();
         fragmentDisplay.displayCurrent (getCurrent());
         fragmentDisplay.displayHistory (getHistory());
     }
    }

    @Override
    public void onHisPressed(int duration) {
        Intent intent = new Intent (this, HistoryActivity.class);
        startActivityForResult(intent, 2000);
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
                Log.d (TAG_TRACE, "onActivityResult: OK "+requestCode);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d (TAG_TRACE, "onActivityResult: CANCELED "+requestCode);
            }
        } else if (requestCode == 2000) {
            String action = data.getStringExtra(KEY_ACTION);
            long id = data.getLongExtra(KEY_ID, -1);
            Log.d (TAG_TRACE, "onActivityResult: OK "+
                    requestCode+
                    " returned: action= "+
                    action+
                    " id= "+id);
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