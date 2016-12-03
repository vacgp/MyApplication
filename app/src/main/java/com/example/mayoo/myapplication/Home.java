package com.example.mayoo.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by mayoo on 11/25/2016.
 */

public class Home  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void records(View view) {
        Intent from_intent = new Intent();
        String username = from_intent.getStringExtra("username");
        Intent to_intent = new Intent(Home.this, Record.class);
        to_intent.putExtra("username", username); // (Key, Value)
        startActivity(to_intent);


    }

    public void ask_a_doctor(View view) {

    }
}
