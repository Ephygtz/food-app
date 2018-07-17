package com.wrx.quickeats.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import com.wrx.quickeats.util.GPSTracker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity implements View.OnClickListener {
  //SharedPreferences sharedPreferences;
  TextView tv_signup, tv_forgot_pass;
  ImageView iv_fb, iv_google;
  EditText email, pass, phone_no1;
  Button login_button;
  GPSTracker gpsTracker;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    DataBindingUtil.setContentView(this, R.layout.login);

    //getDeviceToken();
    //getpermission();
    gpsTracker = new GPSTracker(this, this);

    tv_signup = (TextView) findViewById(R.id.tv_signup);
    tv_forgot_pass = (TextView) findViewById(R.id.tv_forgot_pass);
    //iv_fb = (ImageView) findViewById(R.id.iv_fb);
    //iv_google = (ImageView) findViewById(R.id.iv_google);
    email = (EditText) findViewById(R.id.email);
    pass = (EditText) findViewById(R.id.pass);
    login_button = (Button) findViewById(R.id.login_button);

    tv_signup.setOnClickListener(this);
    //tv_forgot_pass.setOnClickListener(this);
    //iv_fb.setOnClickListener(this);
    //iv_google.setOnClickListener(this);
    login_button.setOnClickListener(this);


    //if (SaveSharedPreference.getLoggedStatus(getApplicationContext())){
    //  Intent intent = new Intent(getApplicationContext(),)
    //}
  }

  @Override public void onClick(View view) {

    switch (view.getId()) {


      case R.id.tv_signup:
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
        break;
      case R.id.tv_forgot_pass:
        Intent intent1 = new Intent(Login.this, ForgotPass.class);
        startActivity(intent1);
        break;
      //case R.id.iv_fb:
      //  startActivityForResult(new Intent(this, FacebookLogin.class), 121);
      //  break;
      //case R.id.iv_google:
      //  startActivityForResult(new Intent(this, GoogleLogin.class), 120);
      //  break;
      case R.id.login_button:
        if (checkLogin()) callLoginApi();
        break;
    }
  }

  //@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  //  super.onActivityResult(requestCode, resultCode, data);
  //  String socialMediaType = "";
  //  if (requestCode == 121) {
  //    try {
  //      socialMediaType = "facebook";
  //      String name =
  //          data.getExtras().getString("f_name") + " " + data.getExtras().getString("l_name");
  //      String f_name = data.getExtras().getString("f_name");
  //      String l_name = data.getExtras().getString("l_name");
  //      String fb_id = data.getExtras().getString("socialid");
  //      String email = data.getExtras().getString("email");
  //      String image = data.getExtras().getString("image");
  //      String gender = data.getExtras().getString("gender");
  //
  //      callSocialLoginApi(name, email, fb_id, image, socialMediaType);
  //    } catch (Exception e) {
  //      e.printStackTrace();
  //    }
  //
  //    //Toast.makeText(this,"this is facebook login class",Toast.LENGTH_LONG).show();
  //  } else if (requestCode == 120) {
  //    socialMediaType = "google";
  //    String name =
  //        data.getExtras().getString("f_name") + " " + data.getExtras().getString("l_name");
  //    String f_name = data.getExtras().getString("f_name");
  //    String l_name = data.getExtras().getString("l_name");
  //    String fb_id = data.getExtras().getString("socialid");
  //    String email = data.getExtras().getString("email");
  //    String image = data.getExtras().getString("image");
  //    // String gender = data.getExtras().getString("gender");
  //
  //    callSocialLoginApi(name, email, fb_id, image, socialMediaType);
  //    //Toast.makeText(LoginActivity.this,"this is google login class",Toast.LENGTH_LONG).show();
  //  }
  //}

  //private void callSocialLoginApi(String name, String email, String fb_id, String image,
  //    String type) {
  //  MyDialog.getInstance(this).showDialog();
  //  Retrofit retrofit = ApiClient.getClient();
  //  ApiInterface apiInterface = retrofit.create(ApiInterface.class);
  //  Call<CommonResponse> call =
  //      apiInterface.getSocialLoginResult(name, email, fb_id, image, type, "android",
  //          SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.device_token));
  //
  //  call.enqueue(new Callback<CommonResponse>() {
  //    @Override
  //    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
  //      MyDialog.getInstance(Login.this).hideDialog();
  //      if (response.isSuccessful()) {
  //        if (response.body().getStatus().equals("SUCCESS")) {
  //          SharedPreferenceWriter.getInstance(Login.this)
  //              .writeStringValue(SharedPreferenceKey.token,
  //                  response.body().getSocialLogin().getToken());
  //          SharedPreferenceWriter.getInstance(Login.this)
  //              .writeBooleanValue(SharedPreferenceKey.currentLogin, true);
  //
  //          Intent intent = new Intent(Login.this, MainActivity.class);
  //          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
  //          startActivity(intent);
  //          finish();
  //        } else {
  //          Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
  //        }
  //      }
  //    }
  //
  //    @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
  //      MyDialog.getInstance(Login.this).hideDialog();
  //    }
  //  });
  //}

  private void callLoginApi() {
    MyDialog.getInstance(this).showDialog();
    Retrofit retrofit = ApiClient.getClient();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    Call<CommonResponse> call =
        apiInterface.getLoginResult(email.getText().toString(), pass.getText().toString(),
            "android",
            SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.device_token));

    call.enqueue(new Callback<CommonResponse>() {
      @Override
      public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
        MyDialog.getInstance(Login.this).hideDialog();
        if (response.isSuccessful()) {
          if (response.body().getStatus().equals("SUCCESS")) {
            SharedPreferenceWriter.getInstance(Login.this)
                .writeStringValue(SharedPreferenceKey.token, response.body().getLogin().getToken());
            SharedPreferenceWriter.getInstance(Login.this)
                .writeBooleanValue(SharedPreferenceKey.currentLogin, true);
            SharedPreferenceWriter.getInstance(Login.this)
                .writeStringValue(SharedPreferenceKey.NOTIFICATION_STATUS,
                    response.body().getLogin().getStatus());
            SharedPreferenceWriter.getInstance(Login.this)
                .writeStringValue(SharedPreferenceKey.Email, response.body().getLogin().getEmail());
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
          } else {
            Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
          }
        }
      }

      @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
        MyDialog.getInstance(Login.this).hideDialog();
      }
    });
  }

  private boolean checkLogin() {
    boolean flag = true;
    if (email.getText().toString().equals("")) {
      Toast.makeText(Login.this, "Please enter email id", Toast.LENGTH_LONG).show();
      flag = false;
    } else if (pass.getText().toString().equals("")) {
      Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_LONG).show();
      flag = false;
    }
    //if (phone_no1.getText().toString().equals("")) {
    //  Toast.makeText(Login.this, "Please enter phone no", Toast.LENGTH_LONG).show();
    //
    //
    //}
    return flag;
  }
}
