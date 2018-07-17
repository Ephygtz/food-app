package com.wrx.quickeats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.wrx.quickeats.bean.NotificationResponse;
import com.wrx.quickeats.R;

import java.util.List;


public class NotifcationAdapter extends RecyclerView.Adapter<NotifcationAdapter.MyViewHolder>{

    private List<NotificationResponse> noti;
    Context context;

    public NotifcationAdapter(Context context,List<NotificationResponse> noti) {
        this.context = context;
        this.noti = noti;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_item, parent, false);
              return new MyViewHolder(itemview);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        String date[]=noti.get(position).getDate().split(" ");
        holder.tv_notif_content.setText(noti.get(position).getMessage());
        holder.tv_notif_time.setText(date[0]);
        holder.tv_notif_time1.setText(date[1]);
        AQuery aQuery=new AQuery(context);
        aQuery.id(holder.iv_notif_user).image(noti.get(position).getImage_url(), false, false, 0, R.drawable.imgae_notification);


        if(position==noti.size()-1){

            holder.noti_viewline.setVisibility(View.GONE);
        }
        else {
            holder.noti_viewline.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return noti.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_notif_user;
        private  View noti_viewline;
        private TextView tv_notif_content,tv_notif_time,tv_notif_time1;

        public MyViewHolder(View itemView) {
            super(itemView);

            iv_notif_user=(ImageView)itemView.findViewById(R.id.iv_notif_user);
            tv_notif_content=(TextView) itemView.findViewById(R.id.tv_notif_content);
            tv_notif_time=(TextView) itemView.findViewById(R.id.tv_notif_time);
            tv_notif_time1=(TextView) itemView.findViewById(R.id.tv_notif_time1);
            noti_viewline=(View) itemView.findViewById(R.id.noti_viewline);

        }
    }
}
