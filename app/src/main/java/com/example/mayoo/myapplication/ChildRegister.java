package com.example.mayoo.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by mayoo on 11/25/2016.
 */

public class ChildRegister extends AppCompatActivity {

    int childID_edit, childID_new;
    ArrayList<String> child_info;

    EditText birth_editText;
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



        myCalendar = Calendar.getInstance();

        EditText child_name_editText = (EditText) findViewById(R.id.child_name_editText);
        birth_editText = (EditText) findViewById(R.id.birth_edit_text);
        RadioGroup gender_radioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
        ImageView record_child_img = (ImageView) findViewById(R.id.childPhotoBtn);

        Intent editIntent_from = getIntent();

        if (editIntent_from.getIntExtra("Edit", 0) == 1) {

            Intent intent_from = getIntent();

            childID_edit = intent_from.getIntExtra("childID", 0);
            Log.d("ChildRecord", childID_edit + "");

            ChildDB childDB_object = new ChildDB(ChildRegister.this);
            childDB_object.open();


            child_info = new ArrayList<>();
            if (childID_edit != 0) {
                child_info = childDB_object.childInfo(childID_edit);
            }


           /* String childBirth = child_info.get(1);
            DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
            Date date1 = null;
            try {
                 date1 = format.parse(childBirth);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(date1 != null) {
                myCalendar.set(Calendar.YEAR, date1.getYear());
                myCalendar.set(Calendar.MONTH, date1.getMonth());
                myCalendar.set(Calendar.DAY_OF_MONTH, date1.getDay());
            }*/


            child_name_editText.setText(child_info.get(0));
            birth_editText.setText(child_info.get(1));
            if (child_info.get(2).equals("Female")) {
                gender_radioGroup.check(R.id.female_radioButton);
            } else {
                gender_radioGroup.check(R.id.male_radioButton);
            }


            img_added = true;

            imageByte_toDB = childDB_object.gettingImage(childID_edit);
            Bitmap imageBitmap_fromByte;
            InputStream inputStream = new ByteArrayInputStream(imageByte_toDB);
            imageBitmap_fromByte = BitmapFactory.decodeStream(inputStream);
            record_child_img.setImageBitmap(imageBitmap_fromByte);
        }


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //Log.d("onDateSet", year+", "+(++monthOfYear)+", "+ dayOfMonth);
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        final DatePickerDialog datePickerDialog = new DatePickerDialog(ChildRegister.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        birth_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !datePickerDialog.isShowing()) {
                    Log.d("onFocus", myCalendar.get(Calendar.YEAR) + ", " + myCalendar.get(Calendar.MONTH) + ", "
                            + myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            }

        });

        birth_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!datePickerDialog.isShowing()) {
                    Log.d("onFocus", myCalendar.get(Calendar.YEAR) + ", " + myCalendar.get(Calendar.MONTH) + ", "
                            + myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            }

        });


    }

    private void updateLabel() {

        Log.d("updateLabel", "jghf");
        String myFormat = "MM-dd-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birth_editText.setText(sdf.format(myCalendar.getTime()));
    }


    public void child_image(View view) {
        verifyStoragePermissions(ChildRegister.this);

        //Create intent to Open ImageHelper applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        Log.d("zzzzzzzzz", "child_image");

        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Log.d("zzzzzzzzz", "onActivityResult");

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
        Log.d("zzzzzzzzz", "verifyStoragePermissions");

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

        if (!img_added) {
            Bitmap defaultImage_bitMap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.kiddo);
            ImageHelper imageHelper_object = new ImageHelper();
            imageByte_toDB = imageHelper_object.getBytes(defaultImage_bitMap);
        }

        Intent intent_to = new Intent(ChildRegister.this, ChildRecord.class);


        Intent editIntent_from = getIntent();

        if (editIntent_from.getIntExtra("Edit", 0) == 1) {
            editChild(child_name_str, birth_str, gender_srt);
            intent_to.putExtra("childID", childID_edit);

        } else {
            newChild(username, child_name_str, birth_str, gender_srt, counter);
            intent_to.putExtra("childID", childID_new);
        }


        intent_to.putExtra("username", username);
        intent_to.putExtra("child#", ++counter);
        finish();
        startActivity(intent_to);

    }

    private void newChild(String username, String child_name_str, String birth_str, String gender_srt, int counter) {
        ChildDB childDB_object = new ChildDB(ChildRegister.this);
        childDB_object.open();
        childDB_object.insertEntry(child_name_str, birth_str, gender_srt, imageByte_toDB);
        childID_new = childDB_object.getChildID(child_name_str);

        childDB_object.close();

        Helper helper_object = new Helper(ChildRegister.this);
        helper_object.open();
        if (counter == 0) {
            helper_object.updateChild(childID_new + "", 1, username);


        } else if (counter == 1) {
            helper_object.updateChild(childID_new + "", 2, username);

        } else if (counter == 2) {
            helper_object.updateChild(childID_new + "", 3, username);

        }
        helper_object.close();
    }

    private void editChild(String child_name_str, String birth_str, String gender_srt) {
        ChildDB childDB_object = new ChildDB(ChildRegister.this);
        childDB_object.open();
        childDB_object.updateChildInfo(childID_edit, child_name_str, birth_str, gender_srt);

        childDB_object.updateImage(imageByte_toDB, childID_edit);
        childDB_object.close();

    }


    public void cancel(View view) {
        finish();

    }
}





