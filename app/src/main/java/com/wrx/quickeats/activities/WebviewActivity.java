package com.wrx.quickeats.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wrx.quickeats.R;

/**
 * Created by mobulous51 on 8/12/17.
 */

public class WebviewActivity  extends AppCompatActivity{

    WebView webView;
    String URL;
    TextView toolText;
    ImageView back_button;
    String tool_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        webView=(WebView)findViewById(R.id.webview);
        toolText=(TextView)findViewById(R.id.text_toolbar);
        back_button=(ImageView)findViewById(R.id.back_button);


        try{
            URL=getIntent().getStringExtra("url");
            tool_text=getIntent().getStringExtra("toolText");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        toolText.setText(tool_text);

        webView.loadUrl(URL);
        //myWebView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setJavaScriptEnabled(true);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
