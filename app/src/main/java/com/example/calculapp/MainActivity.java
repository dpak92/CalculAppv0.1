package com.example.calculapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.calculapp12.History.addCurrent;
import static com.example.calculapp12.History.addHistory;
import static com.example.calculapp12.History.clearCurrent;
import static com.example.calculapp12.History.delLastInCurrent;
import static com.example.calculapp12.History.done;
import static com.example.calculapp12.History.getCurrent;
import static com.example.calculapp12.History.getHistory;
import static com.example.calculapp12.History.setCurrentExp;
import static com.example.calculapp12.History.setDone;
import static com.example.calculapp12.History.setHistory;

public class MainActivity extends AppCompatActivity implements KeysFragment {

    public static final String TAG_TRACE = "TAG_TRACE";
    public static final String KEY_ACTIVITY_CALC = "KEY_ACTIVITY_CALC";
    public static final String KEY_ACTIVITY_DEV = "KEY_ACTIVITY_DEV";

    com.example.calculapp12.DisplayFragment fragmentDisplay = new com.example.calculapp12.DisplayFragment();
    com.example.calculapp12.History history = new com.example.calculapp12.History();


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

        if (done == null) done = false;

        if (savedInstanceState != null) {
            com.example.calculapp12.History savedHistory = (com.example.calculapp12.History) savedInstanceState.getSerializable(KEY_ACTIVITY_CALC);
            setCurrentExp(savedHistory.getCurrent());
            setHistory (savedHistory.getHistory());
            setDone(savedHistory.getDone());
        }

        fragmentDisplay= (com.example.calculapp12.DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        fragmentDisplay.displayCurrent (getCurrent());
        fragmentDisplay.displayHistory(getHistory());

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
        fragmentDisplay= (com.example.calculapp12.DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
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
        fragmentDisplay= (com.example.calculapp12.DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        Log.d (TAG_TRACE, "onDelPressed: "+duration);
        switch (duration) {
            case com.example.calculapp12.KeysFragment.CLICK_SHORT:
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
            case com.example.calculapp12.KeysFragment.CLICK_LONG:
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
        fragmentDisplay= (com.example.calculapp12.DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        switch (duration) {
            case com.example.calculapp12.KeysFragment.CLICK_SHORT:
                try {
                    Log.d (TAG_TRACE, "onEqualsPressed: expression= "+getCurrent());
                    Double dResult = com.example.calculapp12.ExpressionsLib.evalExp(getCurrent());
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
            case com.example.calculapp12.KeysFragment.CLICK_LONG:
                try {
                    Intent intent = new Intent (this, com.example.calculapp12.ConvertActivity.class);
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
                history = (com.example.calculapp12.History) getIntent().getSerializableExtra(KEY_ACTIVITY_CALC);
                fragmentDisplay= (com.example.calculapp12.DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
                fragmentDisplay.displayCurrent (getCurrent());
                fragmentDisplay.displayHistory(getHistory());
                Log.d (TAG_TRACE, "onActivityResult: OK");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d (TAG_TRACE, "onActivityResult: CANCELED");
            }
        }
    }
}