package com.example.mentorlink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomCardAdapter extends ArrayAdapter<String> {

    public CustomCardAdapter(@NonNull Context context, int resource, @NonNull List<String> dataList) {
        super(context, resource, dataList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }

        // Get the current data item if available
        String currentItem = getItem(position);

        // Get references to views inside card_item.xml
        TextView textView = convertView.findViewById(R.id.textView);

        // Set data to views if the current item is not null
        if (currentItem != null) {
            textView.setText(currentItem);
        }

        return convertView;
    }
}

