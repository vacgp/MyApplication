package com.example.mayoo.myapplication;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by mayoo on 11/25/2016.
 */

public class Home extends AppCompatActivity {

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        session = new UserSessionManager(this);


        String LOG_TAG = "Home";
        Intent intent = getIntent();
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:

                Log.d("dfddfdf", getApplicationContext()+"");
                final Dialog dialog = new Dialog(Home.this);
                dialog.getWindow();
                dialog.setContentView(R.layout.delete_record);
                dialog.show();


                Button yes_btn = (Button) dialog.findViewById(R.id.yes_btn);
                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Helper helper_object = new Helper(Home.this);

                        helper_object.open();

                        ArrayList<Integer> childIDs = helper_object.getChildIDs(getIntent().getStringExtra("username"));

                        if (!childIDs.isEmpty()) {
                            ChildDB childDB_object = new ChildDB(getApplicationContext());
                            childDB_object.open();
                            for (int i = 0; i < childIDs.size(); i++) {
                                cancelAlarm(childIDs.get(i), Home.this);
                            }
                        }
                        helper_object.close();




                        session.logoutUser();

                        dialog.dismiss();
                        finish();
                        /*finish();
                        Intent intent_to = new Intent(Home.this, MainActivity.class);
                        startActivity(intent_to);*/
                    }
                });

                Button no_btn = (Button) dialog.findViewById(R.id.no_btn);
                no_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cancelAlarm(int childID, Context context){
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.VAC");

        PendingIntent sender = PendingIntent.getBroadcast(context, childID, notificationIntent,  PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(sender);
        sender.cancel();
    }

    public void records(View view) {
        Intent from_intent = getIntent();
        String username = from_intent.getStringExtra("username");
        Intent to_intent = new Intent(Home.this, Record.class);

        to_intent.putExtra("username", username); // (Key, Value)
        startActivity(to_intent);


    }

    public void ask_a_doctor(View view) {
        Intent intent_browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.chop.edu/centers-programs/vaccine-education-center/contact-ask-the-vec"));
        startActivity(intent_browser);

    }

    public void safety(View view) {
        Intent intent_to = new Intent(Home.this, Safety.class);
        startActivity(intent_to);
    }

    public void about(View view) {
        Intent intent_to = new Intent(Home.this, About.class);
        startActivity(intent_to);
    }

    public void vaccines(View view) {
        Intent intent_to = new Intent(Home.this, VaccinesList.class);
        startActivity(intent_to);
    }
}
