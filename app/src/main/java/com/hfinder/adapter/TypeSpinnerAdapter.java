package com.hfinder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.hfinder.entities.TypeLogement;

import java.util.List;

/**
 * Created by root on 26/06/18.
 */

public class TypeSpinnerAdapter extends ArrayAdapter<TypeLogement>{
    private List<TypeLogement> list;

    public List<TypeLogement> getList() {
        return list;
    }

    public void setList(List<TypeLogement> list) {
        this.list = list;
    }

    public TypeSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<TypeLogement> objects) {
        super(context, resource, objects);
        list = objects;
    }

    @Override
    public int getCount() {
        return list.size();

    }

    @Nullable
    @Override
    public TypeLogement getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }
}
