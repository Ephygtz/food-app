package com.wrx.quickeats.adapter;

///**
// * Created by mobulous51 on 12/9/17.
// */

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.wrx.quickeats.FoodDetails;
import com.wrx.quickeats.R;
import com.wrx.quickeats.activities.FoodMenu;
import com.wrx.quickeats.bean.FMenuBean;

import com.wrx.quickeats.bean.MenuResponse;
import com.wrx.quickeats.databinding.FmenuSingleRowBinding;

import java.util.ArrayList;

public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.FMenuVHolder> {

  private ArrayList<MenuResponse> menuResponseArrayList;
  private LayoutInflater inflater;
  private FMenuListener listener;
  Context context;
  ArrayList<FMenuBean> fMenuBeanArrayList;
  MenuResponse menuResponse;

  public FoodMenuAdapter(Context context, ArrayList<MenuResponse> menuResponseArrayList,
      ArrayList<FMenuBean> fMenuBeanArrayList) {
    this.context = context;
    this.menuResponseArrayList = menuResponseArrayList;
    this.fMenuBeanArrayList = fMenuBeanArrayList;
  }

  @Override public FMenuVHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (inflater == null) this.inflater = LayoutInflater.from(parent.getContext());

    FmenuSingleRowBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.fmenu_single_row, parent, false);

    return new FMenuVHolder(binding);
  }

  @Override public void onBindViewHolder(final FMenuVHolder holder, final int position) {
     menuResponse = menuResponseArrayList.get(position);

    if (fMenuBeanArrayList.size() > 0) {
      try {
        for (int i = 0; i < fMenuBeanArrayList.size(); i++) {
          if (fMenuBeanArrayList.get(i).getId().equals(menuResponse.getId())) {
            holder.binding.tvQuantity.setText("" + fMenuBeanArrayList.get(i).getItemCount());
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }


    holder.binding.foodName.setText(menuResponse.getMenu_name());
    holder.binding.description.setText(menuResponse.getMenu_description());
    holder.binding.tvPrice.setText(" " + menuResponse.getMenu_price());
    AQuery aQuery = new AQuery(context);

    Log.e("IMAGE URL", (menuResponse.getImage_url().toString()));

    aQuery.id(holder.binding.imgFood)
        .image(menuResponse.getImage_url(), true, false, 0, R.drawable.food);

    holder.binding.imgPlus.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        menuResponse = menuResponseArrayList.get(position);
        int count = Integer.parseInt(holder.binding.tvQuantity.getText().toString());
        count = count + 1;
        holder.binding.tvQuantity.setText("" + count);
        menuResponse.setQuantityCount(count);

        if (fMenuBeanArrayList.size() > 0) {

          for (int i = 0; i < fMenuBeanArrayList.size(); i++) {
            if (fMenuBeanArrayList.get(i).getId().equals(menuResponse.getId())) {
              FMenuBean fMenuBean = new FMenuBean();
              fMenuBean.setId(menuResponse.getId());
              fMenuBean.setItemCount(menuResponse.getQuantityCount());
              fMenuBean.setQuantity("" + menuResponse.getQuantityCount());
              fMenuBean.setItem_name(menuResponse.getMenu_name());
              fMenuBean.setItem_price(menuResponse.getMenu_price());

              fMenuBeanArrayList.set(i, fMenuBean);
              break;
            }
          }
        } else {
          FMenuBean fMenuBean = new FMenuBean();
          fMenuBean.setId(menuResponse.getId());
          fMenuBean.setItemCount(menuResponse.getQuantityCount());
          fMenuBean.setQuantity("" + menuResponse.getQuantityCount());
          fMenuBean.setItem_name(menuResponse.getMenu_name());

          fMenuBean.setItem_price(menuResponse.getMenu_price());

          fMenuBeanArrayList.add(fMenuBean);
        }
      }
    });
    holder.binding.imgMinus.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        menuResponse = menuResponseArrayList.get(position);
        int count = Integer.parseInt(holder.binding.tvQuantity.getText().toString());
        count = count - 1;
        if (count >= 0) {
          holder.binding.tvQuantity.setText("" + count);
          menuResponse.setQuantityCount(count);
          for (int i = 0; i < fMenuBeanArrayList.size(); i++) {
            if (fMenuBeanArrayList.get(i).getId().equals(menuResponse.getId())) {
              FMenuBean fMenuBean = new FMenuBean();
              fMenuBean.setItemCount(count);
              fMenuBean.setQuantity("" + count);
              fMenuBean.setId(menuResponse.getId());
              fMenuBean.setItem_name(menuResponse.getMenu_name());
              fMenuBean.setItem_price(menuResponse.getMenu_price());
              fMenuBeanArrayList.set(i, fMenuBean);
              break;
            }
          }
        }
        //                if (count == 0) {
        //                    for (int i = 0; i < fMenuBeanArrayList.size(); i++) {
        //                        if (fMenuBeanArrayList.get(i).getId().equals(menuResponse.getId())) {
        //                            fMenuBeanArrayList.remove(i);
        //                            break;
        //                        }
        //                    }
        //
        //                }
      }
    });

    holder.main.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        menuResponse = menuResponseArrayList.get(position);
        Intent intent = new Intent(v.getContext(),FoodDetails.class);
        Gson gson = new Gson();
        String menuresponseJson = gson.toJson(menuResponse);
        intent.putExtra("details", menuresponseJson);
        context.startActivity(intent);
      }
    });


  }

  @Override public int getItemCount() {
    if (menuResponseArrayList != null) {
      return menuResponseArrayList.size();
    } else {
      return 7;
    }
  }

  public void setUpFMenuListener(FMenuListener listener) {
    this.listener = listener;
  }

  //    CLASS: VIEW HOLDER
  class FMenuVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private FmenuSingleRowBinding binding;
    private RelativeLayout main;


    // ImageView imgfood,imgPlus ,imgminus;
    // TextView  foodname , description , tvprice  ,tvQuantity;
    //public ViewHolder(View view){
    //
    //  super(view);
    //
    //  main = (RelativeLayout) view.findViewById(R.id.main);
    //}

    public FMenuVHolder(FmenuSingleRowBinding binding) {
      super(binding.getRoot());
      //super( view);
      this.binding = binding;
      //listener.getFoodMenuList(menuResponseArrayList);

      // this.binding.imgMinus.setOnClickListener(this);
      //this.binding.imgPlus.setOnClickListener(this);
      main = binding.rlRight;

    }

    @Override public void onClick(View v) {
      switch (v.getId()) {
        // case R.id.img_minus:
        // listener.onMinusClick(binding.getRoot(),getAdapterPosition());

        //break;
        //case R.id.img_plus:
        //listener.onPlusClick(binding.getRoot(),getAdapterPosition());
        //break;

      }
    }
  }

  //    INTERFACE: LISTENER
  public interface FMenuListener {

    void getFoodMenuList(ArrayList<MenuResponse> arrayList);

    //void onPlusClick(View view, int pos);
    //void onMinusClick(View view, int pos);
  }
}
