package com.project.spender.controllers;

import android.content.Context;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import com.project.spender.adapters.HistoryItemAdapter;
import com.project.spender.data.CheckStatus;
import com.project.spender.data.entities.CheckWithProducts;

import java.util.ArrayList;
import java.util.List;

public class HistoryListHolder {

    private ListView listView;
    private List<CheckStatus> list;
    private TextView info;

    public HistoryListHolder() {
        list = new ArrayList<>();
    }

    public void setListView(Context context, ListView listView, TextView textView) {
        this.listView = listView;
        info = textView;
        listView.setAdapter(new HistoryItemAdapter(context, list));
        infoSet();
    }

    public void add(CheckStatus checkStatus) {
        list.add(checkStatus);
        upgrade();
    }

    public void update() {
        if (listView != null) {
            listView.invalidateViews();
        }
    }

    private void infoSet() {
        if (list.size() == 0) {
            info.setVisibility(View.VISIBLE);
        } else {
            info.setVisibility(View.INVISIBLE);
        }
    }

    public void upgrade() {
        if (listView != null) {
            infoSet();
            listView.invalidate();
        }
    }
}
