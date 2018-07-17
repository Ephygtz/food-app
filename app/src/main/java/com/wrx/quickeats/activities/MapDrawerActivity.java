package com.wrx.quickeats.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.wrx.quickeats.adapter.MapDrawerAdap;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.util.BeanClass;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.RestaurantResponse;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.MyDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapDrawerActivity extends AppCompatActivity {

    RecyclerView rv_map_drawer;
    ArrayList<BeanClass> map_restro = new ArrayList<BeanClass>();
    BeanClass beanClass = new BeanClass();
    MapDrawerAdap mapDrawerAdap;
    private TextView tv_title;
    Toolbar myToolbar;
    String restaurantId="";
    ArrayList<RestaurantResponse> homeResponseArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_drawer);
        All_id();

        restaurantId=getIntent().getStringExtra("restaurantId");

        for (int i = 0; i < 10; i++) {

            beanClass.setMap_restro_name("Carlifornia Grill");
            beanClass.setMap_restro_dish("American Flare");

            map_restro.add(beanClass);
        }

//        mapDrawerAdap = new MapDrawerAdap(this, map_restro);
//        rv_map_drawer.setAdapter(mapDrawerAdap);

//        mapDrawerAdap.onItemClickListener(new MapDrawerAdap.MyClickListner() {
//            @Override
//            public void OnCardClick(int position, View v) {
//
//
//                //getSupportFragmentManager().beginTransaction().replace(R.id.search_container, new Description()).addToBackStack("").commit();
//                startActivity(new Intent(MapDrawerActivity.this,Description.class));
//
//            }
//        });
        callrestDetailApi(restaurantId);
    }



    private void All_id() {

        rv_map_drawer = (RecyclerView) findViewById(R.id.rv_map_drawer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_map_drawer.setLayoutManager(linearLayoutManager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("Restaurant");
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);


        myToolbar.setNavigationIcon(R.drawable.back);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MapDrawerActivity.this.onBackPressed();

            }
        });

    }

    public void callrestDetailApi(String resId) {
        MyDialog.getInstance(this).showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<CommonResponse> call = apiInterface.getrestDetailResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.token), resId);

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                MyDialog.getInstance(MapDrawerActivity.this).hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        homeResponseArrayList = (ArrayList<RestaurantResponse>) response.body().getRestDetail();
//                        marker_list.clear();
//                        LatLng latLng;
//                        for (int i = 0; i < restaurantResponseArrayList.size(); i++) {
//                            latLng = new LatLng(Double.parseDouble(restaurantResponseArrayList.get(i).getLatitude()), Double.parseDouble(restaurantResponseArrayList.get(i).getLongitude()));
//                            marker_list.add(latLng);
//                        }
//                        onMapReady(mMap);
                        mapDrawerAdap = new MapDrawerAdap(MapDrawerActivity.this, homeResponseArrayList);
                        rv_map_drawer.setAdapter(mapDrawerAdap);
                        //Toast.makeText(MapDrawerActivity.this, "success", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                MyDialog.getInstance(MapDrawerActivity.this).hideDialog();
            }
        });
    }

}
