package com.wrx.quickeats.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.wrx.quickeats.R;
import com.wrx.quickeats.adapter.MyOrderAdap;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.bean.MyOrderBean;
import com.wrx.quickeats.bean.MyOrderResponse;
import com.wrx.quickeats.database.SharedPreferenceWriter;
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
 * A simple {@link Fragment} subclass.
 */
public class MyOrder extends Fragment {

  private Toolbar myToolbar;
  private TextView tv_title;
  private View view;
  private RecyclerView rv_myorder;
  private ArrayList<BeanClass> order = new ArrayList<BeanClass>();
  private BeanClass beanClass = new BeanClass();
  private MyOrderAdap myOrderAdap;
  ArrayList<MyOrderBean> myOrderBeanArrayList;
  List<MyOrderResponse> orderSummryBeanArrayList;
  //public static boolean openApp=false;

  Handler handler = new Handler();
  Runnable runnable = new Runnable() {
    @Override public void run() {
      callMyOrderApi();
      Log.e("inside runnable", "Running My Order thread");
    }
  };

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_my_order, container, false);

    rv_myorder = (RecyclerView) view.findViewById(R.id.rv_myorder);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

    rv_myorder.setLayoutManager(linearLayoutManager);
    callMyOrderApi();
    //openApp=true;
    SharedPreferenceWriter.getInstance(getActivity())
        .writeBooleanValue(SharedPreferenceKey.appOpenOrNOt, true);
    LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
        .registerReceiver(messageReciever, new IntentFilter("Push"));
    handler.postDelayed(runnable, 10000);

    //        for (int i = 0; i < 2; i++) {
    //
    //            beanClass.setOrder_price("$115");
    //            beanClass.setOrder_time("14-July-2017");
    //            beanClass.setOrder_quantity("1");
    //            beanClass.setOrder_dish("Noodles, Sandwich");
    //            beanClass.setOrder_revie_time("3:10PM");
    //            beanClass.setOrder_pack_time("3:38PM");
    //            beanClass.setOrder_del_time("3:45PM");
    //
    //            order.add(beanClass);
    //        }

    return view;
  }

  private BroadcastReceiver messageReciever = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      if (!intent.getStringExtra("type").equals("")) callMyOrderApi();
    }
  };

  @Override public void onResume() {
    super.onResume();
    //openApp=true;
    SharedPreferenceWriter.getInstance(getActivity())
        .writeBooleanValue(SharedPreferenceKey.appOpenOrNOt, true);
    handler.postDelayed(runnable, 10000);
  }

  @Override public void onPause() {
    super.onPause();
    //openApp=false;
    SharedPreferenceWriter.getInstance(getActivity())
        .writeBooleanValue(SharedPreferenceKey.appOpenOrNOt, false);
    if (handler!=null){
      if (runnable!= null){

        handler.removeCallbacks(runnable);
      }
    }
  }

  @Override public void onStop() {
    super.onStop();
    //openApp=false;
    SharedPreferenceWriter.getInstance(getActivity())
        .writeBooleanValue(SharedPreferenceKey.appOpenOrNOt, false);
    if (handler!=null){
      if (runnable!= null){

        handler.removeCallbacks(runnable);
      }
    }
  }

  private void callMyOrderApi() {
    MyDialog.getInstance(getActivity()).showDialog();
    Retrofit retrofit = ApiClient.getClient();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    Call<CommonResponse> call = apiInterface.getMyOrderResult(
        SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.token));

    call.enqueue(new Callback<CommonResponse>() {
      @Override
      public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
        MyDialog.getInstance(getActivity()).hideDialog();
        if (response.isSuccessful()) {
          if (response.body().getStatus().equals("SUCCESS")) {
            // Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
            //to get all response of
            orderSummryBeanArrayList = response.body().getMyOrders();

            Log.e("orderlist", orderSummryBeanArrayList.toString());
            myOrderAdap = new MyOrderAdap(getActivity(), orderSummryBeanArrayList);
            rv_myorder.setAdapter(myOrderAdap);
          } else {
            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
          }
        }
      }

      @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
        MyDialog.getInstance(getActivity()).hideDialog();
      }
    });
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    //openApp=false;
    SharedPreferenceWriter.getInstance(getActivity())
        .writeBooleanValue(SharedPreferenceKey.appOpenOrNOt, false);
    if (handler!=null){
      if (runnable!= null){

        handler.removeCallbacks(runnable);
      }
    }
  }
}
