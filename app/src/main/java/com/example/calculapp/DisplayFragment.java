package com.example.calculapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.calculapp.model.HistoryExp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DisplayFragment extends Fragment {

    public DisplayFragment() {
        // Required empty public constructor
    }

    public DisplayFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_display, container, false);

        return view;
    }

    /**
     * Displays the current expression in the current TextView
     * @param   inString    current expression as it is typed by the user
     */
    public void displayCurrent (String inString) {
        TextView tvCurrent;

        tvCurrent = getView().findViewById(R.id.tv_fragment_display_current);
        tvCurrent.setText(inString);
    }

    /**
     * Displays the expressions history (already computed expressions) in the history ListView.
     * @param   inStrings    the history of calculated expressions without timestamp.
     */
    public void displayHistory (ArrayList<String> inStrings) {
        ListView lvHistory;
        ArrayAdapter<String> aaHistory;

        if (inStrings.size() > 0) {
            lvHistory = getView().findViewById(R.id.lv_fragment_display_history);
            aaHistory = new ArrayAdapter<String>(getContext(), R.layout.listview_fragment_history, inStrings);
            lvHistory.setAdapter (aaHistory);
        }
    }
}