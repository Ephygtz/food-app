package com.wrx.quickeats.activities;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

/**
 * Created by mobulous51 on 7/10/17.
 */

public class MapForPayment extends AppCompatActivity
    implements OnMapReadyCallback, View.OnClickListener {

  Toolbar myToolbar;
  TextView click_next;
  TextView tv_title;
  private LatLng latLong;
  private GoogleMap mMap;
  MarkerOptions markerOptions = new MarkerOptions();
  ArrayList<FMenuBean> fMenuBeanArrayList;
  private String restaurant_id;
  private int total_item;
  private double grandToatal;
  String address = "";
  double latitude = 0.0, longitude = 0.0;
  private int totalItems = 0;
  private String itemNames;
  private String name;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mapforpayment);
    click_next = (TextView) findViewById(R.id.click_next);

    setUpToolbar();

    GPSTracker gpsTracker = new GPSTracker(this, this);

    try {
      latitude = gpsTracker.getLatitude();
      longitude = gpsTracker.getLongitude();
      fMenuBeanArrayList = (ArrayList<FMenuBean>) getIntent().getSerializableExtra("menuList");
      restaurant_id = getIntent().getStringExtra("restaurant_id");
      total_item = getIntent().getIntExtra("total_item", 0);
      grandToatal = getIntent().getDoubleExtra("grandTotal", 0);
      itemNames = getIntent().getStringExtra("item_names");
      name = getIntent().getStringExtra("name");
      //Toast.makeText(this, "grandTotal: " + grandTotal, Toast.LENGTH_LONG).show();

    } catch (Exception e) {
      e.printStackTrace();
    }
    click_next.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        final Geocoder geocoder;
        final List<Address> addresses;

        try {
          geocoder = new Geocoder(MapForPayment.this, Locale.getDefault());
          addresses = geocoder.getFromLocation(latitude, longitude,
              1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
          address = addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
          e.printStackTrace();
        }
        try {
          Intent intent = new Intent(MapForPayment.this, PaymentMode.class);
          intent.putExtra("menuList", fMenuBeanArrayList);
          intent.putExtra("restaurant_id", restaurant_id);
          intent.putExtra("total_item", total_item);
          intent.putExtra("grandTotal", grandToatal);
          intent.putExtra("name", name);
          intent.putExtra("address", address);
          intent.putExtra("latitude", String.valueOf(latitude));
          intent.putExtra("longitude", String.valueOf(longitude));
          startActivity(intent);
        } catch (Exception e) {
          e.printStackTrace();
        }
        Double gt = grandToatal;
        if (itemNames.endsWith(",")) itemNames = itemNames.substring(0, itemNames.length() - 1);
        //alertDialog(itemNames, String.valueOf(gt));
        setResult(1);

        try {

          String restId = restaurant_id;

          String totalItm = totalItems + "";

          String grndTotals = grandToatal + "";

          String names = name;

          String addre = address;

          callBookOrderApi(restId, totalItm, grndTotals, names, addre);
        } catch (JSONException e) {

          e.printStackTrace();
        }
      }
    });

    double lat = gpsTracker.getLatitude();
    double log = gpsTracker.getLongitude();

    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.description_map);
    mapFragment.getMapAsync(this);
    latLong = new LatLng(lat, log);
    //getSupportFragmentManager().beginTransaction().add(R.id.container, new MapFragment()).commit();

  }

  private void alertDialog(String itemName, String totalAmount) {
    //final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
    //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //dialog.setContentView(R.layout.info_dialog);
    //dialog.setCancelable(false);
    //dialog.setCanceledOnTouchOutside(true);

    //TextView item_text = (TextView) dialog.findViewById(R.id.item_text);
    //TextView payment_text = (TextView) dialog.findViewById(R.id.payment_text);
    //TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
    //TextView proceed = (TextView) dialog.findViewById(R.id.proceed);
    //LinearLayout ll_ok = (LinearLayout) dialog.findViewById(R.id.ll_ok);

    //item_text.setText("You ordered for items name (" + itemName + ").");
    //payment_text.setText("Make your payment ready of KES" + totalAmount + ".");
       /* ll_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                dialog.dismiss();

            }
        });*/
    //cancel.setOnClickListener(new View.OnClickListener() {
    //    @Override
    //    public void onClick(View v) {
    //        dialog.dismiss();
    //    }
    //});
    //
    //proceed.setOnClickListener(new View.OnClickListener() {
    //    @Override
    //    public void onClick(View v) {
    //        setResult(1);
    //        try {
    //            String restId = restaurant_id;
    //            String totalItm = totalItems + "";
    //            String grndTotals = grandToatal + "";
    //            String names = name;
    //            String addre = address;
    //            dialog.dismiss();
    //            callBookOrderApi(restId, totalItm, grndTotals, names, addre);
    //        } catch (JSONException e) {
    //            e.printStackTrace();
    //        }
    //
    //    }
    //});
    //
    //dialog.show();
  }

  private void callBookOrderApi(String restId, String totalItm, String grndTotals, String names,
      String addre) throws JSONException {
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
    jsonObject.put("rest_id", "" + restId);
    jsonObject.put("total_item", "" + totalItm);
    jsonObject.put("total_amount", "" + grndTotals);
    jsonObject.put("name", names);
    jsonObject.put("address", addre);


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
        MyDialog.getInstance(MapForPayment.this).hideDialog();
        if (response.isSuccessful()) {
          if (response.body().getStatus().equals("SUCCESS")) {

            if (!response.body().getOrderResponse().getOrder_no().equalsIgnoreCase("")) {

              SharedPreferenceWriter.getInstance(MapForPayment.this)
                  .writeStringValue(SharedPreferenceKey.order_id,
                      response.body().getOrderResponse().getOrder_no());
            } else {
              SharedPreferenceWriter.getInstance(MapForPayment.this)
                  .writeStringValue(SharedPreferenceKey.order_id, "");
            }
            SharedPreferenceWriter.getInstance(MapForPayment.this)
                .writeStringValue(SharedPreferenceKey.AMOUNT, "" + grandToatal);
            //alertDialog1();

            Intent intent = new Intent(MapForPayment.this, PaymentMode.class);

            intent.putExtra("menuList", fMenuBeanArrayList);

            intent.putExtra("restaurant_id", restaurant_id);

            intent.putExtra("total_item", total_item);

            intent.putExtra("grandTotal", grandToatal);

            intent.putExtra("name", name);

            intent.putExtra("address", address);

            startActivity(intent);


            //                        Intent intent = new Intent(UserAddress.this, MainActivity.class);
            //                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //                        startActivity(intent);
            //                        Toast.makeText(UserAddress.this, "Payment Successfully", Toast.LENGTH_LONG).show();

          } else {
            Toast.makeText(MapForPayment.this, response.body().getMessage(), Toast.LENGTH_LONG)
                .show();
          }
        }
      }

      @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
        MyDialog.getInstance(MapForPayment.this).hideDialog();
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
        Intent intent = new Intent(MapForPayment.this, PaymentMode.class);
        intent.putExtra("menuList", fMenuBeanArrayList);
        intent.putExtra("restaurant_id", restaurant_id);
        intent.putExtra("total_item", total_item);
        intent.putExtra("grandTotal", grandToatal);
        intent.putExtra("name", name);
        intent.putExtra("address", address);
        startActivity(intent);
      }
    });
    dialog.show();
  }

  private void setUpToolbar() {
    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    tv_title = (TextView) findViewById(R.id.tv_title);

    setSupportActionBar(myToolbar);
    if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

    tv_title.setVisibility(View.VISIBLE);
    tv_title.setText("Map Address");
    tv_title.setTextSize(20);

    myToolbar.setNavigationIcon(R.drawable.back);

    myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        onBackPressed();
      }
    });
  }

  @Override public void onClick(View v) {

  }

  @Override public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_res));
    markerOptions.position(latLong);
    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    mMap.addMarker(markerOptions).setDraggable(true);
    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLong, 10);
    mMap.animateCamera(cameraUpdate);
    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
      @Override public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub
        Log.d("System out", "onMarkerDragStart..."
            + arg0.getPosition().latitude
            + "..."
            + arg0.getPosition().longitude);
      }

      @SuppressWarnings("unchecked") @Override public void onMarkerDragEnd(Marker arg0) {
        // TODO Auto-generated method stub
        Log.d("System out", "onMarkerDragEnd..."
            + arg0.getPosition().latitude
            + "..."
            + arg0.getPosition().longitude);
        latitude = arg0.getPosition().latitude;
        longitude = arg0.getPosition().longitude;

        mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
        if (latitude != 0.0d && longitude != 0.0d) {
          address = getAddressss();
        }
      }

      @Override public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub
        Log.i("System out", "onMarkerDrag...");
      }
    });
  }

  public String getAddressss() {
    String addr = "";
    Geocoder geocoder;
    List<Address> addresses;
    geocoder = new Geocoder(this, Locale.getDefault());

    try {
      addresses = geocoder.getFromLocation(latitude, longitude,
          1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
      String address = addresses.get(0)
          .getAddressLine(
              0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
      String city = addresses.get(0).getLocality();
      String state = addresses.get(0).getAdminArea();
      String country = addresses.get(0).getCountryName();
      String postalCode = addresses.get(0).getPostalCode();
      String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
      if (address.length() > 0) {
        addr = address;
      } else {
        addr = knownName + "" + city + "" + state + "" + country + "-" + postalCode;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return addr;
  }
}
