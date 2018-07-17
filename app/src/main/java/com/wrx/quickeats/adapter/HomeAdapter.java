package com.wrx.quickeats.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.wrx.quickeats.bean.HomeResponse;
import com.wrx.quickeats.fragments.Home;
import com.wrx.quickeats.R;

import java.util.ArrayList;

import com.wrx.quickeats.activities.Description;
//
///**
// * Created by mobua01 on 10/8/17.
// */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHomeViewHolder> {

    private ArrayList<HomeResponse> homeResponseArrayList;
    Context context;
    ArrayList<String> arrayList = new ArrayList<>();
    Home fragment;

    public HomeAdapter(Context context, ArrayList<HomeResponse> homeResponseArrayList, Home fragment) {
        this.context = context;
        this.homeResponseArrayList = homeResponseArrayList;
        this.fragment = fragment;
    }

    @Override
    public MyViewHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_layout, parent, false);
        return new MyViewHomeViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final MyViewHomeViewHolder holder, final int position) {

        final HomeResponse homeResponse = homeResponseArrayList.get(position);
        holder.tv_restro_name.setText(homeResponseArrayList.get(position).getRest_name());
        holder.tv_dishes.setText(homeResponseArrayList.get(position).getRest_type());
        AQuery aQuery = new AQuery(context);
        aQuery.id(holder.iv_restro_image).image(homeResponseArrayList.get(position).getImage_url(), false, false, 0, R.drawable.image);

        if (homeResponse.getStatus().equals("Favourite")) {
            holder.iv_like.setImageDrawable(context.getResources().getDrawable(R.drawable.fav_s));
            arrayList.add("Favourite");
        } else if (homeResponse.getStatus().equals("Unfavourite")) {
            holder.iv_like.setImageDrawable(context.getResources().getDrawable(R.drawable.fav_un));
            arrayList.add("Unfavourite");
        }

        holder.iv_restro_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Description.class);
                intent.putExtra("image", homeResponseArrayList.get(position).getImage_url());
                intent.putExtra("rest_name", homeResponseArrayList.get(position).getRest_name());
                intent.putExtra("rest_type", homeResponseArrayList.get(position).getRest_type());
                intent.putExtra("rest_description", homeResponseArrayList.get(position).getRest_description());
                intent.putExtra("lat", homeResponseArrayList.get(position).getLatitude());
                intent.putExtra("log", homeResponseArrayList.get(position).getLongitude());
                intent.putExtra("restaurant_id", homeResponseArrayList.get(position).getId());
                context.startActivity(intent);
            }
        });
        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.get(position).equals("Favourite")) {
                    holder.iv_like.setImageDrawable(context.getResources().getDrawable(R.drawable.fav_un));
                    arrayList.set(position, "Unfavourite");
                    fragment.callFavouriteApi(homeResponse.getId(), "Unfavourite");
                } else {
                    holder.iv_like.setImageDrawable(context.getResources().getDrawable(R.drawable.fav_s));
                    arrayList.set(position, "Favourite");
                    fragment.callFavouriteApi(homeResponse.getId(), "Favourite");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return homeResponseArrayList.size();
    }

    public class MyViewHomeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_restro_name, tv_dishes;
        ImageView iv_restro_image, iv_like;

        public MyViewHomeViewHolder(View itemView) {
            super(itemView);

            tv_restro_name = (TextView) itemView.findViewById(R.id.tv_restro_name);
            tv_dishes = (TextView) itemView.findViewById(R.id.tv_dishes);
            iv_restro_image = (ImageView) itemView.findViewById(R.id.iv_restro_image);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
        }
    }
}
