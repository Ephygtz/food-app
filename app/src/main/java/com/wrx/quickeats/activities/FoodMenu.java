package com.wrx.quickeats.activities;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.Button;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wrx.quickeats.FoodDetails;
import com.wrx.quickeats.adapter.FoodMenuAdapter;
import com.wrx.quickeats.adapter.MenuFilterAdapter;
import com.wrx.quickeats.bean.CategoryListResponse;
import com.wrx.quickeats.bean.FMenuBean;
import com.wrx.quickeats.bean.FilterBean;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.adapter.MyCart_Adap;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.bean.MenuResponse;
import com.wrx.quickeats.databinding.ActivityFoodMenuBinding;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.MyDialog;
import com.wrx.quickeats.util.CommonTask;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodMenu extends AppCompatActivity
    implements View.OnClickListener, MyCart_Adap.MyInterface {

  private ActivityFoodMenuBinding binding;
  private ArrayList<FilterBean> filterBeanArrayList;
  private String nameToolbar = "";
  ArrayList<MenuResponse> menuResponseArrayList;
  TextView tv_title;
  String restaurant_id;
  ArrayList<CategoryListResponse> categoryListResponses;
  // ArrayList<MenuResponse> categoryMenuResponses;
  FoodMenuAdapter adapter;
  ArrayList<FMenuBean> fMenuBeanArrayList;
  int totalQuantity;
  int REQUEST_CODE = 102;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_food_menu);

    filterBeanArrayList = new ArrayList<>();

    setUpToolbar();

    setUpList();

    setUpRecycler();

    binding.rlFilterImg.setOnClickListener(this);

    try {
      restaurant_id = getIntent().getStringExtra("restaurant_id");
    } catch (Exception e) {
      e.printStackTrace();
    }

    callMenuListApi();

    //
    //        vp_menu=(ViewPager)findViewById(R.id.vp_menu);
    //        Menu_tab = (TabLayout) findViewById(R.id.Menu_tab);
    //        btn_addtoCart = (Button) findViewById(R.id.btn_addtoCart);
    //
    //        chinese = new Chinese();
    //        indian= new Indian();
    //        italian = new Italian();
    //        spanish = new Spanish();
    //
    //        SetUpViewPager(vp_menu);
    //
    //        Menu_tab.addTab(Menu_tab.newTab().setText("Chinese"));
    //        Menu_tab.addTab(Menu_tab.newTab().setText("Indian"));
    //        Menu_tab.addTab(Menu_tab.newTab().setText("Italian"));
    //        Menu_tab.addTab(Menu_tab.newTab().setText("Spanish"));
    //
    //        Menu_tab.setTabGravity(TabLayout.GRAVITY_FILL);
    //
    //        Menu_tab.setupWithViewPager(vp_menu);
    //
    //        vp_menu.setOffscreenPageLimit(3);
    //
    //
    //        btn_addtoCart.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //
    //               // getFragmentManager().beginTransaction().replace(R.id.search_container, new MyKart()).addToBackStack("").commit();
    //                startActivity(new Intent(FoodMenu.this,MyKart.class));
    //
    //            }
    //        });

  }

  @Override protected void onResume() {
    super.onResume();
    //tv_title.setText("Menu");
  }

  private void setUpToolbar() {
    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    tv_title = (TextView) findViewById(R.id.tv_title);

    setSupportActionBar(myToolbar);
    if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

    tv_title.setVisibility(View.VISIBLE);
    tv_title.setText("Menu");
    tv_title.setTextSize(20);

    myToolbar.setNavigationIcon(R.drawable.back);

    myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        onBackPressed();
      }
    });
  }

  private void setUpRecycler() {
    LinearLayoutManager layoutManager =
        new LinearLayoutManager(FoodMenu.this, LinearLayoutManager.VERTICAL, true);
    layoutManager.setReverseLayout(true);
    layoutManager.setStackFromEnd(true);
    binding.recyclerV.setNestedScrollingEnabled(false);
    binding.recyclerV.setLayoutManager(layoutManager);

    //        adapter.setUpFMenuListener(new FoodMenuAdapter.FMenuListener() {
    ////            String value = "";
    ////            int c=0;
    //
    //            @Override
    //            public void onPlusClick(View view, int pos) {
    ////                TextView tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
    ////                value = tv_quantity.getText().toString().trim();
    ////                c = Integer.parseInt(value);
    ////                tv_quantity.setText(c + 1);
    //            }
    //
    //            @Override
    //            public void onMinusClick(View view, int pos) {
    ////                TextView tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
    ////                value = tv_quantity.getText().toString().trim();
    ////                c = Integer.parseInt(value);
    ////
    ////                if(value.equalsIgnoreCase("0")) {
    ////                    c = 0;
    ////                    tv_quantity.setText(c + 1);
    ////                }
    //
    //
    //            }
    //        });

  }

  //
  //    class MenuPagerAdapter extends FragmentStatePagerAdapter {
  //
  //        private final List<Fragment> mFragmentList = new ArrayList<>();
  //        private final List<String> mFragmentTitleList = new ArrayList<>();
  //
  //
  //        public MenuPagerAdapter(FragmentManager fm) {
  //            super(fm);
  //        }
  //
  //        @Override
  //        public Fragment getItem(int position) {
  //            return mFragmentList.get(position);
  //        }
  //
  //        @Override
  //        public int getCount() {
  //            return mFragmentList.size();
  //        }
  //
  //
  //        @Override
  //        public int getItemPosition(Object object) {
  //            // refresh all fragments when data set changed
  //            return PagerAdapter.POSITION_NONE;
  //        }
  //
  //        void AddFragment(Fragment fragment, String title) {
  //            mFragmentList.add(fragment);
  //            mFragmentTitleList.add(title);
  //        }
  //
  //        @Override
  //        public CharSequence getPageTitle(int position) {
  //            return mFragmentTitleList.get(position);
  //        }
  //
  //    }
  //    private  void SetUpViewPager(ViewPager viewPager){
  //        MenuPagerAdapter menuPagerAdapter= new MenuPagerAdapter(getSupportFragmentManager());
  //        menuPagerAdapter.AddFragment(chinese, "Chinese");

  //        menuPagerAdapter.AddFragment(indian, "Indian");
  //        menuPagerAdapter.AddFragment(italian, "Italian");
  //        menuPagerAdapter.AddFragment(spanish, "Spanish");
  //        viewPager.setAdapter(menuPagerAdapter);
  //    }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.foodmenu_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.addToCartItem:
