package com.example.mayoo.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by mayoo on 11/25/2016.
 */

public class Record extends AppCompatActivity {

    ImageButton child_1_btn, child_2_btn, child_3_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        child_1_btn = (ImageButton) findViewById(R.id.child_1_btn);
        child_2_btn = (ImageButton) findViewById(R.id.child_2_btn);
        child_3_btn = (ImageButton) findViewById(R.id.child_3_btn);


        Intent from_intent = getIntent();
        String username = from_intent.getStringExtra("username");
        Helper helper_object = new Helper(Record.this);

        helper_object.open();

        ArrayList<Integer> childIDs = helper_object.getChildIDs(username);

             if (!childIDs.isEmpty()) {
                 ChildDB childDB_object = new ChildDB(Record.this);
                 childDB_object.open();
                 for (int i = 0; i <= childIDs.size(); i++) {
                     if (i == 0) {
                         byte[] imageByte_fromDB = childDB_object.gettingImage(childIDs.get(i));
                         Bitmap imageBitmap_fromByte;
                         InputStream inputStream = new ByteArrayInputStream(imageByte_fromDB);
                         imageBitmap_fromByte = BitmapFactory.decodeStream(inputStream);

                         child_1_btn.setImageBitmap(imageBitmap_fromByte);
                         child_1_btn.setVisibility(View.VISIBLE);

                     } else if (i == 1) {

                         byte[] imageByte_fromDB = childDB_object.gettingImage(childIDs.get(i));
                         Bitmap imageBitmap_fromByte;
                         InputStream inputStream = new ByteArrayInputStream(imageByte_fromDB);
                         imageBitmap_fromByte = BitmapFactory.decodeStream(inputStream);

                         child_2_btn.setImageBitmap(imageBitmap_fromByte);
                         child_2_btn.setVisibility(View.VISIBLE);

                     } else if (i == 2) {

                         byte[] imageByte_fromDB = childDB_object.gettingImage(childIDs.get(i));
                         Bitmap imageBitmap_fromByte;
                         InputStream inputStream = new ByteArrayInputStream(imageByte_fromDB);
                         imageBitmap_fromByte = BitmapFactory.decodeStream(inputStream);

                         child_3_btn.setImageBitmap(imageBitmap_fromByte);
                         child_3_btn.setVisibility(View.VISIBLE);


                         ImageButton new_imageButton = (ImageButton) findViewById(R.id.new_btn);
                         new_imageButton.setVisibility(View.GONE);

                     }
                 }

                 childDB_object.close();
             }

             helper_object.close();


    }

    public void new_record(View view) {
        int counter = 0;
        if (child_1_btn.getVisibility() == View.VISIBLE) {
            counter++;
        }
        if (child_2_btn.getVisibility() == View.VISIBLE) {
            counter++;
        }
        if (child_3_btn.getVisibility() == View.VISIBLE) {
            counter++;
        }


        Intent from_intent = getIntent();
        String username = from_intent.getStringExtra("username");

        Intent to_intent = new Intent(Record.this, ChildRegister.class);

        to_intent.putExtra("username", username); // (Key, Value)
        to_intent.putExtra("child#", counter);


        startActivity(to_intent);
    }
}
