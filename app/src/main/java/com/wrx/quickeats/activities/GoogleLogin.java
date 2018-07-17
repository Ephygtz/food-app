package com.wrx.quickeats.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.wrx.quickeats.fragments.Home;

/**
 * Created by mobulous51 on 16/3/17.
 */

public class GoogleLogin extends AppCompatActivity
    implements GoogleApiClient.OnConnectionFailedListener {
  private static final String TAG = "Login";
  private static final int RC_SIGN_IN = 9001;
  private GoogleApiClient mGoogleApiClient;
  private String image;
  private String gender;
  private String socialid;
  private String email;
  private String name;
  private String f_name;
  private String l_name;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GoogleSignInOptions gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile()
            .requestEmail()
            .build();

    mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();
    gmailSignIn();
  }

  //    gmail sigin
  private void gmailSignIn() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override public void onStart() {
    super.onStart();
    OptionalPendingResult<GoogleSignInResult> opr =
        Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
    if (opr.isDone()) {
      Log.d(TAG, "Got cached sign-in");
      GoogleSignInResult result = opr.get();
    } else {
      showProgressDialog();
      opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
        @Override public void onResult(GoogleSignInResult googleSignInResult) {
          hideProgressDialog();
          handleSignInResult(googleSignInResult);

          if (googleSignInResult.isSuccess()) {
            Toast.makeText(GoogleLogin.this, "success", Toast.LENGTH_LONG).show();
          }
        }
      });
    }
  }

  @Override public void onConnectionFailed(ConnectionResult connectionResult) {

    Log.d(TAG, "onConnectionFailed:" + connectionResult);
  }

  private void showProgressDialog() {
    // ProgressDialogUtils.showProgressDialog(this, "Please Wait...", false);
  }

  private void hideProgressDialog() {
    // ProgressDialogUtils.dismissProgressDialog();
  }

  private void handleSignInResult(GoogleSignInResult result) {
    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
    if (result.isSuccess()) {
      GoogleSignInAccount acct = result.getSignInAccount();
      Log.i("user name", "" + acct.getDisplayName());
      Uri personPhoto = acct.getPhotoUrl();
      if (personPhoto != null) {
        image = personPhoto.toString();
      } else {
        image = "";
      }
      gender = "Male";
      socialid = acct.getId();
      email = acct.getEmail();
      name = acct.getDisplayName();
      String[] u_name = null;
      if (name.contains(" ")) {
        u_name = name.split(" ");
        if (u_name.length > 1) {
          f_name = u_name[0];
          l_name = u_name[1];
        } else {
          f_name = u_name[0];
          l_name = u_name[0];
        }
      }
      Intent intent = new Intent();
      // intent.putExtra("id", id);
      intent.putExtra("email", email);
      //intent.putExtra("username", username);
      intent.putExtra("name", name);
      intent.putExtra("f_name", f_name);
      intent.putExtra("l_name", l_name);
      intent.putExtra("socialid", socialid);
      intent.putExtra("image", image);
      setResult(120, intent);
      finish();

      signOut();
      Log.i("Gmail Login", "" + name + "" + email + "" + socialid + "" + image);
    } else if (result.getStatus().getStatusCode() == 12501) {
      //   SnackBar.getSnackBar().showBar(GoogleLogin.this, "Request Cancel", Toast.LENGTH_SHORT);
      Intent intent = new Intent();
      intent = null;
      setResult(120, intent);
      finish();
    } else {

    }
  }

  private void signOut() {
    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
      @Override public void onResult(Status status) {
        if (status.getStatus().getStatusCode() == 0) {
          Intent intent = new Intent();
          intent.putExtra("socialid", socialid);
          intent.putExtra("email", email);
          intent.putExtra("name", name);
          intent.putExtra("f_name", f_name);
          intent.putExtra("l_name", l_name);
          intent.putExtra("image", image);
          setResult(120, intent);
          finish();
        }
      }
    });
  }
  //gmail signin finish

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      /*Toast.makeText(GoogleLogin.this, "success", Toast.LENGTH_LONG).show();
      if (result.isSuccess()) {
        Intent intent = new Intent(GoogleLogin.this, Home.class);
        intent.putExtra("criteria_list_key", "selectClimate");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(GoogleLogin.this, "success", Toast.LENGTH_LONG).show();
      }*/
      handleSignInResult(result);
      //Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
      //String s = "";
    }
    //callbackManager.onActivityResult(requestCode, resultCode, data);
  }
}
