package com.wrx.quickeats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wrx.quickeats.R;

import java.util.List;

import com.wrx.quickeats.bean.MyFavouriteResponse;
import com.wrx.quickeats.fragments.MyFavourite;

/**
 * Created by mobua01 on 11/8/17.
 */

public class MYFavAdap extends RecyclerView.Adapter<MYFavAdap.MyFavAdap> {

    List<MyFavouriteResponse> myFavouriteResponseList;
    Context context;
    boolean fav_unfavourite=true;
    MyFavourite fragment;

    public MYFavAdap(Context context, List<MyFavouriteResponse> myFavouriteResponseList,MyFavourite fragment) {

        this.context = context;
        this.myFavouriteResponseList = myFavouriteResponseList;
        this.fragment=fragment;
    }

    @Override
    public MyFavAdap onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_fav_item, parent, false);
        return new MyFavAdap(itemview);
    }

    @Override
    public void onBindViewHolder(final MyFavAdap holder, final int position) {
        final MyFavouriteResponse myFavouriteResponse = myFavouriteResponseList.get(position);

        holder.tv_fav_rname.setText(myFavouriteResponse.getRest_name());
        holder.tv_fav_cousin.setText(myFavouriteResponse.getRest_type());

        if (position == myFavouriteResponseList.size() - 1) {

            holder.fav_viewline.setVisibility(View.GONE);
        } else {
            holder.fav_viewline.setVisibility(View.VISIBLE);

        }
        holder.fav_unfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (fav_unfavourite)
//                {
//                    holder.fav_unfav.setImageDrawable(context.getResources().getDrawable(R.drawable.fav_un));
//                    fav_unfavourite=false;
//                    myFavouriteResponseList.remove(position);
//                    fragment.callFavouriteApi(myFavouriteResponse.getFavId(),"Favourite");
//                }
//                else
//                {
//                    holder.fav_unfav.setImageDrawable(context.getResources().getDrawable(R.drawable.fav_s));
//                    fav_unfavourite=true;
//                }
                //holder.fav_unfav.setImageDrawable(context.getResources().getDrawable(R.drawable.fav_un));
                fragment.callFavouriteApi(myFavouriteResponse.getFavId(),"Unfavourite",position);
                //myFavouriteResponseList.remove(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return myFavouriteResponseList.size();
    }

    public class MyFavAdap extends RecyclerView.ViewHolder {

        private ImageView fav_unfav;
        private TextView tv_fav_rname, tv_fav_cousin, tv_review;
        private View fav_viewline;


        public MyFavAdap(View itemView) {
            super(itemView);

            tv_fav_rname = (TextView) itemView.findViewById(R.id.tv_fav_rname);
            tv_fav_cousin = (TextView) itemView.findViewById(R.id.tv_fav_cousin);
            tv_review = (TextView) itemView.findViewById(R.id.tv_review);
            fav_viewline = (View) itemView.findViewById(R.id.fav_viewline);
            fav_unfav=(ImageView)itemView.findViewById(R.id.fav_unfav);


        }
    }
}
