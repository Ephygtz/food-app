package com.wrx.quickeats.util;

import android.app.Application;
import android.content.Context;


///**
// * Created by Rajeev Kr.Sharma (rajeevrocker7@gmail.com) on 14/8/17.
// */


public class MyApplication extends Application {

    private static MyApplication myApplication = null;

    public static synchronized MyApplication getInstance()
    {
        if(myApplication == null)
        {
            return new MyApplication();
        }
        else
            return myApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        BitmapAjaxCallback.clearCache();
    }
}
