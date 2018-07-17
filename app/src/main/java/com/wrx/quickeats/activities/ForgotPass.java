package com.wrx.quickeats.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.retrofit.MyDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForgotPass extends AppCompatActivity  implements View.OnClickListener{

    TextView tv_cancel;
    Button send_button;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this,R.layout.activity_forgot_pass);

        tv_cancel=(TextView)findViewById(R.id.tv_cancel);
        send_button=(Button)findViewById(R.id.send_button);
        email=(EditText)findViewById(R.id.email);

        tv_cancel.setOnClickListener(this);
        send_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_cancel:

                onBackPressed();
                break;

            case R.id.send_button:
                if (email.getText().toString().equals(""))
                {
                    Toast.makeText(this,"Please enter email id",Toast.LENGTH_LONG).show();
                }else
                callForgotPasswordApi();
                break;


        }

    }
    private void callForgotPasswordApi()
    {
        MyDialog.getInstance(this).showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface=  retrofit.create(ApiInterface.class);
        Call<CommonResponse> call=apiInterface.getForgotResult(email.getText().toString());

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                MyDialog.getInstance(ForgotPass.this).hideDialog();
                if (response.isSuccessful())
                {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        startActivity(new Intent(ForgotPass.this,ResetPass.class));
                    }else
                    {
                        Toast.makeText(ForgotPass.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                MyDialog.getInstance(ForgotPass.this).hideDialog();
            }
        });
    }
}
