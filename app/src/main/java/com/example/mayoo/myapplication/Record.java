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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by mayoo on 11/25/2016.
 */

public class Record extends AppCompatActivity {

    ImageView child_1_btn, child_2_btn, child_3_btn,
            record_child_gender_1,record_child_gender_2,record_child_gender_3;
    LinearLayout child_1_layout, child_2_layout, child_3_layout;
    TextView child_1_txt, child_2_txt, child_3_txt;
    int child_1_id, child_2_id, child_3_id;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

    }

    @Override
    public void onBackPressed() {
        Intent intent_from = getIntent();
        Intent intent_to = new Intent(Record.this, Home.class);
        intent_to.putExtra("username", intent_from.getStringExtra("username"));
        intent_to.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_to);
        finish();

        Record.super.onBackPressed();
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

        Intent to_intent = new Intent(Record.this, ChildRegister.class);

        to_intent.putExtra("username", username); // (Key, Value)
        to_intent.putExtra("child#", counter);


        //finish();
        startActivity(to_intent);
    }

    @Override
    protected void onResume() {

        Log.d("Record", "onResume");
        super.onResume();


        child_1_btn = (ImageView) findViewById(R.id.child_1_btn);
        child_2_btn = (ImageView) findViewById(R.id.child_2_btn);
        child_3_btn = (ImageView) findViewById(R.id.child_3_btn);
        child_1_layout = (LinearLayout) findViewById(R.id.child_1_layout);
        child_2_layout = (LinearLayout) findViewById(R.id.child_2_layout);
        child_3_layout = (LinearLayout) findViewById(R.id.child_3_layout);
        child_1_txt = (TextView) findViewById(R.id.child_1_txt);
        child_2_txt = (TextView) findViewById(R.id.child_2_txt);
        child_3_txt = (TextView) findViewById(R.id.child_3_txt);
        record_child_gender_1 = (ImageView) findViewById(R.id.record_child_gender_btn_1);
        record_child_gender_2 = (ImageView) findViewById(R.id.record_child_gender_btn_2);
        record_child_gender_3 = (ImageView) findViewById(R.id.record_child_gender_btn_3);






        Intent from_intent = getIntent();
        username = from_intent.getStringExtra("username");
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

                    int child_img_res;
                    if (child_info.get(2).equals("Female")) {
                        child_img_res = R.drawable.female;
                    } else {
                        child_img_res = R.drawable.male;
                    }
                    record_child_gender_1.setImageResource(child_img_res);

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

                    int child_img_res;
                    if (child_info.get(2).equals("Female")) {
                        child_img_res = R.drawable.female;
                    } else {
                        child_img_res = R.drawable.male;
                    }
                    record_child_gender_2.setImageResource(child_img_res);

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

                    int child_img_res;
                    if (child_info.get(2).equals("Female")) {
                        child_img_res = R.drawable.female;
                    } else {
                        child_img_res = R.drawable.male;
                    }
                    record_child_gender_3.setImageResource(child_img_res);

                    child_3_layout.setVisibility(View.VISIBLE);


                    ImageButton new_imageButton = (ImageButton) findViewById(R.id.new_btn);
                    new_imageButton.setVisibility(View.GONE);

                }
            }

            childDB_object.close();
        }

        helper_object.close();

    }

    public void child1(View view) {
        Log.d("username", username);
        Intent intent_to = new Intent(Record.this, ChildRecord.class);
        intent_to.putExtra("childID", child_1_id);
        intent_to.putExtra("username", username);
        intent_to.putExtra("child#", 1);
        startActivity(intent_to);
    }

    public void child2(View view) {
        Intent intent_to = new Intent(Record.this, ChildRecord.class);
        intent_to.putExtra("childID", child_2_id);
        intent_to.putExtra("username", username);
        intent_to.putExtra("child#", 2);
        startActivity(intent_to);
    }
    public void child3(View view) {
        Intent intent_to = new Intent(Record.this, ChildRecord.class);
        intent_to.putExtra("childID", child_3_id);
        intent_to.putExtra("username", username);
        intent_to.putExtra("child#", 3);
        startActivity(intent_to);
    }


}
