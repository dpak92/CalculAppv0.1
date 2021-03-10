package com.example.calculapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.example.calculapp.MainActivity.TAG_TRACE;

public class KeysFragment extends Fragment {

    public static final int CLICK_SHORT = 0;
    public static final int CLICK_LONG = 1;

    Activity activity; // activity used by the Interface
    KeysFragment.Keys keysInterface; // instance of the interface

    ArrayList<KeysClass> keysArray = new ArrayList<KeysClass>();
    ArrayList<View> layoutsArray = new ArrayList<View>();
    ArrayList<View> viewsArray = new ArrayList<View>();

    interface Keys {
        void onKeyPressed (String charTyped);
        void onDelPressed (int duration);
        void onEqualsPressed (int duration);
        void onACPressed (int duration);
    }

    /**
     * Empty constructor required.
     */
    public KeysFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keys, container, false);


        initKeysArrays (view);

        return view;
    }

    /**
     * Initialize the Keys Array by reading the buttons in the XML layout:
     * Structure of XML layout:
     * 4 times:
     *      Grid Layout
     *
     * @param inView    View inflated in Keys fragment.
     */

    private void initKeysArrays (View inView){
        final String CONTENT_DESC_KEYS = getResources().getString(R.string.keys);
        Log.d(TAG_TRACE, "initKeysArrays BaseLayout =" + inView.getContentDescription().toString());
        try {
            View gl_keys_background = inView.findViewById (R.id.cl_key_back);
            Log.d(TAG_TRACE, "initKeysArrays GridLayoutId =" + gl_keys_background.getId());
            viewsArray.clear();
            gl_keys_background.findViewsWithText(viewsArray, CONTENT_DESC_KEYS, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
            for (View outView : viewsArray) {
                Button outButton = (Button) outView;
                setOnClickListeners (outButton);
                outButton.setOnLongClickListener(longClickListener);
                String keyText = outButton.getText().toString();
                KeysClass newKey = new KeysClass(keyText, outButton);
                keysArray.add (newKey);
                Log.d(TAG_TRACE, "initKeysArrays button text =" + keyText+" "+keysArray.size());
            }

        } catch (Exception e){
            Log.d(TAG_TRACE, "initKeysArrays Exception");
        }
    }

    public void setOnClickListeners (Button inButton) {
        inButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Button button = (Button) v;
                String buttonText = button.getText().toString();
                Log.d(TAG_TRACE, "onClick Key pressed: " + buttonText);
                try {
                    switch (buttonText) {
                        case "0":
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "9":
                        case "+":
                        case "-":
                            keysInterface.onKeyPressed(buttonText);
                            break;
                        case ",":
                            keysInterface.onKeyPressed(".");
                            break;
                        case "÷":
                            keysInterface.onKeyPressed("/");
                            break;
                        case "x":
                            keysInterface.onKeyPressed("*");
                            break;
                        case "DEL":
                            keysInterface.onDelPressed(CLICK_SHORT);
                            break;
                        case "AC":
                            keysInterface.onDelPressed(CLICK_LONG);
                            break;
                        case "=":
                            keysInterface.onEqualsPressed(CLICK_SHORT);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    Log.d(TAG_TRACE, "onClick Keys interface may no be implemented");
                }
            }
        });
    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        public boolean onLongClick (View v) {
            Button clickedButton = (Button) v;
            String buttonText = clickedButton.getText().toString();
            Log.d (TAG_TRACE, "button long pressed --> " + buttonText);
            if (clickedButton.getId() == R.id.button_equal) {
                keysInterface.onEqualsPressed(CLICK_LONG);
            } else if (clickedButton.getId() == R.id.button_del) {
                keysInterface.onDelPressed(CLICK_LONG);
            } else if (clickedButton.getId() == R.id.button_AC) {
                keysInterface.onACPressed(CLICK_LONG);
            }
            return true;
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        if (activity instanceof Keys) {
            keysInterface = (Keys) activity;
        }
    }
}