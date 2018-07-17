package com.wrx.quickeats.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wrx.quickeats.R;
import com.wrx.quickeats.fragments.MapFragment;

public class SearchMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);

        getSupportFragmentManager().beginTransaction().add(R.id.search_container, new MapFragment()).commit();

    }

}
