package com.wrx.quickeats.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

/**
 * Created by mobulous51 on 8/12/17.
 */

public class ChangePassword extends AppCompatActivity implements View.OnClickListener{

    EditText new_password,confirm_password;
    Button submit;
    ImageView back_button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        new_password=(EditText)findViewById(R.id.new_password);
        confirm_password=(EditText)findViewById(R.id.confirm_password);
        submit=(Button)findViewById(R.id.submit);
        back_button=(ImageView)findViewById(R.id.back_button);

        submit.setOnClickListener(this);
        back_button.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                if (checkLogin()) {
                    if (new_password.getText().toString().trim().equals(confirm_password.getText().toString().trim()))
                        callForgotPasswordApi();
                    else
                        Toast.makeText(ChangePassword.this,"Please enter same new password and confirm password",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.back_button:
                finish();
                break;
        }

    }
    private void callForgotPasswordApi()
    {
        MyDialog.getInstance(this).showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface=  retrofit.create(ApiInterface.class);
        Call<CommonResponse> call=apiInterface.getChangePasswordResult(SharedPreferenceWriter.getInstance(ChangePassword.this).getString(SharedPreferenceKey.token),new_password.getText().toString());

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                MyDialog.getInstance(ChangePassword.this).hideDialog();
                if (response.isSuccessful())
                {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        Intent intent=new Intent(ChangePassword.this,Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(ChangePassword.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(ChangePassword.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                MyDialog.getInstance(ChangePassword.this).hideDialog();
            }
        });
    }

    private boolean checkLogin()
    {
        boolean flag=true;
        if (new_password.getText().toString().equals(""))
        {
            Toast.makeText(ChangePassword.this,"Please enter new password",Toast.LENGTH_LONG).show();
            flag=false;
        }else if (confirm_password.getText().toString().equals(""))
        {
            Toast.makeText(ChangePassword.this,"Please enter confirm password",Toast.LENGTH_LONG).show();
            flag=false;
        }
        return flag;
    }
}
