package com.wrx.quickeats.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wrx.quickeats.R;


public class Description extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    View view;
    MarkerOptions markerOptions = new MarkerOptions();
    private GoogleMap mMap;
    private LatLng latLong;
    LinearLayout tv_menu;
    private Toolbar tool_bar;
    String image, rest_name, rest_type, rest_description, lat, log, restaurant_id;
    ImageView imgView;
    TextView tv_order_restro, tv_dishes, description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_description);

        All_id();

        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        try {
            restaurant_id = getIntent().getStringExtra("restaurant_id");
            image = getIntent().getStringExtra("image");
            rest_name = getIntent().getStringExtra("rest_name");
            rest_type = getIntent().getStringExtra("rest_type");
            rest_description = getIntent().getStringExtra("rest_description");
            lat = getIntent().getStringExtra("lat");
            log = getIntent().getStringExtra("log");

        } catch (Exception e) {
            e.printStackTrace();
        }
        AQuery aQuery = new AQuery(this);
        aQuery.id(imgView).image(image, false, false, 0, R.drawable.image);
        tv_order_restro.setText(rest_name);
        tv_dishes.setText(rest_type);
        description.setText(rest_description);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.description_map);
        mapFragment.getMapAsync(this);
        try {
            if (!lat.equals("") && !log.equals(""))
                latLong = new LatLng(Double.parseDouble(lat), Double.parseDouble(log));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void All_id() {

        tv_menu = (LinearLayout) findViewById(R.id.tv_menu);
        tv_menu.setOnClickListener(this);
        tool_bar = (Toolbar) findViewById(R.id.tool_bar);
        tool_bar.setNavigationIcon(R.drawable.back);

        imgView = (ImageView) findViewById(R.id.imgView);
        tv_order_restro = (TextView) findViewById(R.id.tv_order_restro);
        tv_dishes = (TextView) findViewById(R.id.tv_dishes);
        description = (TextView) findViewById(R.id.description);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_res));
            markerOptions.position(latLong);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.addMarker(markerOptions);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLong, 10);
            mMap.animateCamera(cameraUpdate);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_menu:
                Intent intent = new Intent(Description.this, FoodMenu.class);
                intent.putExtra("restaurant_id", restaurant_id);
                startActivity(intent);
                //getFragmentManager().beginTransaction().replace(R.id.search_container, new FoodMenu()).addToBackStack("").commit();
                break;

        }

    }
}
