package com.wrx.quickeats.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.wrx.quickeats.R;

public class SecondSplashActivity extends AppCompatActivity {

  Button btn_started;
  TextView tv_login;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second_splash);

    btn_started = (Button) findViewById(R.id.btn_started);
    tv_login = (TextView) findViewById(R.id.tv_login);

    btn_started.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(SecondSplashActivity.this ,Login.class);
        startActivity(intent);

      }
    });

    tv_login.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(SecondSplashActivity.this ,Login.class);
        startActivity(intent);

      }
    });


  }
}
