package com.wrx.quickeats.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by mobulous55 on 29/1/18.
 */

public class DeliveryBoytStatus extends BroadcastReceiver  {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "this is delivery boy status", Toast.LENGTH_SHORT).show();

    }
}