/*
        Bundle bundle = new Bundle();
        bundle.putString("restaurant_id", restaurant_id);
        bundle.putInt("total_item", totalQuantity);
        Intent intent1 = new Intent(this, MyKart.class);
        intent1.putExtras(bundle);
        startActivity(intent1);*/

/*                        for (int i=0;i<menuResponseArrayList.size();i++)
                        {
                            if (menuResponseArrayList.get(i).getQuantityCount()>0) {
                                FMenuBean fMenuBean = new FMenuBean();
                                fMenuBean.setItem_name(menuResponseArrayList.get(i).getMenu_name());
                                fMenuBean.setItem_price(menuResponseArrayList.get(i).getMenu_price());
                                fMenuBean.setQuantity("" + menuResponseArrayList.get(i).getQuantityCount());
                                fMenuBeanArrayList.add(fMenuBean);
                                totalQuantity=totalQuantity+menuResponseArrayList.get(i).getQuantityCount();
                            }
                        }*/
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

            Intent intent = new Intent(FoodMenu.this, MyKart.class);
            intent.putExtra("menuList", arrayList);
            intent.putExtra("restaurant_id", restaurant_id);
            intent.putExtra("total_item", totalQuantity);


            startActivityForResult(intent, REQUEST_CODE);
          } else {
            Toast.makeText(FoodMenu.this, "Please select at least one menu item", Toast.LENGTH_LONG)
                .show();
          }
        }
        //                adapter.setUpFMenuListener(new FoodMenuAdapter.FMenuListener() {
        //                    @Override
        //                    public void getFoodMenuList(ArrayList<MenuResponse> arrayList) {
        //                        String s="";
        //                    }
        //                });

        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rl_filterImg:

        callCategoryListApi();
        break;

        }

  }





  private void setUpList() {
    filterBeanArrayList.add(new FilterBean("Most Popular", "7", false));
    filterBeanArrayList.add(new FilterBean("Breakfast Mains and Slides", "12", false));
    filterBeanArrayList.add(new FilterBean("Breakfast Value and Meals", "14", false));
    filterBeanArrayList.add(new FilterBean("Snack Wraps", "12", false));
    filterBeanArrayList.add(new FilterBean("Bagels", "4", false));
    filterBeanArrayList.add(new FilterBean("Quaker Oat So Simple Porridge", "2", false));
    filterBeanArrayList.add(new FilterBean("Sweet Snacks", "5", false));
    filterBeanArrayList.add(new FilterBean("McCaFe Drinks", "4", false));
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {
      try {
        ArrayList<FMenuBean> arrayList;
        arrayList = (ArrayList<FMenuBean>) data.getSerializableExtra("menu_list");
        for (int k = 0; k < fMenuBeanArrayList.size(); k++) {
          FMenuBean fMenuBean = new FMenuBean();
          fMenuBean.setItemCount(0);
          fMenuBean.setQuantity("" + 0);
          fMenuBean.setItem_name(fMenuBeanArrayList.get(k).getItem_name());
          fMenuBean.setItem_price(fMenuBeanArrayList.get(k).getItem_price());
          fMenuBean.setId(fMenuBeanArrayList.get(k).getId());
          fMenuBeanArrayList.set(k, fMenuBean);
        }
        for (int i = 0; i < arrayList.size(); i++) {
          for (int j = 0; j < fMenuBeanArrayList.size(); j++) {
            if (fMenuBeanArrayList.get(j).getId().equals(arrayList.get(i).getId())) {
              FMenuBean fMenuBean = new FMenuBean();
              fMenuBean.setItemCount(arrayList.get(i).getItemCount());
              fMenuBean.setQuantity(arrayList.get(i).getQuantity());
              fMenuBean.setItem_name(arrayList.get(i).getItem_name());
              fMenuBean.setItem_price(arrayList.get(i).getItem_price());
              fMenuBean.setId(arrayList.get(i).getId());
              fMenuBeanArrayList.set(j, fMenuBean);
            }
          }
        }
        adapter.notifyDataSetChanged();
      } catch (Exception e) {
        e.printStackTrace();
      }

      //Toast.makeText(this,"this is facebook login class",Toast.LENGTH_LONG).show();
    }
  }

  //    METHOD: TO SHOW Filter DIALOG
  private void showFilterDialog() {

    final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.fmenu_filter_dialog);
    dialog.setCancelable(true);
    dialog.setCanceledOnTouchOutside(true);

    Window window = dialog.getWindow();
    if (window != null) {
      window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

      WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
      lp.copyFrom(window.getAttributes());
      lp.width = CommonTask.getScreenDimen(FoodMenu.this)[0] - 150;
      lp.height = CommonTask.getScreenDimen(FoodMenu.this)[1] - 150;
      lp.gravity = Gravity.CENTER;

      window.setAttributes(lp);
    }

    //      RecyclerView Filter
    RecyclerView rvFilter = (RecyclerView) dialog.findViewById(R.id.recyclerVFilter);
    RelativeLayout relativeLayout = (RelativeLayout) dialog.findViewById(R.id.rl_filterRecycler);

    LinearLayoutManager layoutManager = new LinearLayoutManager(FoodMenu.this);
    rvFilter.setLayoutManager(layoutManager);

    final MenuFilterAdapter filterAdapter =
        new MenuFilterAdapter(FoodMenu.this, categoryListResponses);
    rvFilter.setAdapter(filterAdapter);

    filterAdapter.setUpFilterListener(new MenuFilterAdapter.FilterListener() {
      @Override public void onItemClick(View view, int pos) {
        CategoryListResponse categoryListResponse = categoryListResponses.get(pos);

        for (CategoryListResponse categoryListResponse1 : categoryListResponses) {
          if (categoryListResponse1.getCategory_name()
              .equalsIgnoreCase(categoryListResponse1.getCategory_name())) {

            //                        if (f_bean.getFilterName().equalsIgnoreCase("Most Popular")) {
            //                            tv_title.setText("Menu");
            //                        } else {
            //                            nameToolbar = bean.getFilterName();
            //                            tv_title.setText(nameToolbar);
            //                        }

            nameToolbar = categoryListResponse.getCategory_name();
            tv_title.setText(nameToolbar);
            //Api
            callCategoryMenuApi(nameToolbar);

            if (categoryListResponse1.isSelected()) {
              categoryListResponse1.setSelected(false);
            } else {
              categoryListResponse1.setSelected(true);
            }
          } else {
            categoryListResponse1.setSelected(false);
          }
        }

        filterAdapter.notifyDataSetChanged();
        dialog.dismiss();
      }
    });

    relativeLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
      }
    });

    dialog.show();
  }

  private void callCategoryListApi() {
    MyDialog.getInstance(this).showDialog();
    Retrofit retrofit = ApiClient.getClient();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    Call<CommonResponse> call = apiInterface.getCategoryListResult(
        SharedPreferenceWriter.getInstance(FoodMenu.this).getString(SharedPreferenceKey.token),
        restaurant_id);

    call.enqueue(new Callback<CommonResponse>() {
      @Override
      public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
        MyDialog.getInstance(FoodMenu.this).hideDialog();
        if (response.isSuccessful()) {
          if (response.body().getStatus().equals("SUCCESS")) {
            categoryListResponses = new ArrayList<CategoryListResponse>();
            for (int i = 0; i < response.body().getCategoryList().size(); i++) {
              CategoryListResponse categoryListResponse = new CategoryListResponse();
              categoryListResponse.setCategory_name(
                  response.body().getCategoryList().get(i).getCategory_name());
              categoryListResponse.setNumber(response.body().getCategoryList().get(i).getNumber());
              categoryListResponses.add(categoryListResponse);
            }
            showFilterDialog();
          } else {
            Toast.makeText(FoodMenu.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
          }
        }
      }

      @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
        MyDialog.getInstance(FoodMenu.this).hideDialog();
      }
    });
  }

  private void callCategoryMenuApi(String category) {
    MyDialog.getInstance(this).showDialog();
    Retrofit retrofit = ApiClient.getClient();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    Call<CommonResponse> call = apiInterface.getCategoryMenuResult(
        SharedPreferenceWriter.getInstance(FoodMenu.this).getString(SharedPreferenceKey.token),
        restaurant_id, category);

    call.enqueue(new Callback<CommonResponse>() {
      @Override
      public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
        MyDialog.getInstance(FoodMenu.this).hideDialog();
        if (response.isSuccessful()) {
          if (response.body().getStatus().equals("SUCCESS")) {
            menuResponseArrayList = new ArrayList<MenuResponse>();
            //fMenuBeanArrayList=new ArrayList<FMenuBean>();
            for (int i = 0; i < response.body().getCategoryMenu().size(); i++) {
              MenuResponse categoryMenuResponse = new MenuResponse();
              categoryMenuResponse.setId(response.body().getCategoryMenu().get(i).getId());
              categoryMenuResponse.setRestaurent_id(
                  response.body().getCategoryMenu().get(i).getRestaurent_id());
              categoryMenuResponse.setMenu_name(
                  response.body().getCategoryMenu().get(i).getMenu_name());
              categoryMenuResponse.setMenu_category(
                  response.body().getCategoryMenu().get(i).getMenu_category());
              categoryMenuResponse.setMenu_price(
                  response.body().getCategoryMenu().get(i).getMenu_price());
              categoryMenuResponse.setMenu_description(
                  response.body().getCategoryMenu().get(i).getMenu_description());
              categoryMenuResponse.setImage_url(
                  response.body().getCategoryMenu().get(i).getImage_url());
              menuResponseArrayList.add(categoryMenuResponse);

              //                            FMenuBean fMenuBean=new FMenuBean();
              //                            fMenuBean.setId(response.body().getCategoryMenu().get(i).getId());
              //                            fMenuBean.setItem_name(response.body().getCategoryMenu().get(i).getMenu_name());
              //                            fMenuBean.setItem_price(response.body().getCategoryMenu().get(i).getMenu_price());
              //                            fMenuBeanArrayList.add(fMenuBean);
            }
            adapter = new FoodMenuAdapter(FoodMenu.this, menuResponseArrayList, fMenuBeanArrayList);
            binding.recyclerV.setAdapter(adapter);
          } else {
            Toast.makeText(FoodMenu.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
          }
        }
      }

      @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
        MyDialog.getInstance(FoodMenu.this).hideDialog();
      }
    });
  }

  private void callMenuListApi() {
    MyDialog.getInstance(this).showDialog();
    Retrofit retrofit = ApiClient.getClient();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    Call<CommonResponse> call = apiInterface.getMenuListResult(
        SharedPreferenceWriter.getInstance(FoodMenu.this).getString(SharedPreferenceKey.token),
        restaurant_id);

    call.enqueue(new Callback<CommonResponse>() {
      @Override
      public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
        MyDialog.getInstance(FoodMenu.this).hideDialog();
        if (response.isSuccessful()) {
          if (response.body().getStatus().equals("SUCCESS")) {
            fMenuBeanArrayList = new ArrayList<FMenuBean>();
            menuResponseArrayList = new ArrayList<MenuResponse>();
            for (int i = 0; i < response.body().getMenuList().size(); i++) {
              MenuResponse menuResponse = new MenuResponse();
              menuResponse.setId(response.body().getMenuList().get(i).getId());
              menuResponse.setOwner_id(response.body().getMenuList().get(i).getOwner_id());
              menuResponse.setImage_url(response.body().getMenuList().get(i).getImage_url());
              menuResponse.setMenu_name(response.body().getMenuList().get(i).getMenu_name());
              menuResponse.setMenu_description(
                  response.body().getMenuList().get(i).getMenu_description());
              menuResponse.setMenu_price(response.body().getMenuList().get(i).getMenu_price());
              menuResponseArrayList.add(menuResponse);

              FMenuBean fMenuBean = new FMenuBean();
              fMenuBean.setId(response.body().getMenuList().get(i).getId());
              fMenuBean.setItem_name(response.body().getMenuList().get(i).getMenu_name());
              fMenuBean.setItem_price(response.body().getMenuList().get(i).getMenu_price());
              fMenuBean.setItemCount(0);
              fMenuBean.setQuantity("" + 0);

              fMenuBeanArrayList.add(fMenuBean);
            }
            adapter = new FoodMenuAdapter(FoodMenu.this, menuResponseArrayList, fMenuBeanArrayList);
            binding.recyclerV.setAdapter(adapter);

            //                        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.token, response.body().getLogin().getToken());
            //                        Intent intent = new Intent(getActivity(), MainActivity.class);
            //                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //                        startActivity(intent);
          } else {
            Toast.makeText(FoodMenu.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
          }
        }
      }

      @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
        MyDialog.getInstance(FoodMenu.this).hideDialog();
      }
    });
  }

  @Override public void getFoodIdCount(String id, int count) {
    for (int i = 0; i < fMenuBeanArrayList.size(); i++) {
      if (id.equals(fMenuBeanArrayList.get(i).getId())) {
        FMenuBean fMenuBean = new FMenuBean();
        fMenuBean.setItemCount(count);
        fMenuBean.setQuantity(String.valueOf(count));
        fMenuBeanArrayList.add(fMenuBean);
      }
    }
  }
}
