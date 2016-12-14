package com.example.mayoo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mayoo on 12/9/2016.
 */

public class ChildRecord extends AppCompatActivity {
    String[] vacNames;

    listAdapter listAdapter_object;
    TextView record_child_birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_record);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 135, 165)));
        }

        Intent intent_from = getIntent();

        int childID = intent_from.getIntExtra("childID", 0);
        Log.d("ChildRecord", childID + "");

        ChildDB childDB_object = new ChildDB(ChildRecord.this);
        childDB_object.open();

        TextView record_child_name = (TextView) findViewById(R.id.record_child_name);
        record_child_birth = (TextView) findViewById(R.id.record_child_birth);
//        TextView record_child_gender = (TextView) findViewById(R.id.record_child_gender);
        ImageView record_child_img = (ImageView) findViewById(R.id.record_child_img);


        ArrayList<String> child_info = new ArrayList<>();
        if (childID != 0) {
            child_info = childDB_object.childInfo(childID);
        }

        record_child_name.setText(child_info.get(0));
        record_child_birth.setText(child_info.get(1));
//        record_child_gender.setText(child_info.get(2));

        byte[] imageByte_fromDB = childDB_object.gettingImage(childID);
        Bitmap imageBitmap_fromByte;
        InputStream inputStream = new ByteArrayInputStream(imageByte_fromDB);
        imageBitmap_fromByte = BitmapFactory.decodeStream(inputStream);
        record_child_img.setImageBitmap(imageBitmap_fromByte);


        Resources resources = getResources();
        vacNames = resources.getStringArray(R.array.vaccines);
        listAdapter_object = new ChildRecord.listAdapter(getApplicationContext(), vacNames);

        ListView lv = (ListView) findViewById(R.id.vac_listView);
        lv.setAdapter(listAdapter_object);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long row_id) {
                Log.d("ListView onItemClick", vacNames[position]);

                Intent intent_to = new Intent(ChildRecord.this, VacInfo.class);

                switch (vacNames[position]) {
                    case "Hepatitis B #1":
                    case "Hepatitis B #2":
                        intent_to.putExtra("layoutID", R.layout.vac_hepatits_b);
                        break;
                    case "Rotavirus #1":
                    case "Rotavirus #2":
                    case "Rotavirus #3":
                        intent_to.putExtra("layoutID", R.layout.vac_rotavirus);
                        break;
                    case "Diphtheria/Tetanus/Pertussis (DTaP) #1":
                    case "Diphtheria/Tetanus/Pertussis (DTaP) #2":
                    case "Diphtheria/Tetanus/Pertussis (DTaP) #3":
                        intent_to.putExtra("layoutID", R.layout.vac_dtap);
                        break;
                    case "Haemophilus influenzae type b #1":
                    case "Haemophilus influenzae type b #2":
                    case "Haemophilus influenzae type b #3":
                    case "Haemophilus influenzae type b #4":
                        intent_to.putExtra("layoutID", R.layout.vac_hib);
                        break;
                    case "Pneumococcal #1":
                    case "Pneumococcal #2":
                    case "Pneumococcal #3":
                    case "Pneumococcal #4":
                        intent_to.putExtra("layoutID", R.layout.vac_pneumococcal);
                        break;
                    case "Polio #1":
                    case "Polio #2":
                    case "Polio #3":
                        intent_to.putExtra("layoutID", R.layout.vac_polio);
                        break;
                    case "Measles/Mumps/Rubella #1":
                        intent_to.putExtra("layoutID", R.layout.vac_mmr);
                        break;
                    case "Chickenpox #1":
                        intent_to.putExtra("layoutID", R.layout.vac_chickenpox);
                        break;
                    case "Hepatitis A":
                        intent_to.putExtra("layoutID", R.layout.vac_hepatits_a);
                        break;
                    case "Influenza":
                        intent_to.putExtra("layoutID", R.layout.vac_influenza);
                        break;

                    default:
                        break;
                }

                startActivity(intent_to);

            }
        });
    }

    public class listAdapter extends ArrayAdapter<String> {

        Context context;
        String[] vacNames_adapter;


        listAdapter(Context c, String[] vacNames) {
            super(c, R.layout.child_vac_list, R.id.child_vac_list, vacNames);
            this.context = c;
            this.vacNames_adapter = vacNames;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ChildRecord.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.child_vac_list, parent, false);
            }

            TextView userName_searchResult = (TextView) convertView.findViewById(R.id.child_vac_list);

            userName_searchResult.setText(vacNames_adapter[position]);

            convertView.setBackgroundResource(R.color.transparent);

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            String currentDateandTime = sdf.format(new Date());


            String choldBirth = record_child_birth.getText().toString();
            DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
            Date date1 = null, date2 = null;
            try {
                date1 = format.parse(choldBirth);
                date2 = format.parse(currentDateandTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            long diffInMillies = date2.getTime() - date1.getTime();
            long dateDiff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            Log.d("dateDiff", dateDiff + ", "+currentDateandTime+", "+record_child_birth.getText().toString());

            Double months = dateDiff / 30.0;
           // Log.d("months", months + "");


            //BIRTH
            if (months < 1) {
                Log.d("BIRTH", months + ", " + vacNames_adapter[position]);
                if (vacNames_adapter[position].equals("Hepatitis B #1")) {
                    convertView.setBackgroundResource(R.color.purple);
                }

                //1-2 MONTHS
            } else if (months >= 1 && months < 2) {
                Log.d("1_2", months + ", " + vacNames_adapter[position]);
                if (vacNames_adapter[position].equals("Hepatitis B #2")) {

                    convertView.setBackgroundResource(R.color.purple);
                }

                //2 MONTHS
            } else if (months >= 2 && months < 3) {
                Log.d("2MONTHS", months + ", " + vacNames_adapter[position]);
                if (
                        vacNames_adapter[position].equals("Rotavirus #1")
                                ||
                                vacNames_adapter[position].equals("Diphtheria/Tetanus/Pertussis (DTaP) #1")
                                ||
                                vacNames_adapter[position].equals("Haemophilus influenzae type b #1")
                                ||
                                vacNames_adapter[position].equals("Pneumococcal #1")
                                ||
                                vacNames_adapter[position].equals("Polio #1")) {
                    Log.d("2MONTHS_color", months + ", " + vacNames_adapter[position]+" == "+ position);

                    convertView.setBackgroundResource(R.color.purple);
                }

                //4 MONTHS
            } else if (months >= 4 && months < 5) {
                Log.d("4months", months + ", " + vacNames_adapter[position]);
                if (vacNames_adapter[position].equals("Rotavirus #2")
                        ||
                        vacNames_adapter[position].equals("Diphtheria/Tetanus/Pertussis (DTaP) #2")
                        ||
                        vacNames_adapter[position].equals("Haemophilus influenzae type b #2")
                        ||
                        vacNames_adapter[position].equals("Pneumococcal #2")
                        ||
                        vacNames_adapter[position].equals("Polio #2")) {
                    convertView.setBackgroundResource(R.color.purple);
                }

                //6-12 MONTHS
            } else if (months >= 6 && months < 12) {

                //6 MONTHS
                if (months >= 6 && months < 7) {
                    Log.d("6months", months + ", " + vacNames_adapter[position]);
                    if (vacNames_adapter[position].equals("Rotavirus #3")
                            ||
                            vacNames_adapter[position].equals("Diphtheria/Tetanus/Pertussis (DTaP) #3")
                            ||
                            vacNames_adapter[position].equals("Haemophilus influenzae type b #3")
                            ||
                            vacNames_adapter[position].equals("Pneumococcal #3")
                            ||
                            vacNames_adapter[position].equals("Influenza")) {
                        convertView.setBackgroundResource(R.color.purple);
                    }
                }

                Log.d("6_12", months + ", " + vacNames_adapter[position]);
                if (vacNames_adapter[position].equals("Hepatitis B #3") || vacNames_adapter[position].equals("Polio #3")) {
                    convertView.setBackgroundResource(R.color.purple);
                }

                //12-15 months
            } else if (months >= 12 && months < 15) {
                Log.d("12_15", months + ", " + vacNames_adapter[position]);
                if (vacNames_adapter[position].equals("Measles/Mumps/Rubella #1")
                        ||
                        vacNames_adapter[position].equals("Chickenpox #1")

                        ||
                        vacNames_adapter[position].equals("Haemophilus influenzae type b #4")
                        ||
                        vacNames_adapter[position].equals("Pneumococcal #4")) {
                    convertView.setBackgroundResource(R.color.purple);
                }

                //15-18 MONTHS
            } else if (months >= 15 && months < 18) {
                Log.d("15_18", months + ", " + vacNames_adapter[position]);
                if (vacNames_adapter[position].equals("Diphtheria/Tetanus/Pertussis (DTaP) #4")) {
                    convertView.setBackgroundResource(R.color.purple);
                }

                //18-23 MONTHS
            } else if (months >= 18 && months < 23) {
                Log.d("18_23", months + ", " + vacNames_adapter[position]);
                if (vacNames_adapter[position].equals("Hepatitis A")) {
                    convertView.setBackgroundResource(R.color.purple);
                }
            }

            return convertView;
        }


    }

    /**
     * Get a diff between two dates
     *
     * @param date1    the oldest date
     * @param date2    the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        Log.d("getDateDiff", timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS) + "");
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


}


