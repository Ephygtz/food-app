package com.wrx.quickeats.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.fragments.Home;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by mobulous51 on 16/3/17.
 */

public class FacebookLogin extends AppCompatActivity {

  CallbackManager callbackManager;
  AccessToken accessToken;
  private String email;
  private String username;
  private String name;
  private String socialid;
  private String image;
  private String f_name;
  private String l_name;
  private String id;
  private String gender;
  private List<String> values = new CopyOnWriteArrayList<>();

  private FirebaseAuth mAuth;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_main);
    FacebookSdk.sdkInitialize(this);
    // Initialize Firebase Auth
    mAuth = FirebaseAuth.getInstance();
    //        AppEventsLogger.activateApp(this);
    callbackManager = CallbackManager.Factory.create();
    accessToken = AccessToken.getCurrentAccessToken();
    fbSignIn();
  }

  //fb signin
  private void fbSignIn() {
    try {
      PackageInfo info = getPackageManager().getPackageInfo("com.sats.quickeats.activities",
          PackageManager.GET_SIGNATURES);
      for (Signature signature : info.signatures) {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(signature.toByteArray());
        Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
      }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
    LoginManager.getInstance()
        .logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
    LoginManager.getInstance()
        .registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

          @Override public void onSuccess(LoginResult result) {
            accessToken = AccessToken.getCurrentAccessToken();
            getUserDetail();
            //Toast.makeText(FacebookLogin.this, "" + accessToken + "Login Success",
            //    Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(FacebookLogin.this, Home.class);
            //intent.putExtra("criteria_list_key", "selectClimate");
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
            //finish();
            //Toast.makeText(FacebookLogin.this, "" + accessToken + "Login Success",
            //    Toast.LENGTH_SHORT).show();
          }

          @Override public void onError(FacebookException error) {
            Toast.makeText(FacebookLogin.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
            Log.i("Facebook Error----->", error.toString());
          }

          @Override public void onCancel() {
            Toast.makeText(FacebookLogin.this, "Request Cancel", Toast.LENGTH_SHORT).show();
            Log.i("facebook onCancel----->", "cancel");
            Intent intent = new Intent();
            intent = null;
            setResult(121, intent);
            finish();
          }
        });
  }

  private void getUserDetail() {
    GraphRequest request =
        GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
          @Override public void onCompleted(JSONObject object, GraphResponse response) {
            try {
              Log.e("JSON------>>>", object.toString());
              if (object != null) {
                id = object.getString("id");
                email = object.getString("email");
                if (email != null) {
                  SharedPreferenceWriter.getInstance(FacebookLogin.this)
                      .writeStringValue(SharedPreferenceKey.Email, email);
                }

                gender = object.getString("gender");
                int index = email.indexOf('@');
                username = email.substring(0, index);
                name = object.getString("name");
                f_name = object.getString("first_name");
                l_name = object.getString("last_name");
                socialid = object.getString("id");
                image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                image = "https://graph.facebook.com/" + socialid + "/picture?type=large";

                Log.i("fbbb", "" + image);
                if (image == null) {
                  image = "";
                }
                //                                socialMediaServiceHit();
                Intent intent = new Intent();
                intent.putExtra("id", id);
                intent.putExtra("email", email);
                intent.putExtra("username", username);
                intent.putExtra("name", name);
                intent.putExtra("f_name", f_name);
                intent.putExtra("l_name", l_name);
                intent.putExtra("socialid", socialid);
                intent.putExtra("image", image);
                intent.putExtra("gender", gender);
                setResult(121, intent);
                finish();
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
    Bundle parameters = new Bundle();
    parameters.putString("fields",
        "id,name,link,email,birthday,gender,first_name,last_name,picture.type(large)");
    request.setParameters(parameters);
    request.executeAsync();
  }

  //fb sign in finish
  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }
}
