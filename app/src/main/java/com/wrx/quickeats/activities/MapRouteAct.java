package com.wrx.quickeats.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.databinding.ActivityMapRouteBinding;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.util.SharedPreferenceKey;

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

public class MapRouteAct extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {

    ActivityMapRouteBinding binding;

    Handler handler=new Handler();
    private double rest_lat_ = 0.0;
    private double rest_lng_ = 0.0;
    private double del_boy_lat_ = 0.0;
    private double del_boy_lng_ = 0.0;
    private static LatLng destination = new LatLng(0.0, 0.0);
    private static LatLng currentLocation;
    private static LatLng delLatng;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;

    int REQUEST_CHECK_SETTINGS = 100;
    Location mLastLocation;
    private static final int REQUEST_CODE_LOCATION = 000;
    private String restorentId;
    private String orderId;
    private GoogleMap mMap;
    private Marker mCurrLocationMarker;
    Toolbar myToolbar;
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            getdelLatlongApi(orderId, restorentId);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MapRouteAct.this, R.layout.activity_map_route);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
       /* tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("Address");
        tv_title.setTextSize(22);*/
        myToolbar.setNavigationIcon(R.drawable.back);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        Intent in = getIntent();
        Bundle b = in.getExtras();

        orderId = b.getString("orderId");
        restorentId = b.getString("restorentId");
        loadGoogleApi();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.description_map);
        mapFragment.getMapAsync(MapRouteAct.this);
    }


    //method to load google api
    public void loadGoogleApi()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(MapRouteAct.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(MapRouteAct.this)
                .addOnConnectionFailedListener(MapRouteAct.this)
                .build();
        mGoogleApiClient.connect();
        createLocationRequest();
    }

    //method to create location request and set its priorities
    private void createLocationRequest()
    {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(5 * 1000);
    }
    private void loadLoaction()
    {
        if(checkrequestLocPermission())
        {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation == null)
            {
                createLocationRequest();
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, MapRouteAct.this);

            }
            else
            {
                currentLocation=new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                getdelLatlongApi(orderId,restorentId);

            }
        }
        else
        {
            requestPermission();
        }
    }

    // map callbacks
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if(checkrequestLocPermission())
        {
            mMap.setMyLocationEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }


    }


    //    google api client callbacks
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //initialize the pending result and locationRequest
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        if(mGoogleApiClient!=null)
        {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(mGoogleApiClient!=null)
        {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (location != null)
        {
            Log.i("Location Changed", ""+mLastLocation.getLatitude()+":"+mLastLocation.getLongitude());
            currentLocation=new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            getdelLatlongApi(orderId,restorentId);

        }
        else
        {
            loadGoogleApi();
            Toast.makeText(MapRouteAct.this, "GPS is still disabled.", Toast.LENGTH_SHORT).show();
        }
    }

//    gps enable result
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult)
    {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode())
        {
            case LocationSettingsStatusCodes.SUCCESS:// GPS is already enabled no need of dialog
                loadLoaction();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED://  Location settings are not satisfied. Show the user a dialog
                try
                {
                    // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                    status.startResolutionForResult(MapRouteAct.this, REQUEST_CHECK_SETTINGS);
                }
                catch (IntentSender.SendIntentException e)
                {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    //GPS permission start
    public boolean checkrequestLocPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private void requestPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
        {
            Toast.makeText(MapRouteAct.this, "Current Location needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // success!
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                else
                {
                    mGoogleApiClient.connect();
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if(mLastLocation==null)
                    {
                        createLocationRequest();
                    }
                    else
                    {
                        currentLocation=new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        getdelLatlongApi(orderId,restorentId);
                        Log.i("on request Location", ""+mLastLocation.getLatitude()+":"+mLastLocation.getLongitude());
                    }
                }
            }
            else
            {
                requestPermission();
                Toast.makeText(MapRouteAct.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //GPS permission ends
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {
                loadLoaction();
                Toast.makeText(getApplicationContext(), "GPS is enabled now.", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //    server response

    private void getdelLatlongApi(String orderId, String restorentId) {

        try {

//            MyDialog.getInstance(this).showDialog();
            Retrofit retrofit = ApiClient.getClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            Call<CommonResponse> call = apiInterface.getdelLatlong(
                    SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.token), orderId, restorentId);

            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
//                    MyDialog.getInstance(MapRouteAct.this).hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("SUCCESS")) {

                            Log.w("rest_lat", response.body().getLatlongResponse().getRest_lat());
                            Log.w("rest_long", response.body().getLatlongResponse().getRest_long());
                            Log.w("del_lat", response.body().getLatlongResponse().getDel_lat());
                            Log.w("del_long", response.body().getLatlongResponse().getDel_long());

                            if (response.body().getLatlongResponse().getRest_lat() != null && !response.body().getLatlongResponse().getRest_lat().equalsIgnoreCase("")) {
                                rest_lat_ = Double.parseDouble(response.body().getLatlongResponse().getRest_lat());
                            }
                            if (response.body().getLatlongResponse().getRest_long() != null && !response.body().getLatlongResponse().getRest_long().equalsIgnoreCase("")) {
                                rest_lng_ = Double.parseDouble(response.body().getLatlongResponse().getRest_long());
                            }

                            if (response.body().getLatlongResponse().getDel_lat() != null && !response.body().getLatlongResponse().getDel_lat().equalsIgnoreCase("")) {
                                del_boy_lat_ = Double.parseDouble(response.body().getLatlongResponse().getDel_lat());
                            }
                            if (response.body().getLatlongResponse().getDel_long() != null && !response.body().getLatlongResponse().getDel_long().equalsIgnoreCase("")) {
                                del_boy_lng_ = Double.parseDouble(response.body().getLatlongResponse().getRest_long());
                            }

                            destination = new LatLng(rest_lat_, rest_lng_);
                            delLatng=new LatLng(del_boy_lat_, del_boy_lng_);

                            mMap.clear();

                            MarkerOptions homeMarker = new MarkerOptions();
                            homeMarker.position(currentLocation);
                            homeMarker.title("Source");
                            homeMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.home_locationnew));
                            mCurrLocationMarker = mMap.addMarker(homeMarker);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

                            MarkerOptions markerOptions1 = new MarkerOptions();
                            markerOptions1.position(delLatng);
                            markerOptions1.title("Delivery Boy ");
                            markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.user_locationnew));
                            mCurrLocationMarker = mMap.addMarker(markerOptions1);
//                            mMap.moveCamera(CameraUpdateFactory.newLatLng(delLatng));

                            MarkerOptions markerOptions2 = new MarkerOptions();
                            markerOptions2.position(destination);
                            markerOptions2.title("Destination");
                            markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_res));
                            mCurrLocationMarker = mMap.addMarker(markerOptions2);
//                            mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));


                            getDirection();
                            handler.postDelayed(runnable, 10000);

//                            client = new GoogleApiClient.Builder(MapForOrderDispatch.this).addApi(AppIndex.API).build();
                        } else {
                            Toast.makeText(MapRouteAct.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
        //            MyDialog.getInstance(MapRouteAct.this).hideDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

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
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
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

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null)
        {
            if(runnable!=null)
            {
                handler.removeCallbacks(runnable);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(handler!=null)
        {
            if(runnable!=null)
            {
                handler.removeCallbacks(runnable);
            }
        }
    }
}
