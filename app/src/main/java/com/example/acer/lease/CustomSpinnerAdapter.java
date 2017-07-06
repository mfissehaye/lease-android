package com.example.acer.lease;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by merhawifissehaye@gmail.com on 7/5/2017.
 */

class CustomSpinnerAdapter extends ArrayAdapter {
    private final Context context;
    private final List<User> users;

    public CustomSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<User> users) {
        super(context, resource, users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    public TextView getView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        v.setText(users.get(position).name);
        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        v.setText(users.get(position).name);
        return v;
    }
}
