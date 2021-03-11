package com.example.calculapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calculapp.model.HistoryExp;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import static com.example.calculapp.MainActivity.TAG_TRACE;

public class HistoryAdapter extends ArrayAdapter<HistoryExp> {
    private Context context;
    private ArrayList<HistoryExp> historyList = new ArrayList<>();
    View view;
    LayoutInflater inflater;

    public HistoryAdapter (@NonNull Context context,  int textViewResourceId, ArrayList<HistoryExp> list) {
        super(context, 0 , list);
        context = context;
        historyList = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        view = inflater.inflate(R.layout.list_history, null);
        HistoryExp item = historyList.get(position);

        if (item != null) {
            TextView tvList = (TextView) view.findViewById(R.id.tv_list);
            ImageView ivModify = (ImageView) view.findViewById(R.id.iv_list_modify);
            ivModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "You clicked on ImageView", Toast.LENGTH_LONG).show();
                }
            });
            ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_list_delete);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "You clicked on ImageView", Toast.LENGTH_LONG).show();
                }
            });

            try {
                tvList.setText(item.getExpression().toString());
                Log.d(TAG_TRACE, "getView : " + item.toString());
            } catch (Exception e) {
                Log.d(TAG_TRACE, "getView tvList exception!");
            }


        }
        return view;
    }
}
