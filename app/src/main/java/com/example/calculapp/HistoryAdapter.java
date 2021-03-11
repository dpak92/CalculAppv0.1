package com.example.calculapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calculapp.model.HistoryExp;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import static com.example.calculapp.MainActivity.TAG_TRACE;

public class HistoryAdapter extends ArrayAdapter<HistoryExp> {
    private Context context;
    private ArrayList<HistoryExp> historyList = new ArrayList<>();
    View view;
    LayoutInflater inflater;
    Activity activity; // activity used by the Interface
    HistoryAdapter.HistoryInterface historyInterface; // instance of the interface

    interface HistoryInterface {
        void onModify (long id);
        void onDelete (long id);

    }

    public HistoryAdapter (@NonNull Context context,  int textViewResourceId, ArrayList<HistoryExp> list) {
        super(context, 0 , list);
        context = context;
        historyList = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activity = (Activity) context;
        if (activity instanceof HistoryInterface) {
            historyInterface = (HistoryInterface) activity;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent){

        view = inflater.inflate(R.layout.list_history, null);
        HistoryExp item = historyList.get(position);

        if (item != null) {
            TextView tvList = (TextView) view.findViewById(R.id.tv_list);
            ImageView ivModify = (ImageView) view.findViewById(R.id.iv_list_modify);
            ivModify.setTag(item.get_id());
            ivModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG_TRACE, "onClick ImageView Modify= "+v.getTag());
                    historyInterface.onModify ((Long) v.getTag());
                }
            });
            ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_list_delete);
            ivDelete.setTag(item.get_id());
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG_TRACE, "onClick ImageView Delete= "+v.getTag());

                    historyInterface.onDelete ((Long) v.getTag());
                }
            });

            try {
                Log.d(TAG_TRACE, "getView setText= "+item.getExpression().toString());
                tvList.setText(item.getExpression().toString());
            } catch (Exception e) {
                Log.d(TAG_TRACE, "getView tvList exception!");
            }


        }
        return view;
    }
}
