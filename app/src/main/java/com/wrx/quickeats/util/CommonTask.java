package com.wrx.quickeats.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;


public class CommonTask {

    public static int[] getScreenDimen(Context context) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int width = display.getWidth(); // deprecated
        int height = display.getHeight(); // deprecated

        return new int[]{width, height};
    }
}
