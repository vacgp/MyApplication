package com.example.mayoo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

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
        int counter=0;
        ImageButton child_1_btn = (ImageButton) findViewById(R.id.child_1_btn);
        if (child_1_btn.getVisibility() == View.VISIBLE){
            counter++;
        }
        ImageButton child_2_btn = (ImageButton) findViewById(R.id.child_2_btn);
        if (child_2_btn.getVisibility() == View.VISIBLE){
            counter++;
        }
        ImageButton child_3_btn = (ImageButton) findViewById(R.id.child_3_btn);
        if (child_3_btn.getVisibility() == View.VISIBLE){
            counter++;
        }


        Intent from_intent = new Intent();
        String username = from_intent.getStringExtra("username");

        Intent to_intent = new Intent(Record.this, ChildRegister.class);
        to_intent.putExtra("username", username); // (Key, Value)
        to_intent.putExtra("child#", counter);


        startActivity(to_intent);
    }
}
