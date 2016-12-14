package com.example.mayoo.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by mayoo on 11/25/2016.
 */

public class Home  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 135, 165)));
        }

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
