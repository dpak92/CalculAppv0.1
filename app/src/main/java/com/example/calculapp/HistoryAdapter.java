package com.example.calculapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.calculapp.model.HistoryExp;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import static com.example.calculapp.MainActivity.TAG_TRACE;

public class HistoryAdapter extends ArrayAdapter<HistoryExp> {
    private Context context;
    private ArrayList<HistoryExp> historyList = new ArrayList<>();

    public HistoryAdapter (@NonNull Context context,  int textViewResourceId, ArrayList<HistoryExp> list) {
        super(context, 0 , list);
        context = context;
        historyList = list;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_history, null);
        }

        HistoryExp item = historyList.get(position);

        if (item != null) {
            TextView tvList = (TextView) v.findViewById(R.id.tv_list);

            try {
                tvList.setText(item.toString());
                Log.d(TAG_TRACE, "getView : " + item.toString());
            } catch (Exception e) {
                Log.d(TAG_TRACE, "getView tvList exception!");
            }
        }
        return v;
    }
}
