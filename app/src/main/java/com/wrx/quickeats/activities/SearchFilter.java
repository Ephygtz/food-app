package com.wrx.quickeats.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wrx.quickeats.bean.HomeResponse;
import com.wrx.quickeats.R;

import java.util.ArrayList;

public class SearchFilter extends AppCompatActivity {

    ImageView iv_close,iv_yes;
    TextView food_type;
    Spinner spinner;
    EditText res_name;
    ArrayList<HomeResponse> arrayList;
    ArrayList<String> spinnerList;
    FilterInterface filterInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_filter);
        food_type = (TextView) findViewById(R.id.food_type);
        res_name = (EditText) findViewById(R.id.res_name);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_yes = (ImageView) findViewById(R.id.iv_yes);
        spinner = (Spinner) findViewById(R.id.spinner);

        filterInterface= (FilterInterface) MainActivity.getFragment();
        spinnerList = new ArrayList<>();
        try {
            arrayList = (ArrayList<HomeResponse>) getIntent().getSerializableExtra("home_arrylist");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if (arrayList!=null) {
            for (HomeResponse homeResponse : arrayList) {
                spinnerList.add(homeResponse.getRest_type());
            }
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(SearchFilter.this, android.R.layout.simple_list_item_1, spinnerList);
        spinner.setAdapter(arrayAdapter);

        food_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                spinner.performClick();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                food_type.setText(parent.getItemAtPosition(position).toString());
                //spinner.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        iv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterInterface.getFilterTypeOrName(food_type.getText().toString(),res_name.getText().toString());
                finish();
            }
        });
    }

    public interface FilterInterface{
        public void getFilterTypeOrName(String resType,String resName);
    }
}

