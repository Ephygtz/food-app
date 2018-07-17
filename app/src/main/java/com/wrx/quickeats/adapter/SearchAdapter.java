package com.wrx.quickeats.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wrx.quickeats.fragments.MapFragment;
import com.wrx.quickeats.R;
import com.wrx.quickeats.activities.Description;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mobulous51 on 27/10/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> hashMapArrayList;
    String cousineName;
    EditText editText;
    RecyclerView recyclerView;
    MapFragment fragment;

    public SearchAdapter(Context context, ArrayList<HashMap<String, String>> hashMapArrayList, String cousineName, EditText editText, RecyclerView recyclerView, MapFragment fragment) {
        this.context = context;
        this.hashMapArrayList = hashMapArrayList;
        this.cousineName = cousineName;
        this.editText = editText;
        this.recyclerView = recyclerView;
        this.fragment = fragment;
    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.customsearch_list, parent, false);
        return new SearchAdapter.MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.MyViewHolder holder, final int position) {

        if (hashMapArrayList.get(position).get("cousine").equals("")) {
            holder.name.setText(hashMapArrayList.get(position).get("city"));
            holder.type.setText(hashMapArrayList.get(position).get("type"));
        } else {
            holder.name.setText(cousineName);
            holder.type.setText("cousine");
        }
        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hashMapArrayList.get(position).get("check").equals("location")) {
                    editText.setText("");
                    recyclerView.setVisibility(View.GONE);
                    fragment.callsearchDetailApi(hashMapArrayList.get(position).get("restaurant_id"),"location",hashMapArrayList.get(position).get("city"));
                    //Toast.makeText(context, "location click", Toast.LENGTH_LONG).show();
                } else if (hashMapArrayList.get(position).get("check").equals("cousine")) {
                    //Toast.makeText(context, "cousine click", Toast.LENGTH_LONG).show();
                    editText.setText("");
                    recyclerView.setVisibility(View.GONE);
                    fragment.callsearchDetailApi(hashMapArrayList.get(position).get("cousine"),"cousine","");

                } else if (hashMapArrayList.get(position).get("check").equals("restaurant")) {
                    //Toast.makeText(context, "restaurant click", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, Description.class);
                    intent.putExtra("restaurant_id", hashMapArrayList.get(position).get("restaurant_id"));
                    intent.putExtra("image", hashMapArrayList.get(position).get("image"));
                    intent.putExtra("rest_name", hashMapArrayList.get(position).get("rest_name"));
                    intent.putExtra("rest_type", hashMapArrayList.get(position).get("rest_type"));
                    intent.putExtra("rest_description", hashMapArrayList.get(position).get("rest_description"));
                    intent.putExtra("lat", hashMapArrayList.get(position).get("lat"));
                    intent.putExtra("log", hashMapArrayList.get(position).get("log"));
                    context.startActivity(intent);
                    editText.setText("");
                    recyclerView.setVisibility(View.GONE);

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return hashMapArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, type;
        private RelativeLayout root_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            type = (TextView) itemView.findViewById(R.id.type);
            root_layout = (RelativeLayout) itemView.findViewById(R.id.root_layout);

        }
    }
}
