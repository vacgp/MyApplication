package com.example.mayoo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by mayoo on 12/9/2016.
 */

public class Safety extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int id = R.layout.safety;
        View view1 = getLayoutInflater().inflate(id, null);
        setContentView(view1);
    }
}