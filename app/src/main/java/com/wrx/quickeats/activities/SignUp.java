package com.wrx.quickeats.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.util.Post;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.retrofit.MyDialog;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

  TextView tv_loginagain;
  ImageView iv_back;
  Button btn_next;
  public EditText email, phone_no1, pass, confirm_pass;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

    btn_next = (Button) findViewById(R.id.btn_next);
    //tv_loginagain = (TextView) findViewById(R.id.tv_loginagain);
    //iv_back = (ImageView) findViewById(R.id.iv_back);
    email = (EditText) findViewById(R.id.email);
    phone_no1 = (EditText) findViewById(R.id.phone_no);
    pass=(EditText)findViewById(R.id.pass);
    //confirm_pass=(EditText)findViewById(R.id.confirm_pass);

    btn_next.setOnClickListener(this);
    //tv_loginagain.setOnClickListener(this);
    //iv_back.setOnClickListener(this);
    // setUpMapData();
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_next:
        if (checkSignup())

        {
          if (phone_no1.getText().toString().trim().equals(phone_no1.getText().toString().trim())) {
            callchkUserApi();
          } else {
            Toast.makeText(SignUp.this, "Please enter same password and confirm password",
                Toast.LENGTH_LONG).show();
          }
        }

        break;

      //case R.id.tv_loginagain:
      //
      //  onBackPressed();
      //
      //  break;

      case R.id.iv_back:

        onBackPressed();

        break;
    }
  }

  private void callchkUserApi() {
    MyDialog.getInstance(this).showDialog();
    Retrofit retrofit = ApiClient.getClient();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    Call<CommonResponse> call =
        apiInterface.getchkUserResult(email.getText().toString(), phone_no1.getText().toString());

    call.enqueue(new Callback<CommonResponse>() {
      @Override
      public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
        MyDialog.getInstance(SignUp.this).hideDialog();
        if (response.isSuccessful()) {
          Toast.makeText(SignUp.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
          if (response.body().getStatus().equals("SUCCESS")) {
            callVerificationCodeApi();
          }
        }
      }

      @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
        MyDialog.getInstance(SignUp.this).hideDialog();
      }
    });
  }

  private void callVerificationCodeApi() {
    MyDialog.getInstance(this).showDialog();
    Retrofit retrofit = ApiClient.getClient();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    Call<CommonResponse> call =
        apiInterface.getverificationCodeResult(phone_no1.getText().toString());

    call.enqueue(new Callback<CommonResponse>() {
      @Override
      public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
        MyDialog.getInstance(SignUp.this).hideDialog();
        if (response.isSuccessful()) {
          Toast.makeText(SignUp.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
          if (response.body().getStatus().equals("SUCCESS")) {
            // SharedPreferenceWriter.getInstance(SignUp.this).writeStringValue(SharedPreferenceKey.token, response.body().getRegister().getToken());
            Intent intent = new Intent(SignUp.this, MainActivity.class);
//            sendSMS(response.body().getVerificationCode(),phone_no1.getText().toString());
//            intent.putExtra("verificationCode", response.body().getVerificationCode());
            intent.putExtra("email", email.getText().toString());
            intent.putExtra("mobile_number", phone_no1.getText().toString());
            intent.putExtra("password", pass.getText().toString());
            startActivity(intent);
//            Toast.makeText(SignUp.this, response.body().getVerificationCode(), Toast.LENGTH_LONG)
//                .show();
          }
        }
      }

      @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
        MyDialog.getInstance(SignUp.this).hideDialog();
      }
    });
  }

  private void sendSMS(String verificationCode,String phone) {

    JSONObject jsonObject = new JSONObject();

    try {


      JSONArray recipients = new JSONArray();

      recipients.put(phone);

      jsonObject.put("body","Your QuickEATS verification code is "+ verificationCode);

      jsonObject.put("from","QuickEATS");

      jsonObject.put("to",recipients);


      Log.e("sent data", jsonObject.toString());

    } catch (JSONException e) {

      e.printStackTrace();

    }

    Post.PostData("https://api.clxcommunications.com/xms/v1/quickeats12/batches", jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

      @Override

      public void onResponse(JSONObject response) {

        Log.e("Response",response.toString());



      }

    });

  }


  private Map<String, RequestBody> setUpMapData() {
    Map<String, RequestBody> fields = new HashMap<>();
    RequestBody phone_no =
        RequestBody.create(MediaType.parse("text/plain"), phone_no1.getText().toString());
    RequestBody email_id =
        RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());

    RequestBody pwd = RequestBody.create(MediaType.parse("text/plain"), pass.getText().toString());

    //RequestBody con_pwd =
    //    RequestBody.create(MediaType.parse("text/plain"), confirm_pass.getText().toString());

    RequestBody devicetoken = RequestBody.create(MediaType.parse("text/plain"),
        SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.device_token));
    RequestBody devicetype = RequestBody.create(MediaType.parse("text/plain"), "android");
    fields.put("device_token", devicetoken);
    fields.put("device_type", devicetype);
    fields.put("mobile_number", phone_no);
    fields.put("email", email_id);
    //fields.put("password", pwd);

    return fields;
  }

  private boolean checkSignup() {
    boolean flag = true;
    if (email.getText().toString().equals("")) {
      Toast.makeText(SignUp.this, "Please enter email id", Toast.LENGTH_LONG).show();
      flag = false;
    } else if (phone_no1.getText().toString().equals("")) {
      Toast.makeText(SignUp.this, "Please enter phone no", Toast.LENGTH_LONG).show();
      flag = false;
    }
    else if (pass.getText().toString().equals(""))
    {
        Toast.makeText(SignUp.this,"Please enter password",Toast.LENGTH_LONG).show();
        flag=false;
    }

    //else if (confirm_pass.getText().toString().equals(""))
    //{
    //    Toast.makeText(SignUp.this,"Please enter confirm password",Toast.LENGTH_LONG).show();
    //    flag=false;
    //}
    return flag;
  }
}
