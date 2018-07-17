package com.wrx.quickeats.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.retrofit.MyDialog;
import com.wrx.quickeats.util.SharedPreferenceKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by mobulous51 on 8/12/17.
 */

public class ContactUs extends AppCompatActivity {

    ImageView back_button;
    EditText title,description;
    Button send_button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        back_button= (ImageView) findViewById(R.id.back_button);
        title=(EditText)findViewById(R.id.title);
        description=(EditText)findViewById(R.id.description);
        send_button=(Button)findViewById(R.id.send_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().equals("")){
                    Toast.makeText(ContactUs.this,"Please fill title",Toast.LENGTH_LONG).show();
                }else if (description.getText().toString().equals(""))
                {
                    Toast.makeText(ContactUs.this,"Please fill description",Toast.LENGTH_LONG).show();
                }else {
                    callContactApi();
                }
            }
        });
    }

    private void callContactApi() {
        MyDialog.getInstance(ContactUs.this).showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<CommonResponse> call = apiInterface.getContactResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.token),"User",title.getText().toString(),description.getText().toString());

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                MyDialog.getInstance(ContactUs.this).hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        //contactInterface.getMessage("success");
                        finish();
                        Toast.makeText(ContactUs.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ContactUs.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                MyDialog.getInstance(ContactUs.this).hideDialog();
            }
        });
    }
}
