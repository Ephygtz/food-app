package com.wrx.quickeats.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.MyDialog;
import com.wrx.quickeats.util.Generate256Hash;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by mobulous55 on 23/1/18.
 */

public class PaymentBackground extends Activity
{
    MyDialog myDialog;
    String sid="", vid="", secretkey="", hash="";
    private String transcationId="";
    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_background);
        sid=getIntent().getStringExtra("sid");
        vid=getIntent().getStringExtra("vid");
        secretkey=getIntent().getStringExtra("secretkey");
        String dataString=sid+vid;
        try {
            Date date=new Date();
            Log.i("Date", dateFormat.format(date));
            hash= Generate256Hash.hashMac(secretkey, dataString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        transctMobilePaymentService(hash);
    }

    private void transctMobilePaymentService(final String hashId){

        myDialog=MyDialog.getInstance(PaymentBackground.this);
        if(!myDialog.isShowing())
        {
            myDialog.showDialog();
        }
        Retrofit retrofit = ApiClient.getClientPaymentMpesa();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<CommonResponse> call = apiInterface.transactMobileMoney(
                sid,
                vid,
                hashId);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
//                MyDialog.getInstance(PaymentBackground.this).hideDialog();
                if (response.isSuccessful()) {
                    switch (response.body().getStatus())
                    {
                        case "bdi6p2yy76etrs":  // pending request
                            transctMobilePaymentService(hash);
                            break;

                        case "aei7p7yrx4ae34":  // success request
                            if(!myDialog.isShowing())
                            {
                                myDialog.showDialog();
                            }
                            transcationId=response.body().getTxncd();
                            paymentApi();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                MyDialog.getInstance(PaymentBackground.this).hideDialog();
            }
        });
    }

    private void paymentApi(){

        try {
            Date date=new Date();
            MyDialog.getInstance(this).showDialog();
            Retrofit retrofit = ApiClient.getClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            Call<CommonResponse> call = apiInterface.payment(
                    SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.token),
                    SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.order_id),
                    "MPESA",
                    transcationId, "MPESA", dateFormat.format(date));

            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    MyDialog.getInstance(PaymentBackground.this).hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("SUCCESS"))
                        {

                            Intent intent = new Intent(PaymentBackground.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            Toast.makeText(PaymentBackground.this, "Payment Successfully", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(PaymentBackground.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    MyDialog.getInstance(PaymentBackground.this).hideDialog();
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
