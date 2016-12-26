package com.example.mayoo.myapplication;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.drawable.ColorDrawable;

import java.util.Calendar;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(this);

        if (session.isUserLoggedIn()) {
            /*
            * Alternative way is to create a blank Activity, with no UI, and register it as your launcher.
            * This Activity's role is to choose the correct actual Activity you want to show,
            * */

            Intent i = new Intent(MainActivity.this, Home.class);

            HashMap<String, String> user = session.getUserDetails();

            String name = user.get(UserSessionManager.KEY_NAME);

            i.putExtra("username", name); // (Key, Value)
            finish();
            startActivity(i);
        }
        /*AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");

        notificationIntent.putExtra("title", Calendar.getInstance().getTime().toString());
        notificationIntent.putExtra("Content", "1111111");
        notificationIntent.putExtra("Ticker", "Ticker");

        notificationIntent.addCategory("android.intent.category.VAC");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        //cal.add(Calendar.MONTH, 1);
        //Log.d("ddsd",cal.getTimeInMillis()+", "+ cal.getTimeInMillis()+5);

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),*//*60000 milliseconds = 1 minute*//*60000 , broadcast);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        }*/

    }

    public void Register(View view) {
        Intent i = new Intent(this, Register.class);
        startActivity(i);
    }

    public void login(View view) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow();
        dialog.setContentView(R.layout.login);
        dialog.show();
        Button loginbtn = (Button) dialog.findViewById(R.id.login_button);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username_txt = (EditText) dialog.findViewById(R.id.username_editText);
                EditText password_txt = (EditText) dialog.findViewById(R.id.pw_editText);
                Helper h = new Helper(MainActivity.this);
                h.open();

                String savedpw = h.getSingleEntry(username_txt.getText().toString());

                if(savedpw.equals(password_txt.getText().toString())){
                    session.createUserLoginSession(username_txt.getText().toString());

                    dialog.dismiss();
                    Intent i = new Intent(MainActivity.this, Home.class);

                    i.putExtra("username", username_txt.getText().toString()); // (Key, Value)
                    finish();
                    startActivity(i);
                }else {
                    Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();


                }


            }
        });
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_button);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }



}

