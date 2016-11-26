package com.example.mayoo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by mayoo on 11/25/2016.
 */

public class Record extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
    }

    public void new_record(View view) {
        Intent from_intent = new Intent();
        String username = from_intent.getStringExtra("username");
        Intent to_intent = new Intent(Record.this, ChildRegister.class);
        to_intent.putExtra("username", username); // (Key, Value)
        startActivity(to_intent);
    }
}
