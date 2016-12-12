package com.example.mayoo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by AbdulRahman on 12/12/2016.
 */

public class VacInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent_from = getIntent();
        setContentView(intent_from.getIntExtra("layoutID", 0));
    }
}
