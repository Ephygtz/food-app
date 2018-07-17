package com.wrx.quickeats.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationSettingsStates;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.util.StartLocationAlert;
import com.wrx.quickeats.R;
import com.wrx.quickeats.util.GPSTracker;

/**
 * Created by mobulous51 on 10/10/17.
 */

public class SplashActivity extends AppCompatActivity {

  GPSTracker gpsTracker;
  protected static final int REQUEST_CHECK_SETTINGS = 0x1;
  boolean permission = false;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    //startActivity(new Intent(SplashActivity.this, SecondSplashActivity.class));
    //finish();

    getDeviceToken();
    getpermission();
    gpsTracker = new GPSTracker(this, this);
    permission = SharedPreferenceWriter.getInstance(this)
        .getBoolean(SharedPreferenceKey.permission_granted_location, true);

    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    if (statusOfGPS) {
      if (permission) {
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            if (SharedPreferenceWriter.getInstance(SplashActivity.this)
                .getBoolean(SharedPreferenceKey.currentLogin, false)) {
              startActivity(new Intent(SplashActivity.this, MainActivity.class));
              finish();
            } else {
              startActivity(new Intent(SplashActivity.this,SecondSplashActivity.class));
              finish();
            }
          }
        }, 2000);
      }
      //} else {
      //  startActivity(new Intent(SplashActivity.this, SecondSplashActivity.class));
      //  finish();
      //}
      //else//Toast.makeText(SplashActivity.this,"permission not granted",Toast.LENGTH_LONG).show();

    } else {
      new StartLocationAlert(SplashActivity.this);
    }
  }

  private void getpermission() {
    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      requestPermissionforApp();
    }
  }

  private void requestPermissionforApp() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        android.Manifest.permission.ACCESS_FINE_LOCATION)) {
      Toast.makeText(this,
          "Current Location needed. Please allow in App Settings for additional functionality.",
          Toast.LENGTH_SHORT).show();
      Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
          Uri.fromParts("package", getPackageName(), null));
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
      finish();
    } else {
      ActivityCompat.requestPermissions(this, new String[] {
          Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
      }, 123);
    }
  }

  private void getDeviceToken() {

    final Thread thread = new Thread() {
      @Override public void run() {
        Log.e(">>>>>>>>>>>>>>", "thred IS  running");
        SharedPreferenceWriter mPreference =
            SharedPreferenceWriter.getInstance(getApplicationContext());
        try {
          if (mPreference.getString(SharedPreferenceKey.device_token).isEmpty()) {
            String token = FirebaseInstanceId.getInstance().getToken();
            //                        String token = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(),
            //                                android.provider.Settings.Secure.ANDROID_ID);
            Log.e("Generated Device Token", "-->" + token);
            if (token == null) {
              getDeviceToken();
            } else {
              mPreference.writeStringValue(SharedPreferenceKey.device_token, token);
            }
          }
        } catch (Exception e1) {
          e1.printStackTrace();
        }
        super.run();
      }
    };
    thread.start();
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    try {
      if (requestCode == 123) {
        int lenght = grantResults.length;

        for (int grantlength = 0; grantlength < grantResults.length; grantlength++) {
          if (grantResults[grantlength] == PackageManager.PERMISSION_GRANTED) {
            getDeviceToken();
            SharedPreferenceWriter.getInstance(this)
                .writeBooleanValue(SharedPreferenceKey.permission_granted_location, true);
            gpsTracker = new GPSTracker(this, SplashActivity.this);
            startActivity(new Intent(SplashActivity.this, Login.class));
            finish();
          } else if (grantResults[grantlength] == PackageManager.PERMISSION_DENIED) {
            boolean b =
                ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[grantlength]);
            if (!b) {
              requestPermissionforApp();
            } else {
              requestPermissionforApp();
            }
          } else {
            requestPermissionforApp();
          }
        }
      } else {
        requestPermissionforApp();
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
    switch (requestCode) {
      case REQUEST_CHECK_SETTINGS:
        switch (resultCode) {
          case Activity.RESULT_OK: {
            // showDialog();
            //new SplashActivity();
            //gpsTracker = new GPSTracker(this, SplashActivity.this);
            if (SharedPreferenceWriter.getInstance(SplashActivity.this)
                .getBoolean(SharedPreferenceKey.currentLogin, false)) {
              startActivity(new Intent(SplashActivity.this, MainActivity.class));
              finish();
            } else {
              startActivity(new Intent(SplashActivity.this, Login.class));
              finish();
            }

            // All required changes were successfully made
            break;
          }
          case Activity.RESULT_CANCELED: {
            new StartLocationAlert(SplashActivity.this);
            // The user was asked to change settings, but chose not to
            break;
          }
          default: {
            break;
          }
        }
        break;
    }
  }
}
