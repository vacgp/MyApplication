package com.example.mayoo.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by mayoo on 11/25/2016.
 */

public class Record extends AppCompatActivity {

    ImageButton child_1_btn, child_2_btn, child_3_btn;
    LinearLayout child_1_layout, child_2_layout, child_3_layout;
    TextView child_1_txt, child_2_txt, child_3_txt;
    int child_1_id, child_2_id, child_3_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 135, 165)));
        }

        child_1_btn = (ImageButton) findViewById(R.id.child_1_btn);
        child_2_btn = (ImageButton) findViewById(R.id.child_2_btn);
        child_3_btn = (ImageButton) findViewById(R.id.child_3_btn);
        child_1_layout = (LinearLayout) findViewById(R.id.child_1_layout);
        child_2_layout = (LinearLayout) findViewById(R.id.child_2_layout);
        child_3_layout = (LinearLayout) findViewById(R.id.child_3_layout);
        child_1_txt = (TextView) findViewById(R.id.child_1_txt);
        child_2_txt = (TextView) findViewById(R.id.child_2_txt);
        child_3_txt = (TextView) findViewById(R.id.child_3_txt);




        Intent from_intent = getIntent();
        String username = from_intent.getStringExtra("username");
        Helper helper_object = new Helper(Record.this);

        helper_object.open();

        ArrayList<Integer> childIDs = helper_object.getChildIDs(username);

             if (!childIDs.isEmpty()) {
                 ChildDB childDB_object = new ChildDB(Record.this);
                 childDB_object.open();
                 for (int i = 0; i < childIDs.size(); i++) {
                     if (i == 0) {
                         child_1_id = childIDs.get(i);
                         byte[] imageByte_fromDB = childDB_object.gettingImage(childIDs.get(i));
                         Bitmap imageBitmap_fromByte;
                         InputStream inputStream = new ByteArrayInputStream(imageByte_fromDB);
                         imageBitmap_fromByte = BitmapFactory.decodeStream(inputStream);

                         child_1_btn.setImageBitmap(imageBitmap_fromByte);
                         ArrayList<String> child_info = childDB_object.childInfo(childIDs.get(i));
                         child_1_txt.setText(child_info.get(0));
                         Log.d("size", child_info.size()+"");
                         Log.d("child", child_info.get(0));
                         child_1_layout.setVisibility(View.VISIBLE);

                     } else if (i == 1) {
                         child_2_id = childIDs.get(i);
                         byte[] imageByte_fromDB = childDB_object.gettingImage(childIDs.get(i));
                         Bitmap imageBitmap_fromByte;
                         InputStream inputStream = new ByteArrayInputStream(imageByte_fromDB);
                         imageBitmap_fromByte = BitmapFactory.decodeStream(inputStream);

                         child_2_btn.setImageBitmap(imageBitmap_fromByte);
                         ArrayList<String> child_info = childDB_object.childInfo(childIDs.get(i));
                         child_2_txt.setText(child_info.get(0));
                         child_2_layout.setVisibility(View.VISIBLE);

                     } else if (i == 2) {

                         child_3_id = childIDs.get(i);
                         byte[] imageByte_fromDB = childDB_object.gettingImage(childIDs.get(i));
                         Bitmap imageBitmap_fromByte;
                         InputStream inputStream = new ByteArrayInputStream(imageByte_fromDB);
                         imageBitmap_fromByte = BitmapFactory.decodeStream(inputStream);

                         child_3_btn.setImageBitmap(imageBitmap_fromByte);
                         ArrayList<String> child_info = childDB_object.childInfo(childIDs.get(i));
                         child_3_txt.setText(child_info.get(0));
                         child_3_layout.setVisibility(View.VISIBLE);


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
        if (child_1_layout.getVisibility() == View.VISIBLE) {
            counter++;
        }
        if (child_2_layout.getVisibility() == View.VISIBLE) {
            counter++;
        }
        if (child_3_layout.getVisibility() == View.VISIBLE) {
            counter++;
        }


        Intent from_intent = getIntent();
        String username = from_intent.getStringExtra("username");

        Intent to_intent = new Intent(Record.this, ChildRegister.class);

        to_intent.putExtra("username", username); // (Key, Value)
        to_intent.putExtra("child#", counter);


        startActivity(to_intent);
    }

    public void child1(View view) {
        Intent intent_to = new Intent(Record.this, ChildRecord.class);
        intent_to.putExtra("childID", child_1_id);
        startActivity(intent_to);
    }

    public void child2(View view) {
        Intent intent_to = new Intent(Record.this, ChildRecord.class);
        intent_to.putExtra("childID", child_2_id);
        startActivity(intent_to);
    }
    public void child3(View view) {
        Intent intent_to = new Intent(Record.this, ChildRecord.class);
        intent_to.putExtra("childID", child_3_id);
        startActivity(intent_to);
    }


}
