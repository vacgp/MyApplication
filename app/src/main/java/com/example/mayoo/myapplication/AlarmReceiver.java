package com.example.mayoo.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("AlarmReceiver", "onReceive");

        String LOG_TAG = "AlarmReceiver";
        //Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            Log.e(LOG_TAG, "Dumping Intent start");
            while (it.hasNext()) {
                String key = it.next();
                Log.e(LOG_TAG, "[" + key + "=" + bundle.get(key) + "]");
            }
            Log.e(LOG_TAG, "Dumping Intent end");
        } else {
            Log.e(LOG_TAG, "bundle is empty");
        }


        int childID = intent.getIntExtra("childID", 0);
        Log.d("ChildRecord", childID + "");

        ChildDB childDB_object = new ChildDB(context);
        childDB_object.open();

        ArrayList<String> child_info = new ArrayList<>();
        if (childID != 0) {
            child_info = childDB_object.childInfo(childID);
        }

        Intent notificationIntent = new Intent(context, ChildRecord.class);

        //notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        notificationIntent.putExtra("username", intent.getStringExtra("username"));
        notificationIntent.putExtra("childID", childID);
        notificationIntent.putExtra("child#", intent.getIntExtra("child#", 0));
        notificationIntent.putExtra("fromNotifi", 1);
        notificationIntent.putExtra("childAge", intent.getIntExtra("childAge", 0));
       /* TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ChildRecord.class);
        stackBuilder.addNextIntent(notificationIntent);*/

        PendingIntent pendingIntent = PendingIntent.getActivity(context, childID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        int childAge = intent.getIntExtra("childAge", 0);

        byte[] imageByte_toDB = childDB_object.gettingImage(childID);
        Bitmap imageBitmap_fromByte;
        InputStream inputStream = new ByteArrayInputStream(imageByte_toDB);
        imageBitmap_fromByte = BitmapFactory.decodeStream(inputStream);

        Notification notification = builder.setContentTitle(child_info.get(0) + context.getString(R.string.is) + childAge + " " + (childAge == 1 ? context.getString(R.string.month) : context.getString(R.string.months)) + context.getString(R.string.now))
                .setContentText(context.getString(R.string.Click_to_see_vaccines))
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(imageBitmap_fromByte)
                //.addAction(R.mipmap.ic_launcher, "Call", pendingIntent)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(childID, notification);
    }
}