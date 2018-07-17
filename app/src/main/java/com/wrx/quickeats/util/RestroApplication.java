package com.wrx.quickeats.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by mobua01 on 25/7/17.
 */

public class RestroApplication extends Application {


    private static Context context;
    CustomFontFamily customFontFamily;


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        RestroApplication.context = this;
        customFontFamily = CustomFontFamily.getInstance();
        // add your custom fonts here with your own custom name.
        customFontFamily.addFont("robo_black", "Roboto-Black.ttf");
        customFontFamily.addFont("robo_black_italic", "Roboto-BlackItalic.ttf");
        customFontFamily.addFont("robo_bold", "Roboto-Bold.ttf");
        customFontFamily.addFont("robo_bold_italic", "Roboto-BoldItalic.ttf");
        customFontFamily.addFont("robo_italic", "Roboto-Italic.ttf");
        customFontFamily.addFont("robo_light", "Roboto-Light.ttf");
        customFontFamily.addFont("robo_light_italic", "Roboto-LightItalic.ttf");
        customFontFamily.addFont("robo_medium", "Roboto-Medium.ttf");
        customFontFamily.addFont("robo_medium_italic", "Roboto-MediumItalic.ttf");
        customFontFamily.addFont("robo_regular", "Roboto-Regular.ttf");
        customFontFamily.addFont("robo_thin", "Roboto-Thin.ttf");
        customFontFamily.addFont("robo_thin_italic", "Roboto-ThinItalic.ttf");
        customFontFamily.addFont("robo_cond_bold", "RobotoCondensed-Bold.ttf");
        customFontFamily.addFont("robo_cond_bold_italic", "RobotoCondensed-BoldItalic.ttf");
        customFontFamily.addFont("robo_cond_italic", "RobotoCondensed-Italic.ttf");
        customFontFamily.addFont("robo_cond_light", "RobotoCondensed-Light.ttf");
        customFontFamily.addFont("robo_cond_light_italic", "RobotoCondensed-LightItalic.ttf");
        customFontFamily.addFont("robo_cond_regular", "RobotoCondensed-Regular.ttf");
        customFontFamily.addFont("test", "Calligraphunk-trial.ttf");
    }

    public static Context getContext() {
        return context;
    }
}
