package com.example.mayoo.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Created by mayoo on 11/25/2016.
 */

public class ChildRegister extends AppCompatActivity {

    EditText editText;
    Calendar myCalendar;


    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    byte[] imageByte_toDB;
    boolean img_added;


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_regiter);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 135, 165)));
        }


        editText = (EditText) findViewById(R.id.birth_edit_text);
        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        editText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(ChildRegister.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return false;
            }
        });


    }

    private void updateLabel() {

        String myFormat = "MM-dd-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }


    public void child_image(View view) {
        verifyStoragePermissions(ChildRegister.this);

        //Create intent to Open ImageHelper applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an ImageHelper is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the ImageHelper from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageButton imgBtn = (ImageButton) findViewById(R.id.childPhotoBtn);
                // Set the ImageHelper in ImageView after decoding the String
                imgBtn.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

                ImageHelper imageObject = new ImageHelper();
                imageByte_toDB = imageObject.getBytes(BitmapFactory.decodeFile(imgDecodableString));
                // imageView_Value = Arrays.toString(imageByte_toDB);
                img_added = true;


            } else {
                Toast.makeText(this, "You haven't picked a photo", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public void Done(View view) {
        EditText child_name_editText = (EditText) findViewById(R.id.child_name_editText);
        String child_name_str = child_name_editText.getText().toString();

        EditText birth_editText = (EditText) findViewById(R.id.birth_edit_text);
        String birth_str = birth_editText.getText().toString();

        RadioGroup gender_radioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
        RadioButton gender_radioBtn = (RadioButton) findViewById(gender_radioGroup.getCheckedRadioButtonId());
        String gender_srt;
        if (gender_radioBtn.getText().toString().equals("Female")) {
            gender_srt = "Female";
        } else {
            gender_srt = "Male";
        }

        Intent from_intent = getIntent();
        String username = from_intent.getStringExtra("username");
        int counter = from_intent.getIntExtra("child#", 0);


        ChildDB childDB_object = new ChildDB(ChildRegister.this);
        childDB_object.open();
        if (!img_added) {
            Bitmap defaultImage_bitMap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.kiddo);
            ImageHelper imageHelper_object = new ImageHelper();
            imageByte_toDB = imageHelper_object.getBytes(defaultImage_bitMap);
        }
        childDB_object.insertEntry(child_name_str, birth_str, gender_srt, imageByte_toDB);
        int childID = childDB_object.getChildID(child_name_str);

        childDB_object.close();

        Helper helper_object = new Helper(ChildRegister.this);
        helper_object.open();
        if (counter == 0) {
            helper_object.updateChild(childID + "", 1, username);


        } else if (counter == 1) {
            helper_object.updateChild(childID + "", 2, username);

        } else if (counter == 2) {
            helper_object.updateChild(childID + "", 3, username);

        }
        helper_object.close();
        Intent intent_to = new Intent(ChildRegister.this, ChildRecord.class);
        intent_to.putExtra("childID", childID);
        startActivity(intent_to);

    }

   /* public void cancel(View view) {
        Intent intent_to = new Intent(ChildRegister.this, Record.class);
        startActivity(intent_to);
    }*/
}





