package com.wrx.quickeats.activities;


import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;

import android.view.View;

import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.wrx.quickeats.bean.FMenuBean;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.databinding.FragmentPaymentModeBinding;
import com.wrx.quickeats.dialogs.PaymentProceedDialog;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.MyDialog;
import com.wrx.quickeats.util.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PaymentMode extends AppCompatActivity {

  TextView tv_title, tv_mpesa,tvCreditCard;
  Toolbar myToolbar;
  Button btn_paymentmode;
  ArrayList<FMenuBean> fMenuBeanArrayList;
  String restaurant_id, name = "", address = "";
  int total_item;
  double grandTotal;
  int totalItems = 0;
  double latitude = 0.0, longitude = 0.0;
  CheckBox checkbox_cash;
  String itmesNames = "";
  CardView cardview_mpesa, cardview_creditcard;
  LinearLayout llmpesa;
  boolean checkcolor = false;
  String isCheckBoxChecked = "";
  String ismpesaproceed = "";
  String phoneNo = "";
  String currency = "";
  String whichPaymentModeSelected = "";

  FragmentPaymentModeBinding binding;

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.fragment_payment_mode);

    //checkbox_cash = (CheckBox) findViewById(R.id.checkbox_cash);
    cardview_mpesa = (CardView) findViewById(R.id.cardview_mpesa);
    cardview_creditcard = (CardView) findViewById(R.id.cardview_creditcard);
    tv_mpesa = (TextView) findViewById(R.id.tv_mpesa);
    tvCreditCard = (TextView) findViewById(R.id.tvCreditCard);
    //llmpesa=(LinearLayout)findViewById(R.id.llmpesa);
    btn_paymentmode = (Button) findViewById(R.id.btn_paymentmode);

    binding.tvTitle1.setVisibility(View.VISIBLE);
    binding.tvTitle1.setText("Payment Mode");

    binding.myToolbar21.setNavigationIcon(R.drawable.back);
    binding.myToolbar21.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });


    try {
      try {
        fMenuBeanArrayList = (ArrayList<FMenuBean>) getIntent().getSerializableExtra("menuList");
        restaurant_id = getIntent().getStringExtra("restaurant_id");
        total_item = getIntent().getIntExtra("total_item", 0);
        grandTotal = getIntent().getDoubleExtra("grandTotal", 0);
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));

      } catch (Exception e) {
        e.printStackTrace();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }


    //checkbox_cash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    //  @Override
    //  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    //    if (isChecked) {
    //      if (itmesNames.endsWith(","))
    //        itmesNames = itmesNames.substring(0, itmesNames.length() - 1);
    //      alertDialogSelectedOnlyCod(itmesNames, String.valueOf(grandTotal));
    //      isCheckBoxChecked = "yes";
    //      cardview_mpesa.setBackground(null);
    //      tv_mpesa.setTextColor(getResources().getColor(R.color.gray));
    //      cardview_creditcard.setBackground(null);
    //      tvCreditCard.setTextColor(getResources().getColor(R.color.gray));
    //      whichPaymentModeSelected="";
    //
    //    } else {
    //      isCheckBoxChecked = "no";
    //
    //    }
    //  }
    //});
    cardview_mpesa.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        cardview_mpesa.setBackground(getResources().getDrawable(R.drawable.quantity_no));
        whichPaymentModeSelected = "mpessa";
        tv_mpesa.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.colorAccent));
        cardview_creditcard.setBackground(null);
        tvCreditCard.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.gray));
        //checkbox_cash.setChecked(false);

      }
    });
    cardview_creditcard.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        cardview_creditcard.setBackground(getResources().getDrawable(R.drawable.quantity_no));
        whichPaymentModeSelected = "creditcard";
        tvCreditCard.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
        cardview_mpesa.setBackground(null);
        tv_mpesa.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.gray));
        //checkbox_cash.setChecked(false);


      }
    });


    /*    cardview_mpesa.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if(checkcolor==false)
                {
                    cardview_mpesa.setBackground(getResources().getDrawable(R.drawable.quantity_no));
                    checkcolor=true;
                    tv_mpesa.setTextColor(getColor(R.color.colorAccent));

                    ismpesaproceed="yes";

                }
                else {
                    cardview_mpesa.setBackground(getResources().getDrawable(R.drawable.contact_shape));
                    checkcolor=false;
                    tv_mpesa.setTextColor(getColor(R.color.light_gray));

                    ismpesaproceed="no";


                }

            }
        });*/

    binding.btnPaymentmode.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (whichPaymentModeSelected.equals("mpessa"))
        {
          new PaymentProceedDialog(PaymentMode.this).show();

        } else if (whichPaymentModeSelected.equals("creditcard")) {
          Intent intent=new Intent(PaymentMode.this,CreditCard.class);
          startActivity(intent);

        } else if (isCheckBoxChecked.equals("yes")) {
          cardview_mpesa.setBackground(null);
          whichPaymentModeSelected = "";
          tv_mpesa.setTextColor(getResources().getColor(R.color.gray));
          //cardview_creditcard.setBackground(null);
          //tvCreditCard.setTextColor(getResources().getColor(R.color.gray));

          setResult(1);
          try {
            paymentApi();
          } catch (JSONException e) {
            e.printStackTrace();
          }
        } else {
          //Toast.makeText(PaymentMode.this, "Please select payment mode", Toast.LENGTH_LONG).show();
          alertDialogOnlyProceed();
        }
      }
    });

    //        Log.d("dfad",address);
    try {
      if (address.equals("")) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(PaymentMode.this, Locale.getDefault());
        try {
          try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0);
          } catch (IOException e) {
            e.printStackTrace();
            address = addresses.get(0).getAddressLine(0);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }


      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    Log.d("System out", "onMarkerDragEnd Address..." + address);

    if (fMenuBeanArrayList.size() > 0) {
      for (int i = 0; i < fMenuBeanArrayList.size(); i++) {
        totalItems = totalItems + fMenuBeanArrayList.get(i).getItemCount();
        itmesNames = itmesNames + fMenuBeanArrayList.get(i).getItem_name() + ",";
      }
    }

       /* binding.btnPaymentmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               *//* setResult(1);
                try {
                    callBookOrderApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*//*

                  setResult(1);
                try {
                    paymentApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });*/

  }

  private void alertDialogOnlyProceed() {

    final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    // dialog.setContentView(R.layout.info_dialog);
    dialog.setContentView(R.layout.info_dont_select_anything_dialog);

    dialog.setCancelable(false);

    LinearLayout ll_ok = (LinearLayout) dialog.findViewById(R.id.ll_ok);

    ll_ok.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        dialog.dismiss();
      }
    });

    dialog.show();

  }

  private void alertDialogSelectedOnlyCod(String itemName, String totalAmount) {
    final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    // dialog.setContentView(R.layout.info_dialog);
    dialog.setContentView(R.layout.info_select_only_cod);

    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(true);

    TextView item_text = (TextView) dialog.findViewById(R.id.item_text);
    TextView payment_text = (TextView) dialog.findViewById(R.id.payment_text);
    //TextView ok = (TextView) dialog.findViewById(R.id.ok);
    LinearLayout ll_ok = (LinearLayout) dialog.findViewById(R.id.ll_ok);

    item_text.setText("You ordered for items name (" + itemName + ").");
    payment_text.setText("Make your payment ready of KES" + totalAmount + ".");
    ll_ok.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        dialog.dismiss();
      }
    });

    dialog.show();
  }

  private void callBookOrderApi() throws JSONException {
    GPSTracker gpsTracker = new GPSTracker(this, this);
    double lat = 0.0, lon = 0.0;
    try {
      lat = gpsTracker.getLatitude();
      lon = gpsTracker.getLongitude();
    } catch (Exception e) {
      e.printStackTrace();
    }
    MyDialog.getInstance(this).showDialog();
    Retrofit retrofit = ApiClient.getClient();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("token", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.token));
    jsonObject.put("rest_id", "" + restaurant_id);
    jsonObject.put("total_item", "" + totalItems);
    jsonObject.put("total_amount", "" + grandTotal);
    jsonObject.put("name", name);
    jsonObject.put("address", address);
    if (checkbox_cash.isChecked())
      jsonObject.put("payment_mode", "Cash On Delivery");
    else
      jsonObject.put("payment_mode", "Card");
    if (address != null) {
      jsonObject.put("map_location", "");
    } else {
      jsonObject.put("map_location", String.valueOf(lat) + "," + String.valueOf(lon));
    }
    JSONArray jsonArray = new JSONArray();
    for (int i = 0; i < fMenuBeanArrayList.size(); i++) {
      JSONObject jsonObject1 = new JSONObject();
      jsonObject1.put("item_name", fMenuBeanArrayList.get(i).getItem_name());
      jsonObject1.put("item_price", fMenuBeanArrayList.get(i).getItem_price());
      jsonObject1.put("quantity", fMenuBeanArrayList.get(i).getQuantity());
      jsonArray.put(i, jsonObject1);

    }
    jsonObject.put("items", jsonArray);

    MediaType JSON = MediaType.parse("application/json");
    //    sending json object
    RequestBody body = RequestBody.create(JSON, jsonObject.toString());
    Call<CommonResponse> call = apiInterface.getBookOrderResult(body);
    call.enqueue(new Callback<CommonResponse>() {
      @Override
      public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
        MyDialog.getInstance(PaymentMode.this).hideDialog();
        if (response.isSuccessful()) {
          if (response.body().getStatus().equals("SUCCESS")) {

            Intent intent = new Intent(PaymentMode.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(PaymentMode.this, "Payment Successfully", Toast.LENGTH_LONG).show();

          } else {
            Toast.makeText(PaymentMode.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
          }
        }
      }

      @Override
      public void onFailure(Call<CommonResponse> call, Throwable t) {
        MyDialog.getInstance(PaymentMode.this).hideDialog();
      }
    });
  }

  private void paymentApi() throws JSONException {

    MyDialog.getInstance(this).showDialog();
    Retrofit retrofit = ApiClient.getClient();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);

    Call<CommonResponse> call = apiInterface.payment(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.token),
        SharedPreferenceWriter.getInstance(PaymentMode.this).getString(SharedPreferenceKey.order_id),
        "COD", "", "", "");

    call.enqueue(new Callback<CommonResponse>() {
      @Override
      public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
        MyDialog.getInstance(PaymentMode.this).hideDialog();
        if (response.isSuccessful()) {
          if (response.body().getStatus().equals("SUCCESS")) {
            //alertDialogcodAndproceed();
            // Toast.makeText(PaymentMode.this, "Payment Successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(PaymentMode.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(PaymentMode.this, "Payment Successfully", Toast.LENGTH_LONG).show();

          } else {
            Toast.makeText(PaymentMode.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
          }
        }
      }

      @Override
      public void onFailure(Call<CommonResponse> call, Throwable t) {
        MyDialog.getInstance(PaymentMode.this).hideDialog();
      }
    });
  }

  private void alertDialogcodAndproceed() {

    final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    // dialog.setContentView(R.layout.info_dialog);
    dialog.setContentView(R.layout.info_select_cod_and_proceed_dialog);

    dialog.setCancelable(false);

    LinearLayout ll_ok = (LinearLayout) dialog.findViewById(R.id.ll_ok);


    ll_ok.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        dialog.dismiss();
      }
    });

    dialog.show();

  }


}