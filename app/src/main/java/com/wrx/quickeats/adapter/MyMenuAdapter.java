package com.wrx.quickeats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wrx.quickeats.R;
import com.wrx.quickeats.util.BeanClass;

import java.util.ArrayList;

/**
 * Created by mobulous51 on 6/9/17.
 */

public class MyMenuAdapter extends RecyclerView.Adapter<MyMenuAdapter.MyViewHolder> {


    ArrayList<BeanClass> menu = new ArrayList<>();
    Context context;

    public MyMenuAdapter(Context context, ArrayList<BeanClass> menu) {

        this.context = context;
        this.menu = menu;
    }

    @Override
    public MyMenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_menu, parent, false);
        return new MyMenuAdapter.MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(MyMenuAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
