package com.wrx.quickeats.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import com.wrx.quickeats.adapter.MYFavAdap;
import com.wrx.quickeats.R;

import com.wrx.quickeats.util.BeanClass;

public class MyFav extends AppCompatActivity {

    private Toolbar my_toolbar;
    TextView tv_title;
    RecyclerView rv_myfav;
    ArrayList<BeanClass> fav= new ArrayList<BeanClass>();
    BeanClass beanClass= new BeanClass();
    MYFavAdap myFavAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fav);


        All();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rv_myfav.setLayoutManager(linearLayoutManager);



//        for (int i = 0; i < 4; i++) {
//
//            beanClass.setFav_restro("California Grill");
//            beanClass.setFave_dish("Spanish");
//
//            fav.add(beanClass);
//        }
//
//        myFavAdap = new MYFavAdap(this, fav);
//        rv_myfav.setAdapter(myFavAdap);


    }


    private void All(){

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("My Favourite");

        my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        rv_myfav = (RecyclerView)findViewById(R.id.rv_myfav);

        setSupportActionBar(my_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        my_toolbar.setNavigationIcon(R.drawable.back);


        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


    }

}
