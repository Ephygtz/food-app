package com.wrx.quickeats.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class ResetPass extends AppCompatActivity {

    ImageView iv_back;
    Button done;
    EditText new_pass,confirm_pass;
    private Toolbar myToolbar;
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        done=(Button) findViewById(R.id.done);
        //generated_pass=(EditText)findViewById(R.id.generated_pass);
        new_pass=(EditText)findViewById(R.id.new_pass);
        confirm_pass=(EditText)findViewById(R.id.confirm_pass);
        tv_title = (TextView) findViewById(R.id.tv_title);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

       tv_title.setVisibility(View.VISIBLE);
       tv_title.setText("Change Password");



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

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()) {
                    if (new_pass.getText().toString().trim().equals(confirm_pass.getText().toString().trim()))
                    callForgotPasswordApi();
                    else
                        Toast.makeText(ResetPass.this,"Please enter same new password and confirm password",Toast.LENGTH_LONG).show();

                }
            }
        });


    }
    private void callForgotPasswordApi()
    {
        MyDialog.getInstance(this).showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface=  retrofit.create(ApiInterface.class);
        Call<CommonResponse> call=apiInterface.getChangePasswordResult(SharedPreferenceWriter.getInstance(ResetPass.this).getString(SharedPreferenceKey.token),new_pass.getText().toString());

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                MyDialog.getInstance(ResetPass.this).hideDialog();
                if (response.isSuccessful())
                {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        Intent intent=new Intent(ResetPass.this,Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(ResetPass.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(ResetPass.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                MyDialog.getInstance(ResetPass.this).hideDialog();
            }
        });
    }

    private boolean checkLogin()
    {
        boolean flag=true;
         if (new_pass.getText().toString().equals(""))
        {
            Toast.makeText(ResetPass.this,"Please enter new password",Toast.LENGTH_LONG).show();
            flag=false;
        }else if (confirm_pass.getText().toString().equals(""))
        {
            Toast.makeText(ResetPass.this,"Please enter confirm password",Toast.LENGTH_LONG).show();
            flag=false;
        }
        return flag;
    }

}
