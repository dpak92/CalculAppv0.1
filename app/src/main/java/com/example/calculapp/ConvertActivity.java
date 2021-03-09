package com.example.calculapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.calculapp.History.getCurrent;
import static com.example.calculapp.History.getHistory;
import static com.example.calculapp.History.setCurrentExp;
import static com.example.calculapp.History.setDone;
import static com.example.calculapp.History.setHistory;

public class ConvertActivity extends AppCompatActivity implements KeysFragment.Keys{

    private static final String TAG_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private static final String[] COUNTRIES = {"USD", "JPY", "GBP", "SEK" };
    private static final Double[] RATES = {1.1938, 129.38, 0.8630, 10.1863};
    ArrayList<String> spStrings = new ArrayList<String>();
    private static int countrySel = 0;

    DisplayFragment fragmentDisplay = new DisplayFragment();
    History history = new History();

    Spinner spCurrency;
    TextView tvTargetCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.title_converter));
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF1E272E")));
        //for image
        // bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.settings_icon));

        setContentView(R.layout.activity_convert);

        if (savedInstanceState != null) {
            History savedHistory = (History) savedInstanceState.getSerializable(MainActivity.KEY_ACTIVITY_DEV);
            setCurrentExp(getCurrent());
            setHistory (getHistory());
            setDone(savedHistory.getDone());
        }

        history = (History) getIntent().getSerializableExtra(MainActivity.KEY_ACTIVITY_DEV);
        fragmentDisplay= (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        fragmentDisplay.displayCurrent (getCurrent());
        fragmentDisplay.displayHistory(getHistory());

        spCurrency = findViewById(R.id.);
        spCurrency.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                String str = spCurrency.getItemAtPosition(arg2).toString();
                tvTargetCurrency.setText(str);
                countrySel = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spStrings.clear();
        for (int i=0; i<COUNTRIES.length; i++){
            spStrings.add(COUNTRIES[i]+" "+ RATES[i]);
            Log.d (MainActivity.TAG_TRACE, "onCreate Convert: "+COUNTRIES[i]+" "+ RATES[i]);
        }
        ArrayAdapter<String> aaCurrency = new ArrayAdapter<String>(this, R.layout.spinner_currency, spStrings);
        spCurrency.setAdapter(aaCurrency);
        tvTargetCurrency= findViewById(R.id.tv_convert_target_currency);
        String str = spCurrency.getItemAtPosition(countrySel).toString();
        tvTargetCurrency.setText(str);

    }

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
    public void onDelPressed(int duration) {
        fragmentDisplay= (com.example.calculapp12.DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        switch (duration) {
            case com.example.calculapp12.KeysFragment.CLICK_SHORT:
                if (done) {
                    clearCurrent();
                    fragmentDisplay.displayCurrent (getCurrent());
                }
                delLastInCurrent();
                fragmentDisplay.displayCurrent (getCurrent());
                break;
            case com.example.calculapp12.KeysFragment.CLICK_LONG:
                clearCurrent();
                fragmentDisplay.displayCurrent(getCurrent());
            default:
        }
    }

    @Override
    public void onEqualsPressed(int duration) {
        fragmentDisplay= (com.example.calculapp12.DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_display);
        switch (duration) {
            case com.example.calculapp12.KeysFragment.CLICK_SHORT:
                try {
                    Log.d (MainActivity.TAG_TRACE, "onEqualsPressed: expression= "+getCurrent());
                    Double dResult = com.example.calculapp12.ExpressionsLib.evalExp(getCurrent());
                    Double dResult2 = dResult * RATES[countrySel];
                    int iResult = dResult.intValue();
                    int iResult2 = dResult2.intValue();
                    addCurrent("=");
                    String sResult = Integer.toString(iResult);
                    addCurrent(sResult);
                    addCurrent(" EUR > ");
                    String sResult2 = Integer.toString(iResult2);
                    addCurrent(sResult2);
                    addCurrent(" "+COUNTRIES[countrySel]);
                    fragmentDisplay.displayCurrent (getCurrent());
                    addHistory(getCurrent());
                    fragmentDisplay.displayHistory(getHistory());
                    done = true;
                } catch (Exception e) {
                    Log.d (MainActivity.TAG_TRACE, "onEqualsPressed:  evalExp failed");
                };
                break;
            case com.example.calculapp12.KeysFragment.CLICK_LONG:
                try {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(MainActivity.KEY_ACTIVITY_CALC, history);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                } catch (Exception e) {
                    Log.d (MainActivity.TAG_TRACE, "onEqualsPressed LNG:  start activity failed");
                };
            default:
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MainActivity.KEY_ACTIVITY_DEV, history);
    }
}