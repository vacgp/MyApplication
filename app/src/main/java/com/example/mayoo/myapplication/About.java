package com.example.mayoo.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mayoo on 12/9/2016.
 */

public class About extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(66,77,82)));
        }

    }
}
