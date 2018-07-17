package com.wrx.quickeats.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wrx.quickeats.bean.FMenuBean;
import com.wrx.quickeats.R;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by mobua01 on 14/8/17.
 */

public class MyCart_Adap extends RecyclerView.Adapter<MyCart_Adap.MyCartHolder> {

  ArrayList<FMenuBean> fMenuBeanArrayList;
  Context context;
  TextView grand_total, sub_total;
  double gra_total, su_total;
  MyInterface myInterface;
  String isNotify = "";

  public MyCart_Adap(Context context, ArrayList<FMenuBean> fMenuBeanArrayList,
      TextView grand_total, TextView sub_total) {

    this.context = context;
    this.fMenuBeanArrayList = fMenuBeanArrayList;
    this.grand_total = grand_total;
    this.sub_total = sub_total;
  }

  @Override public MyCartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemview =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false);
    return new MyCartHolder(itemview);
  }

  @Override public void onBindViewHolder(final MyCartHolder holder, final int position) {
    final FMenuBean fMenuBean = fMenuBeanArrayList.get(position);
    final double price = Double.parseDouble(fMenuBeanArrayList.get(position).getItem_price());
    double quantity = Double.parseDouble(fMenuBeanArrayList.get(position).getQuantity());
    double total = price * quantity;
    double total2 = price * quantity;
    gra_total = gra_total + total;
    su_total = su_total + total2;
    if (fMenuBean.getItemCount() > 0) {
      holder.tv_cart_item_name.setText(fMenuBeanArrayList.get(position).getItem_name());
      holder.tv_itemquantity.setText(fMenuBeanArrayList.get(position).getQuantity());
      holder.signle_food_price.setText(
          "from KES " + fMenuBeanArrayList.get(position).getItem_price());
      holder.tv_cart_item_price.setText("KES " + total2);


      //holder.signle_food_price.setText(
      //    "from KES " + fMenuBeanArrayList.get(position).getItem_price());
      //holder.sub_total.setText("KES " + total2);

      //sub_total.setText("KES " + su_total);


      Log.w("grand total", "" + gra_total);
      holder.cardView.setVisibility(View.VISIBLE);
    }

    //
    //holder.iv_minus.setOnClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View v) {
    //    int count = Integer.parseInt(holder.tv_itemquantity.getText().toString());
    //    count = count - 1;
    //    double total;
    //    double total2 = 0;
    //    if (count > 0) {
    //      holder.tv_itemquantity.setText("" + count);
    //      total = price * count;
    //      holder.tv_cart_item_price.setText("KES " + total2);
    //      //holder.sub_total.setText("KES " + total2);
    //      fMenuBean.setQuantity("" + count);
    //      fMenuBean.setItemCount(count);
    //      gra_total =
    //          gra_total - Integer.parseInt(fMenuBeanArrayList.get(position).getItem_price());
    //      grand_total.setText("KES " + gra_total);
    //      isNotify = "no";
    //      // myInterface.getFoodIdCount(fMenuBeanArrayList.get(position).getId(),count);
    //    }
    //
    //    if (count == 0) {
    //      // holder.tv_itemquantity.setText("" + count);
    //      // double total = price * count;
    //      // holder.tv_cart_item_price.setText("$" + total+"");
    //      isNotify = "yes";
    //      fMenuBean.setQuantity("" + count);
    //      fMenuBean.setItemCount(count);
    //      gra_total =
    //          gra_total - Integer.parseInt(fMenuBeanArrayList.get(position).getItem_price());
    //      grand_total.setText("KES " + gra_total);
    //      fMenuBeanArrayList.remove(position);
    //      notifyDataSetChanged();
    //      //                    holder.cardView.setVisibility(View.GONE);
    //      //                    fMenuBean.setQuantity("" + count);
    //      //                    fMenuBean.setItemCount(count);
    //    }
    //  }
    //});
    //holder.iv_plus.setOnClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View v) {
    //    int count = Integer.parseInt(holder.tv_itemquantity.getText().toString());
    //    count = count + 1;
    //    holder.tv_itemquantity.setText("" + count);
    //    double total = price * count;
    //    double total2 = price * count;
    //    holder.tv_cart_item_price.setText("KES " + total2);
    //    //holder.sub_total.setText("KES " + total2);
    //    fMenuBean.setQuantity("" + count);
    //    fMenuBean.setItemCount(count);
    //    gra_total = gra_total + Integer.parseInt(fMenuBeanArrayList.get(position).getItem_price());
    //    grand_total.setText("KES " + gra_total);
    //    //myInterface.getFoodIdCount(fMenuBeanArrayList.get(position).getId(),count);
    //
    //  }
    //});
  }

  @Override public int getItemCount() {
    return fMenuBeanArrayList.size();
  }

  public class MyCartHolder extends RecyclerView.ViewHolder {

    private TextView sub_total, tv_cart_item_price, tv_cart_item_name, tv_itemquantity,
        signle_food_price,iv_minus, iv_plus;
    private ImageView img_Food;

    LinearLayout lll;
    CardView cardView;

    public MyCartHolder(View itemView) {
      super(itemView);

      //tv_editorder = (TextView) itemView.findViewById(R.id.tv_editorder);
      tv_cart_item_price = (TextView) itemView.findViewById(R.id.tv_cart_item_price);
      tv_cart_item_name = (TextView) itemView.findViewById(R.id.tv_cart_item_name);
      tv_itemquantity = (TextView) itemView.findViewById(R.id.tv_itemquantity);
      signle_food_price = (TextView) itemView.findViewById(R.id.signle_food_price);
      //iv_minus = (TextView) itemView.findViewById(R.id.iv_minus);
      //iv_plus = (TextView) itemView.findViewById(R.id.iv_plus);
      lll = (LinearLayout) itemView.findViewById(R.id.lll);
      cardView = (CardView) itemView.findViewById(R.id.cardView);
      //img_Food = (ImageView)itemView.findViewById(R.id.img_Food);
      //sub_total = (TextView) itemView.findViewById(R.id.sub_total);
    }
  }

  public interface MyInterface {
    public void getFoodIdCount(String id, int count);
  }

  public void setMenuListener(MyInterface myInterface) {
    this.myInterface = myInterface;
  }
}
