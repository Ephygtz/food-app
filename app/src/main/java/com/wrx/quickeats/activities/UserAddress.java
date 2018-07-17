package com.wrx.quickeats.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wrx.quickeats.bean.FMenuBean;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.MyDialog;
import com.wrx.quickeats.util.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAddress extends AppCompatActivity {

  Button btn_done;
  Toolbar myToolbar;
  TextView tv_title;
  ArrayList<FMenuBean> fMenuBeanArrayList;

  String restaurant_id;
  String address = "";
  int total_item;
  double grandTotal;
  EditText name, house_no, society, city, zipcode;

  String itmesNames = "";
  int totalItems = 0;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    setContentView(R.layout.fragment_user_address);
    name = (EditText) findViewById(R.id.name);
    house_no = (EditText) findViewById(R.id.house_no);
    //society = (EditText) findViewById(R.id.society);
    //zipcode = (EditText) findViewById(R.id.zipcode);
    city = (EditText) findViewById(R.id.city);
    btn_done = (Button) findViewById(R.id.btn_done);
    tv_title = (TextView) findViewById(R.id.tv_title);
    myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    tv_title.setVisibility(View.VISIBLE);
    tv_title.setText("Address");
    tv_title.setTextSize(22);

    myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    myToolbar.setNavigationIcon(R.drawable.back);
    setSupportActionBar(myToolbar);
    if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

    try {
      fMenuBeanArrayList = (ArrayList<FMenuBean>) getIntent().getSerializableExtra("menuList");
      restaurant_id = getIntent().getStringExtra("restaurant_id");
      total_item = getIntent().getIntExtra("total_item", 0);
      grandTotal = getIntent().getDoubleExtra("grandTotal", 0);
      //Toast.makeText(this, "grandTotal: " + grandTotal, Toast.LENGTH_LONG).show();

    } catch (Exception e) {
      e.printStackTrace();
    }

    myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        onBackPressed();
      }
    });

    btn_done.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (validationforEditext()) {

          address = house_no.getText().toString()
              //+ society.getText().toString().trim()
              + city.getText().toString();
                   /* // getFragmentManager().beginTransaction().replace(R.id.search_container, new PaymentMode()).addToBackStack("").commit();
                    Intent intent = new Intent(UserAddress.this, PaymentMode.class);
                    intent.putExtra("menuList", fMenuBeanArrayList);
                    intent.putExtra("restaurant_id", restaurant_id);
                    intent.putExtra("total_item", total_item);
                    intent.putExtra("grandTotal", grandTotal);
                    intent.putExtra("name", name.getText().toString().trim());
                    intent.putExtra("address", address);
                    startActivity(intent);*/
          if (itmesNames.endsWith(",")) {
            itmesNames = itmesNames.substring(0, itmesNames.length() - 1);
          }
          //alertDialog(itmesNames, String.valueOf(grandTotal));
          setResult(1);

          try {

            if(address!=null && !address.equals("")){

              callBookOrderApi();

            }else{

              Toast.makeText(UserAddress.this,"please fill all field",Toast.LENGTH_LONG).show();

            }

          } catch (JSONException e) {

            e.printStackTrace();

          }

        }
      }
    });

    if (fMenuBeanArrayList.size() > 0) {
      for (int i = 0; i < fMenuBeanArrayList.size(); i++) {
        totalItems = totalItems + fMenuBeanArrayList.get(i).getItemCount();
        itmesNames = itmesNames + fMenuBeanArrayList.get(i).getItem_name() + ",";
      }
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.address_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.openmap:
        if (!name.getText().toString().trim().equals("")) {
          Intent intent = new Intent(UserAddress.this, MapForPayment.class);
          intent.putExtra("menuList", fMenuBeanArrayList);
          intent.putExtra("restaurant_id", restaurant_id);
          intent.putExtra("total_item", total_item);
          intent.putExtra("grandTotal", grandTotal);
          intent.putExtra("name", name.getText().toString());
          intent.putExtra("item_names", itmesNames);
          startActivity(intent);
        } else {
          Toast.makeText(this, "Name fill compulsary", Toast.LENGTH_LONG).show();
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private boolean validateName() {
    boolean flag = true;
    if (name.getText().toString().trim().equals("")) {
      Toast.makeText(UserAddress.this, "Please enter name", Toast.LENGTH_LONG).show();
      return false;
    }
    return flag;
  }

  private boolean validationforEditext() {
    boolean flag = true;
    if (name.getText().toString().trim().equals("")) {
      flag = false;
      Toast.makeText(UserAddress.this, "Please enter name ", Toast.LENGTH_LONG).show();
    } else if (house_no.getText().toString().trim().equals("")) {
      flag = false;
      Toast.makeText(UserAddress.this, "Please enter house no. ", Toast.LENGTH_LONG).show();
    }

    //else if (society.getText().toString().trim().equals("")) {
    //  flag = false;
    //  Toast.makeText(UserAddress.this, "Please enter society ", Toast.LENGTH_LONG).show();
    //}

    else if (city.getText().toString().trim().equals("")) {
      flag = false;
      Toast.makeText(UserAddress.this, "Please enter city ", Toast.LENGTH_LONG).show();
      //} else if (zipcode.getText().toString().trim().equals("")) {
      //  //flag=false;
      //  //Toast.makeText(UserAddress.this,"Please enter zipcode",Toast.LENGTH_LONG).show();
      //}
    }
    return flag;
  }

  private void alertDialog(String itemName, String totalAmount) {
    final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.info_dialog);
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(true);

    TextView item_text = (TextView) dialog.findViewById(R.id.item_text);
    TextView payment_text = (TextView) dialog.findViewById(R.id.payment_text);
    TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
    TextView proceed = (TextView) dialog.findViewById(R.id.proceed);
    LinearLayout ll_ok = (LinearLayout) dialog.findViewById(R.id.ll_ok);

    item_text.setText("You ordered for items name (" + itemName + ").");
    float total = Float.parseFloat(totalAmount);
    total += 100;
    totalAmount = String.valueOf(total);
    payment_text.setText("Make your payment ready of KES" + totalAmount + ".");
       /* ll_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                dialog.dismiss();

            }
        });*/
    cancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
      }
    });

    proceed.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        setResult(1);
        try {
          if (address != null && !address.equals("")) {
            dialog.dismiss();
            callBookOrderApi();
          } else {
            Toast.makeText(UserAddress.this, "please fill all field", Toast.LENGTH_LONG).show();
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
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
    jsonObject.put("token",
        SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.token));
    jsonObject.put("rest_id", "" + restaurant_id);
    jsonObject.put("total_item", "" + totalItems);
    grandTotal+= 100.0;
    jsonObject.put("total_amount", "" + grandTotal);
    jsonObject.put("name", name);
    jsonObject.put("address", address);


       /* if (checkbox_cash.isChecked())
            jsonObject.put("payment_mode","Cash On Delivery");
        else
            jsonObject.put("payment_mode","Card");
        if (address != null) {
            jsonObject.put("map_location", "");
        } else {
            jsonObject.put("map_location", String.valueOf(lat) + "," + String.valueOf(lon));
        }*/

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
        MyDialog.getInstance(UserAddress.this).hideDialog();
        if (response.isSuccessful()) {
          if (response.body().getStatus().equals("SUCCESS")) {

            if (!response.body().getOrderResponse().getOrder_no().equalsIgnoreCase("")) {

              SharedPreferenceWriter.getInstance(UserAddress.this)
                  .writeStringValue(SharedPreferenceKey.order_id,
                      response.body().getOrderResponse().getOrder_no());
            } else {
              SharedPreferenceWriter.getInstance(UserAddress.this)
                  .writeStringValue(SharedPreferenceKey.order_id, "");
            }
            SharedPreferenceWriter.getInstance(UserAddress.this)
                .writeStringValue(SharedPreferenceKey.AMOUNT, "" + grandTotal);
            //alertDialog1();
            Intent intent = new Intent(UserAddress.this,PaymentMode.class);

            intent.putExtra("menuList", fMenuBeanArrayList);

            intent.putExtra("restaurant_id", restaurant_id);

            intent.putExtra("total_item", total_item);

            intent.putExtra("grandTotal", grandTotal);

            intent.putExtra("name", name.getText().toString().trim());

            intent.putExtra("address", address);

            startActivity(intent);

            //                        Intent intent = new Intent(UserAddress.this, MainActivity.class);
            //                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //                        startActivity(intent);
            //                        Toast.makeText(UserAddress.this, "Payment Successfully", Toast.LENGTH_LONG).show();

          } else {
            Toast.makeText(UserAddress.this, response.body().getMessage(), Toast.LENGTH_LONG)
                .show();
          }
        }
      }

      @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
        MyDialog.getInstance(UserAddress.this).hideDialog();
      }
    });
  }

  private void alertDialog1() {
    final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.info_proceed_dialog);
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(true);

    LinearLayout llproceed = (LinearLayout) dialog.findViewById(R.id.ll_proceed);
    TextView proceed = (TextView) dialog.findViewById(R.id.proceed);

    llproceed.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
        Intent intent = new Intent(UserAddress.this, PaymentMode.class);
        intent.putExtra("menuList", fMenuBeanArrayList);
        intent.putExtra("restaurant_id", restaurant_id);
        intent.putExtra("total_item", total_item);
        intent.putExtra("grandTotal", grandTotal);
        intent.putExtra("name", name.getText().toString().trim());
        intent.putExtra("address", address);
        startActivity(intent);
      }
    });

    dialog.show();
  }
}
