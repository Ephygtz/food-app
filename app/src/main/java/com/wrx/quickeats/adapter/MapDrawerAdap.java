package com.wrx.quickeats.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wrx.quickeats.R;

import java.util.ArrayList;

import com.wrx.quickeats.activities.Description;
import com.wrx.quickeats.bean.RestaurantResponse;

/**
 * Created by mobua01 on 12/8/17.
 */

public class MapDrawerAdap extends RecyclerView.Adapter<MapDrawerAdap.MyMapViewholder> {

    ArrayList<RestaurantResponse> map_drawer;
    Context context;
    MyClickListner listner;
    public MapDrawerAdap(Context context,ArrayList<RestaurantResponse> map_drawer) {

        this.context=context;
        this.map_drawer = map_drawer;
    }


    @Override
    public MyMapViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_drawer_layout, parent, false);
        return new MyMapViewholder(itemview);
    }

    @Override
    public void onBindViewHolder(MapDrawerAdap.MyMapViewholder holder, final int position) {

        holder.tv_map_restro.setText(map_drawer.get(position).getRest_name());
        holder.tv_map_cousin.setText(map_drawer.get(position).getRest_type());
        if (map_drawer.get(position).getStatus().equals("Favourite"))
        {
            holder.favOrUnfav.setImageResource(R.drawable.fav_s);
        }
        else {
            holder.favOrUnfav.setImageResource(R.drawable.fav_un);
        }
        holder.cardview_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Description.class);
                intent.putExtra("image", map_drawer.get(position).getImage_url());
                intent.putExtra("rest_name", map_drawer.get(position).getRest_name());
                intent.putExtra("rest_type", map_drawer.get(position).getRest_type());
                intent.putExtra("rest_description", map_drawer.get(position).getRest_description());
                intent.putExtra("lat", map_drawer.get(position).getLatitude());
                intent.putExtra("log", map_drawer.get(position).getLongitude());
                intent.putExtra("restaurant_id", map_drawer.get(position).getId());
                context.startActivity(intent);
            }
        });
       
       

    }

    @Override
    public int getItemCount() {
        return map_drawer.size();
    }

    public class MyMapViewholder extends RecyclerView.ViewHolder {

     private ImageView iv_map_dimage;
        private TextView tv_map_restro,tv_map_cousin;
        CardView cardview_layout;
        private ImageView favOrUnfav;


        public MyMapViewholder(View itemView) {
            super(itemView);

            iv_map_dimage=(ImageView)itemView.findViewById(R.id.iv_map_dimage);
            favOrUnfav=(ImageView)itemView.findViewById(R.id.favOrUnfav);

            tv_map_restro=(TextView) itemView.findViewById(R.id.tv_map_restro);
            tv_map_cousin=(TextView) itemView.findViewById(R.id.tv_map_cousin);
            cardview_layout=(CardView) itemView.findViewById(R.id.cardview_layout);

        }

    }

    public void onItemClickListener(MyClickListner listner){
        this.listner=listner;

    }

    public interface MyClickListner{

         void OnCardClick(int position,View v);

    }
}
