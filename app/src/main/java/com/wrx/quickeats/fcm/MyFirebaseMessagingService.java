package com.wrx.quickeats.fcm;

/**
 * Created by mobulous2 on 3/3/17.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.activities.MainActivity;

import org.json.JSONObject;

import java.util.Date;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    // Called when message is received.@param remoteMessage Object representing the message received from Firebase Cloud Messaging.
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) // Check if message contains a data payload.
        {
            String type="",message="",order_id="",time="";
            try {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Map<String, String> data = remoteMessage.getData();
                JSONObject jsonObject = new JSONObject(data);
                 type = jsonObject.getString("type");
                 message = jsonObject.getString("message");
                 order_id = jsonObject.getString("order_id");
                 time = jsonObject.getString("time");

                if (SharedPreferenceWriter.getInstance(this).getBoolean(SharedPreferenceKey.appOpenOrNOt,false)) {
                    Intent intent = new Intent("Push");
                    intent.putExtra("type",type);
                    intent.putExtra("message",message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    Log.d(TAG, "openApp truepart: " + remoteMessage.getNotification().getBody());

                } else {
                    sendNotification(type, message, order_id, time);
                    Log.d(TAG, "openApp falsepart: " + remoteMessage.getNotification().getBody());
                }


            } catch (Exception e) {
                e.printStackTrace();
                //sendNotification(type, message, order_id, time);
            }
        }
        if (remoteMessage.getNotification() != null) // Check if message contains a notification payload.
        {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }


    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.
    private void sendNotification(String type, String message, String order_id, String time) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("message", message);
        intent.putExtra("order_id", order_id);
        intent.putExtra("time", time);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.imgae_notification)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setColor(ContextCompat.getColor(MyFirebaseMessagingService.this, R.color.home_header_color))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
    }

}
//Server key
//AIzaSyARp5tjnNDrypmTHXkLdZgRWoxDHETRXm8
//json info{"type":"Follow","message":"raveena started following you","video_id":"0","notification_message":"Unfollow"}