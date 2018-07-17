package com.wrx.quickeats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.OrderSummryResponse;

import java.util.List;

/**
 * Created by mobulous51 on 6/10/17.
 */

public class OrderSummryAdapter extends RecyclerView.Adapter<OrderSummryAdapter.OrderAdapSummry> {

    List<OrderSummryResponse> orderSummryBeanArrayList;
    Context context;
    LayoutInflater inflater;
    int posstionn;


    public OrderSummryAdapter(Context context, List<OrderSummryResponse> orderSummryBeanArrayList) {

        this.context = context;
        this.orderSummryBeanArrayList = orderSummryBeanArrayList;
    }

    @Override
    public OrderSummryAdapter.OrderAdapSummry onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        View view = inflater.inflate(R.layout.myordersummry_item, parent, false);
        return new OrderAdapSummry(view);
    }

    @Override
    public void onBindViewHolder(OrderSummryAdapter.OrderAdapSummry holder, int position) {

//        OrderSummryBean orderSummryBean=orderSummryBeanArrayList.get(position);
//        holder.item_name.setText(orderSummryBean.getItem_name());
//        holder.item_quantity.setText(orderSummryBean.getQuantity());
//        holder.total.setText(orderSummryBean.getItem_price());

        holder.item_name.setText(orderSummryBeanArrayList.get(position).getItem_name());
        holder.item_quantity.setText(orderSummryBeanArrayList.get(position).getQuantity());
        holder.total.setText(orderSummryBeanArrayList.get(position).getItem_price());

    }

    @Override
    public int getItemCount() {
        return orderSummryBeanArrayList.size();
    }

    public class OrderAdapSummry extends RecyclerView.ViewHolder {

        TextView item_name, item_quantity, total;

        public OrderAdapSummry(View itemView) {
            super(itemView);

            item_name=(TextView)itemView.findViewById(R.id.item_name);
            item_quantity=(TextView)itemView.findViewById(R.id.item_quantity);
            total=(TextView)itemView.findViewById(R.id.total);


        }
    }
}
