package com.wrx.quickeats;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.wrx.quickeats.activities.FoodMenu;
import com.wrx.quickeats.activities.MyKart;
import com.wrx.quickeats.adapter.FoodMenuAdapter;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.bean.FMenuBean;
import com.wrx.quickeats.bean.FilterBean;
import com.wrx.quickeats.bean.MenuResponse;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.retrofit.MyDialog;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.content.Context;

import static com.wrx.quickeats.util.RestroApplication.getContext;

public class FoodDetails extends AppCompatActivity {
  private RecyclerView recyclerView;
  TextView food_name, food_price, food_description, imgPlus,imgMinus,tvQuantity ;
  ImageView img_Food;
  CollapsingToolbarLayout collapsingToolbarLayout;
  FloatingActionButton btnCart;
  String restaurantId;
  FoodMenuAdapter adapter;
  ArrayList<MenuResponse> menuResponseArrayList;
  private ArrayList<FilterBean> filterBeanArrayList;
  ArrayList<FMenuBean> fMenuBeanArrayList;
  private ActionBar mToolbar;
  MenuResponse menuResponse;
  String restaurant_id;
  int totalQuantity;
  int REQUEST_CODE = 102;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_food_details);


    //init view

    btnCart = (FloatingActionButton)findViewById(R.id.btnCart);


    food_description = (TextView)findViewById(R.id.food_description);
    food_name = (TextView)findViewById(R.id.food_name);
    food_price = (TextView)findViewById(R.id.food_price);
    img_Food = (ImageView)findViewById(R.id.img_Food);
    imgMinus = (TextView)  findViewById(R.id.imgMinus);
    imgPlus = (TextView) findViewById(R.id.imgPlus);
    tvQuantity = (TextView) findViewById(R.id.tvQuantity);


    collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsingtoolbar);
    //collapsingToolbarLayout.setTitleEnabled(false);
    //mToolbar.setTitle(menuResponseArrayList);
    collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppbar);
    collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.ExpandedAppbar);

    //Get food Id from Intent

 Bundle bundle = getIntent().getExtras();
    Gson gson = new Gson();
 if(bundle != null){
   String menuResponeJson = bundle.getString("details");

   if (!menuResponeJson.isEmpty()){
      menuResponse = gson.fromJson(menuResponeJson, MenuResponse.class);
     food_description.setText(menuResponse.getMenu_description());
     food_name.setText(menuResponse.getMenu_name());
     food_price.setText(menuResponse.getMenu_price());

     collapsingToolbarLayout.setTitle(menuResponse.getMenu_name());

     //set image

     //Picasso.get(getBaseContext()).load(menuResponse.getImage_url())
     //    .into(img_Food);

     Picasso.get().load(menuResponse.getImage_url())
         .into(img_Food);

   }

 }

 imgPlus.setOnClickListener(new View.OnClickListener() {
   @Override public void onClick(View view) {
     int count = Integer.parseInt(tvQuantity.getText().toString());
     count = count + 1;
     tvQuantity.setText("" + count);
     menuResponse.setQuantityCount(count);
fMenuBeanArrayList = new ArrayList<FMenuBean>();
     if (fMenuBeanArrayList.size() > 0){

       for (int i = 0; i < fMenuBeanArrayList.size(); i++){
         FMenuBean fMenuBean = new FMenuBean();
         fMenuBean.setId(menuResponse.getId());
         fMenuBean.setItemCount(menuResponse.getQuantityCount());
         fMenuBean.setQuantity("" + menuResponse.getQuantityCount());
         fMenuBean.setItem_name(menuResponse.getMenu_name());
         fMenuBean.setItem_price(menuResponse.getMenu_price());

         fMenuBeanArrayList.set(i, fMenuBean);
         break;
       }
     }else {
       FMenuBean fMenuBean = new FMenuBean();
       fMenuBean.setId(menuResponse.getId());
       fMenuBean.setItemCount(menuResponse.getQuantityCount());
       fMenuBean.setQuantity("" + menuResponse.getQuantityCount());
       fMenuBean.setItem_name(menuResponse.getMenu_name());
       fMenuBean.setItem_price(menuResponse.getMenu_price());
       fMenuBean.setItem_price(menuResponse.getMenu_price());


       fMenuBeanArrayList.add(fMenuBean);

     }
   }
 });

 imgMinus.setOnClickListener(new View.OnClickListener() {
   @Override public void onClick(View view) {
     int count = Integer.parseInt(tvQuantity.getText().toString());
     count = count - 1;
     tvQuantity.setText("" + count);
     menuResponse.setQuantityCount(count);
     fMenuBeanArrayList = new ArrayList<FMenuBean>();

     if (count >= 0){

       for (int i = 0; i < fMenuBeanArrayList.size(); i++){
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
   }
 });

 btnCart.setOnClickListener(new View.OnClickListener() {
   @Override public void onClick(View view) {

     if (fMenuBeanArrayList != null) {
       ArrayList<FMenuBean> arrayList = new ArrayList<>();
       if (fMenuBeanArrayList.size() > 0) {
         for (int i = 0; i < fMenuBeanArrayList.size(); i++) {
           if (fMenuBeanArrayList.get(i).getItemCount() > 0) {
             FMenuBean fMenuBean = new FMenuBean();
             fMenuBean.setId(fMenuBeanArrayList.get(i).getId());
             fMenuBean.setQuantity(fMenuBeanArrayList.get(i).getQuantity());
             fMenuBean.setItemCount(fMenuBeanArrayList.get(i).getItemCount());
             fMenuBean.setItem_name(fMenuBeanArrayList.get(i).getItem_name());
             fMenuBean.setItem_price(fMenuBeanArrayList.get(i).getItem_price());
             arrayList.add(fMenuBean);
             totalQuantity = totalQuantity + fMenuBeanArrayList.get(i).getItemCount();
           }
         }
       }

       if (arrayList.size() > 0) {

         Intent intent = new Intent(FoodDetails.this, MyKart.class);
         intent.putExtra("menuList", arrayList);
         intent.putExtra("restaurant_id", restaurant_id);
         intent.putExtra("total_item", totalQuantity);


         startActivityForResult(intent, REQUEST_CODE);
       } else {
         Toast.makeText(FoodDetails.this, "Please select at least one menu item", Toast.LENGTH_LONG)
             .show();
       }
     }
   }
 });

  }



}
