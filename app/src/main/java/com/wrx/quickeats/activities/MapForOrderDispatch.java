package com.wrx.quickeats.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.MyDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Dev-PC on 1/27/2018.
 */

public class MapForOrderDispatch extends FragmentActivity
    implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

  private static LatLng destination = new LatLng(0.0, 0.0);
  private static LatLng currentLocation;
  private Bundle bundle = null;
  private double rest_lat_ = 0.0;
  private double rest_lng_ = 0.0;
  private double del_boy_lat_ = 0.0;
  private double del_boy_lng_ = 0.0;
  final static int RQS_1 = 1;

  private GoogleApiClient client;
  private GoogleMap mMap;
  Marker mCurrLocationMarker;
  private GoogleApiClient mGoogleApiClient;

  ProgressDialog dialogcategory;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map_order_dispatch);

    Intent in = getIntent();
    Bundle b = in.getExtras();

    String orderId = b.getString("orderId");
    String restorentId = b.getString("restorentId");

    getdelLatlongApi(orderId, restorentId);      //SERVICE HIT...


           /* AlarmManager am=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, DeliveryBoytStatus.class);
            //intent.putExtra(ONE_TIME, Boolean.TRUE);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, 40000,40000, pi);*/

  }

  private void getdelLatlongApi(String orderId, String restorentId) {

    try {

      MyDialog.getInstance(this).showDialog();
      Retrofit retrofit = ApiClient.getClient();
      ApiInterface apiInterface = retrofit.create(ApiInterface.class);

      Call<CommonResponse> call = apiInterface.getdelLatlong(
          SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.token), orderId,
          restorentId);

      call.enqueue(new Callback<CommonResponse>() {
        @Override
        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
          MyDialog.getInstance(MapForOrderDispatch.this).hideDialog();
          if (response.isSuccessful()) {
            if (response.body().getStatus().equals("SUCCESS")) {

              Log.w("rest_lat", response.body().getLatlongResponse().getRest_lat());
              Log.w("rest_long", response.body().getLatlongResponse().getRest_long());
              Log.w("del_lat", response.body().getLatlongResponse().getDel_lat());
              Log.w("del_long", response.body().getLatlongResponse().getDel_long());

              if (response.body().getLatlongResponse().getRest_lat() != null && !response.body()
                  .getLatlongResponse()
                  .getRest_lat()
                  .equalsIgnoreCase("")) {
                rest_lat_ = Double.parseDouble(response.body().getLatlongResponse().getRest_lat());
              }
              if (response.body().getLatlongResponse().getRest_long() != null && !response.body()
                  .getLatlongResponse()
                  .getRest_long()
                  .equalsIgnoreCase("")) {
                rest_lng_ = Double.parseDouble(response.body().getLatlongResponse().getRest_long());
              }

              if (response.body().getLatlongResponse().getDel_lat() != null && !response.body()
                  .getLatlongResponse()
                  .getDel_lat()
                  .equalsIgnoreCase("")) {
                del_boy_lat_ =
                    Double.parseDouble(response.body().getLatlongResponse().getDel_lat());
              }
              if (response.body().getLatlongResponse().getDel_long() != null && !response.body()
                  .getLatlongResponse()
                  .getDel_long()
                  .equalsIgnoreCase("")) {
                del_boy_lng_ =
                    Double.parseDouble(response.body().getLatlongResponse().getRest_long());
              }

              destination = new LatLng(rest_lat_, rest_lng_);
              currentLocation = new LatLng(del_boy_lat_, del_boy_lng_);

              SupportMapFragment mapFragment =
                  (SupportMapFragment) getSupportFragmentManager().findFragmentById(
                      R.id.description_map);
              mapFragment.getMapAsync(MapForOrderDispatch.this);

              //                            client = new GoogleApiClient.Builder(MapForOrderDispatch.this).addApi(AppIndex.API).build();

              Toast.makeText(MapForOrderDispatch.this, "my order Successfully", Toast.LENGTH_LONG)
                  .show();
            } else {
              Toast.makeText(MapForOrderDispatch.this, response.body().getMessage(),
                  Toast.LENGTH_LONG).show();
            }
          }
        }

        @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
          MyDialog.getInstance(MapForOrderDispatch.this).hideDialog();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public void onMapReady(GoogleMap googleMap) {

    mMap = googleMap;

    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(destination);
    markerOptions.title("Destination");
    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_res));
    mCurrLocationMarker = mMap.addMarker(markerOptions);
    mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

    LocationManager lm = (LocationManager) MapForOrderDispatch.this.getSystemService(
        MapForOrderDispatch.LOCATION_SERVICE);
    if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || !lm.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER)) {
      // Build the alert dialog
      buildGoogleApiClient();
      AlertDialog.Builder builder = new AlertDialog.Builder(MapForOrderDispatch.this);
      builder.setTitle("Location Services Not Active");
      builder.setCancelable(false);

      builder.setMessage("Please enable Location Services and GPS");
      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialogInterface, int i) {
          Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          startActivity(intent);
        }
      });
      Dialog alertDialog = builder.create();
      alertDialog.setCanceledOnTouchOutside(false);
      alertDialog.show();
    }

    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapForOrderDispatch.this);

    if (status != ConnectionResult.SUCCESS) {

      int requestCode = 10;
      Dialog dialog =
          GooglePlayServicesUtil.getErrorDialog(status, MapForOrderDispatch.this, requestCode);
      dialog.show();
    } else {

      buildGoogleApiClient();
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
          != PackageManager.PERMISSION_GRANTED
          && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
          != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return;
      }
      googleMap.setMyLocationEnabled(true);

      LocationManager locationManager =
          (LocationManager) MapForOrderDispatch.this.getSystemService(Context.LOCATION_SERVICE);

      Criteria criteria = new Criteria();

      String provider = locationManager.getBestProvider(criteria, true);

      android.location.Location location = locationManager.getLastKnownLocation(provider);

      if (location != null) {
        onLocationChanged(location);
      }

      if (ActivityCompat.checkSelfPermission(MapForOrderDispatch.this,
          Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
          && ActivityCompat.checkSelfPermission(MapForOrderDispatch.this,
          Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

      }

      locationManager.requestLocationUpdates(provider, 50000, 0, (LocationListener) this);
    }

    getDirection();     //start fetching url from google
  }

  public Action getIndexApiAction() {
    Thing object = new Thing.Builder().setName("GetDirection Page")
        .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
        .build();
    return new Action.Builder(Action.TYPE_VIEW).setObject(object)
        .setActionStatus(Action.STATUS_TYPE_COMPLETED)
        .build();
  }

  @Override public void onStart() {
    super.onStart();

    if (client != null) {
      client.connect();
      AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }
  }

  @Override public void onStop() {
    super.onStop();

    if (client != null) {
      AppIndex.AppIndexApi.end(client, getIndexApiAction());
      client.disconnect();
    }
  }

  protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
    mGoogleApiClient.connect();
  }

  @Override public void onLocationChanged(Location location) {

    //        try {
    //
    //            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    //
    //            currentLocation = latLng;
    //
    //            MarkerOptions markerOptions = new MarkerOptions();
    //            markerOptions.position(currentLocation);
    //            markerOptions.title("Current");
    //            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_s));
    //            mCurrLocationMarker = mMap.addMarker(markerOptions);
    //
    //            mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
    //
    //            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f), null);
    //
    //
    //        } catch (Exception e) {
    //
    //        }

  }

  @Override public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  @Override public void onProviderEnabled(String provider) {

  }

  @Override public void onProviderDisabled(String provider) {

  }

  @Override public void onConnected(@Nullable Bundle bundle) {

  }

  @Override public void onConnectionSuspended(int i) {

  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  public void getDirection() {

      /*  dialogcategory = new ProgressDialog(MapForOrderDispatch.this);
        dialogcategory.setMessage("Fetching Route please wait...");
        dialogcategory.setCancelable(true);
        dialogcategory.show();*/

    try {
      String url = getUrl();
      new FetchUrl().execute(url);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String getUrl() {

    String str_origin = "origin=" + currentLocation.latitude + "," + currentLocation.longitude;

    String str_dest = "destination=" + destination.latitude + "," + destination.longitude;

    String sensor = "sensor=false";

    String parameters = str_origin + "&" + str_dest + "&" + sensor;

    String output = "json";

    String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

    return url;
  }

  private class FetchUrl extends AsyncTask<String, Void, String> {

    @Override protected String doInBackground(String... url) {

      // For storing data from web service
      String data = "";

      try {
        // Fetching the data from web service
        data = downloadUrl(url[0]);
        Log.d("Background Task data", data.toString());
      } catch (Exception e) {
        Log.d("Background Task", e.toString());
      }
      return data;
    }

    @Override protected void onPostExecute(String result) {
      super.onPostExecute(result);

      ParserTask parserTask = new ParserTask();

      // Invokes the thread for parsing the JSON data
      parserTask.execute(result);
    }
  }

  private String downloadUrl(String strUrl) throws IOException {

    String data = "";
    InputStream iStream = null;
    HttpURLConnection urlConnection = null;
    try {
      URL url = new URL(strUrl);

      urlConnection = (HttpURLConnection) url.openConnection();

      urlConnection.connect();

      iStream = urlConnection.getInputStream();

      BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

      StringBuffer sb = new StringBuffer();

      String line = " ";
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }

      data = sb.toString();
      Log.d("downloadUrl", data.toString());
      br.close();
    } catch (Exception e) {
      Log.d("Exception", e.toString());
    } finally {
      iStream.close();
      urlConnection.disconnect();
    }
    return data;
  }

  private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    // Parsing the data in non-ui thread
    @Override protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

      JSONObject jObject;
      List<List<HashMap<String, String>>> routes = null;

      try {
        jObject = new JSONObject(jsonData[0]);
        Log.d("ParserTask", jsonData[0].toString());
        DataParser parser = new DataParser();
        Log.d("ParserTask", parser.toString());

        // Starts parsing data
        routes = parser.parse(jObject);
        Log.d("ParserTask", "Executing routes");
        Log.d("ParserTask", routes.toString());
      } catch (Exception e) {
        Log.d("ParserTask", e.toString());
        e.printStackTrace();
      }
      return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override protected void onPostExecute(List<List<HashMap<String, String>>> result) {
      ArrayList<LatLng> points;
      PolylineOptions lineOptions = null;

      System.out.println(" result size " + result.size());
      // Traversing through all the routes
      for (int i = 0; i < result.size(); i++) {
        points = new ArrayList<>();
        lineOptions = new PolylineOptions();

        // Fetching i-th route
        List<HashMap<String, String>> path = result.get(i);

        // Fetching all the points in i-th route
        for (int j = 0; j < path.size(); j++) {
          HashMap<String, String> point = path.get(j);

          double lat = Double.parseDouble(point.get("lat"));
          double lng = Double.parseDouble(point.get("lng"));
          LatLng position = new LatLng(lat, lng);

          points.add(position);
        }

        // Adding all the points in the route to LineOptions
        lineOptions.addAll(points);
        lineOptions.width(15);
        lineOptions.color(Color.BLUE);

        Log.d("onPostExecute", "onPostExecute lineoptions decoded");
      }

      if (lineOptions != null) {

        mMap.addPolyline(lineOptions);

        System.out.println(" null ");
      } else {
        Log.d("onPostExecute", "without Polylines drawn");
      }

      dialogcategory.dismiss();
    }
  }
}
