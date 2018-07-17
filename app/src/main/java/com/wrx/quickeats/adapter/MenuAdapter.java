package com.wrx.quickeats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wrx.quickeats.util.BeanClass;
import com.wrx.quickeats.R;

import java.util.ArrayList;

/**
 * Created by mobua01 on 16/8/17.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyMenuAdapter> {

    ArrayList<BeanClass> menu = new ArrayList<>();
    Context context;

    public MenuAdapter(Context context, ArrayList<BeanClass> menu) {

        this.context = context;
        this.menu = menu;
    }

    @Override
    public MyMenuAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.restro_menu_item, parent, false);
        return new MyMenuAdapter(itemview);
    }

    @Override
    public void onBindViewHolder(MyMenuAdapter holder, int position) {

        holder.tv_menu_item_name.setText(menu.get(position).getMenu_dish_name());

        if(position==menu.size()-1){
            holder.order_view.setVisibility(View.GONE);

        }else {
            holder.order_view.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    public class MyMenuAdapter extends RecyclerView.ViewHolder {

        TextView tv_menu_item_name,tv_itemquantity;
        ImageView iv_minus,iv_plus;
        View order_view;

        public MyMenuAdapter(View itemView) {
            super(itemView);

            tv_menu_item_name = (TextView) itemView.findViewById(R.id.tv_menu_item_name);
            tv_itemquantity = (TextView) itemView.findViewById(R.id.tv_itemquantity);
            iv_minus = (ImageView) itemView.findViewById(R.id.iv_minus);
            iv_plus = (ImageView) itemView.findViewById(R.id.iv_plus);
            order_view = (View) itemView.findViewById(R.id.order_view);

        }
    }

}
