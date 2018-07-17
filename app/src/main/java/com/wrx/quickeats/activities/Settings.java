package com.wrx.quickeats.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.MyDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    private Toolbar myToolbar;
    TextView tv_title, tv_change_pass;
    LinearLayout ll_about, ll_help, ll_contact;
    ToggleButton toggleButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_change_pass = (TextView) findViewById(R.id.tv_change_pass);
        ll_about = (LinearLayout) findViewById(R.id.ll_about);
        ll_help = (LinearLayout) findViewById(R.id.ll_help);
        ll_contact = (LinearLayout) findViewById(R.id.ll_contact);
        toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);


        if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.NOTIFICATION_STATUS).equals("ON")) {
            toggleButton1.setChecked(true);
        } else {
            toggleButton1.setChecked(false);
        }

        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    callChangeStatusApi("ON");
                } else {
                    callChangeStatusApi("OFF");
                }
            }
        });
        tv_change_pass.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        ll_help.setOnClickListener(this);
        ll_contact.setOnClickListener(this);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("Settings");

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);


        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.back);


        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_change_pass:
                Intent intent = new Intent(this, ChangePassword.class);
                startActivity(intent);

                break;
            case R.id.ll_about:
                Intent intent1 = new Intent(this, WebviewActivity.class);
                intent1.putExtra("url", "http://mobulous.co.in/restaurent/CustomerServices/about");
                intent1.putExtra("toolText", "About Us");
                startActivity(intent1);
                break;
            case R.id.ll_help:
                Intent intent2 = new Intent(this, WebviewActivity.class);
                intent2.putExtra("url", "http://mobulous.co.in/restaurent/CustomerServices/help");
                intent2.putExtra("toolText", "Help");
                startActivity(intent2);
                break;
            case R.id.ll_contact:
                Intent intent3 = new Intent(this, ContactUs.class);
                startActivity(intent3);
                break;

        }


    }

    private void callChangeStatusApi(final String status) {
        MyDialog.getInstance(Settings.this).showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<CommonResponse> call = apiInterface.getchangeStatusResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.token), status);

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                MyDialog.getInstance(Settings.this).hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        SharedPreferenceWriter.getInstance(Settings.this).writeStringValue(SharedPreferenceKey.NOTIFICATION_STATUS, status);
                        // Toast.makeText(Settings.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Settings.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                MyDialog.getInstance(Settings.this).hideDialog();
            }
        });
    }
}
