package com.wrx.quickeats.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.wrx.quickeats.activities.MapRouteAct;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.R;

import java.util.ArrayList;
import java.util.List;

import com.wrx.quickeats.activities.MapForOrderDispatch;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.bean.MyOrderResponse;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.retrofit.MyDialog;
import com.wrx.quickeats.util.BeanClass;
import com.wrx.quickeats.util.SharedPreferenceKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by mobua01 on 11/8/17.
 */

public class MyOrderAdap extends RecyclerView.Adapter<MyOrderAdap.MyOrderHOlder> {

    ArrayList<BeanClass> order=new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    List<MyOrderResponse> orderSummryBeanArrayList;


    public MyOrderAdap(Context context,List<MyOrderResponse> orderSummryBeanArrayList) {

        this.context=context;
        this.orderSummryBeanArrayList=orderSummryBeanArrayList;
    }


    @Override
    public MyOrderHOlder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate( R.layout.my_order_item, parent, false);
        return new MyOrderHOlder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MyOrderHOlder holder, final int position) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        holder.order_recyclerview.setLayoutManager(linearLayoutManager);

        OrderSummryAdapter orderSummryAdapter=new OrderSummryAdapter(context,orderSummryBeanArrayList.get(position).getOrderSummryResponseList());
        holder.order_recyclerview.setAdapter(orderSummryAdapter);

        holder.tv_order_time.setText(orderSummryBeanArrayList.get(position).getDate());
        holder.tv_order_quant.setText(orderSummryBeanArrayList.get(position).getTotal_item());
        holder.tv_order_restro.setText(orderSummryBeanArrayList.get(position).getRest_name());
        holder.tv_dishes.setText(orderSummryBeanArrayList.get(position).getRest_type());
        //holder.tv_order_desc.setText(order.get(position).getOrder_dish());
        //holder.tv_order_price.setText(order.get(position).getOrder_price());
        if (orderSummryBeanArrayList.get(position).getStatus().equals("Cooking"))
        {
            holder.imageView1.setImageResource(R.drawable.tick);
            holder.imageView12.setImageResource(R.drawable.tick);
            holder.imageView2.setImageResource(R.drawable.circle);
            holder.imag.setImageResource(R.drawable.circle);
            holder.download_progressbar.setProgress(40);
            holder.tv_order_recv_time.setText(orderSummryBeanArrayList.get(position).getAccept_time());
            holder.tv_order_pack_time.setText(orderSummryBeanArrayList.get(position).getAccept_time());
            holder.ivMap.setVisibility(View.GONE);


        }else if (orderSummryBeanArrayList.get(position).getStatus().equals("Dispatched"))
        {
            holder.imageView1.setImageResource(R.drawable.tick);
            holder.imageView12.setImageResource(R.drawable.tick);
            holder.imag.setImageResource(R.drawable.tick);
            holder.imageView2.setImageResource(R.drawable.circle);

            holder.download_progressbar.setProgress(70);
            holder.tv_order_recv_time.setText(orderSummryBeanArrayList.get(position).getAccept_time());
            holder.tv_order_pack_time.setText(orderSummryBeanArrayList.get(position).getAccept_time());
            holder.tv_order_del_time.setText(orderSummryBeanArrayList.get(position).getDispatch_time());

          //  holder.ivMap.setImageResource(R.drawable.delivery);
            holder.ivMap.setVisibility(View.VISIBLE);

        }else if (orderSummryBeanArrayList.get(position).getStatus().equals("Delivered")){
            holder.imageView1.setImageResource(R.drawable.tick);
            holder.imageView12.setImageResource(R.drawable.tick);
            holder.imageView2.setImageResource(R.drawable.tick);
            holder.imag.setImageResource(R.drawable.tick);

            holder.download_progressbar.setProgress(100);
            holder.tv_order_recv_time.setText(orderSummryBeanArrayList.get(position).getAccept_time());
            holder.tv_order_pack_time.setText(orderSummryBeanArrayList.get(position).getAccept_time());
            holder.tv_order_del_time.setText(orderSummryBeanArrayList.get(position).getDispatch_time());
            holder.tv_order_delivered_time.setText(orderSummryBeanArrayList.get(position).getDelivery_time());
            holder.ivMap.setVisibility(View.GONE);

        }else {
            holder.imageView1.setImageResource(R.drawable.circle);
            holder.imageView12.setImageResource(R.drawable.circle);
            holder.imageView2.setImageResource(R.drawable.circle);
            holder.imag.setImageResource(R.drawable.circle);
            holder.download_progressbar.setProgress(0);
            holder.tv_order_recv_time.setText("");
            holder.tv_order_pack_time.setText("");
            holder.tv_order_del_time.setText("");
            holder.tv_order_delivered_time.setText("");

            holder.ivMap.setVisibility(View.GONE);

        }

        AQuery aQuery = new AQuery(context);
        aQuery.id(holder.iv_order_image).image(orderSummryBeanArrayList.get(position).getImage_url(), false, false, 0, R.drawable.image);

        //holder.tv_order_time.setText(myOrderBeanArrayList.get(position).getAccept_time());
        holder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

         String orderId=orderSummryBeanArrayList.get(position).getOrderSummryResponseList().get(0).getOrder_id();
         String restorentId=orderSummryBeanArrayList.get(position).getId();

                // Creating Bundle object
                Bundle b = new Bundle();

                // Storing data into bundle
                b.putString("orderId", orderId);
                b.putString("restorentId", restorentId);

                // getdelLatlongApi(orderId,restorentId);
                Intent intent=new Intent(context, MapRouteAct.class);
                intent.putExtras(b);
                context.startActivity(intent);

            }
        });

    }

    private void getdelLatlongApi(String orderId,String restorentId ) {
        try {

            MyDialog.getInstance(context).showDialog();
            Retrofit retrofit = ApiClient.getClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            Call<CommonResponse> call = apiInterface.getdelLatlong(
                    SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.token),orderId,restorentId);

            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    MyDialog.getInstance(context).hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("SUCCESS"))
                        {
                            Intent intent=new Intent(context, MapForOrderDispatch.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);

                            Toast.makeText(context, "my order Successfully", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    MyDialog.getInstance(context).hideDialog();
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderSummryBeanArrayList.size();
    }

    public class MyOrderHOlder extends RecyclerView.ViewHolder {

        private TextView tv_order_time, tv_order_quant, tv_order_desc, tv_order_price,tv_order_recv_time,
                tv_order_pack_time,tv_order_del_time,tv_order_restro,tv_dishes,tv_order_delivered_time;
        private ImageView iv_order_image,imageView1,imageView12,imageView2,imag,ivMap;
        ProgressBar download_progressbar;
        RecyclerView order_recyclerview;

        public MyOrderHOlder(View itemView) {
            super(itemView);


            tv_order_time = (TextView) itemView.findViewById(R.id.tv_order_time);
            tv_order_quant = (TextView) itemView.findViewById(R.id.tv_order_quant);
            tv_order_restro = (TextView) itemView.findViewById(R.id.tv_order_restro);
            tv_dishes = (TextView) itemView.findViewById(R.id.tv_dishes);
            tv_order_delivered_time = (TextView) itemView.findViewById(R.id.tv_order_delivered_time);
           // tv_order_desc = (TextView) itemView.findViewById(R.id.tv_order_desc);
           // tv_order_price = (TextView) itemView.findViewById(R.id.tv_order_price);
            //tv_order_recv_time = (TextView) itemView.findViewById(R.id.tv_order_recv_time);
            tv_order_recv_time = (TextView) itemView.findViewById(R.id.tv_order_recv_time);
            tv_order_pack_time = (TextView) itemView.findViewById(R.id.tv_order_pack_time);
            tv_order_del_time = (TextView) itemView.findViewById(R.id.tv_order_del_time);

            iv_order_image = (ImageView) itemView.findViewById(R.id.iv_order_image);
            download_progressbar = (ProgressBar) itemView.findViewById(R.id.download_progressbar);
            order_recyclerview = (RecyclerView) itemView.findViewById(R.id.order_recyclerview);

            imageView1=(ImageView)itemView.findViewById(R.id.imageView1);
            imageView12=(ImageView)itemView.findViewById(R.id.imageView12);
            imageView2=(ImageView)itemView.findViewById(R.id.imageView2);
            imag=(ImageView)itemView.findViewById(R.id.imag);

            ivMap=(ImageView)itemView.findViewById(R.id.ivMap);

        }
    }
}
