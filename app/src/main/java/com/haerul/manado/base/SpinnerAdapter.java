package com.haerul.manado.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.List;

public class SpinnerAdapter<S> extends ArrayAdapter {

    public SpinnerAdapter(@NonNull Context context, @LayoutRes int resource, List spinnerArray) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        return super.getCount() - 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = super.getView(position, convertView, parent);
        if (position == getCount()) {
            ((TextView) convertView.findViewById(android.R.id.text1)).setText("");
            ((TextView) convertView.findViewById(android.R.id.text1)).setHint((CharSequence) getItem(getCount())); //"Hint to be displayed"
        }

        return convertView;
    }
}