package com.example.mayoo.myapplication;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by mayoo on 12/9/2016.
 */

public class ChildRecord extends AppCompatActivity {
    String[] vacNames;

    listAdapter listAdapter_object;
    TextView record_child_birth;
    int childID;
    ArrayList<String> child_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_record);

        String LOG_TAG = "ChildRecord";
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            Log.e(LOG_TAG, "Dumping Intent start");
            while (it.hasNext()) {
                String key = it.next();
                Log.e(LOG_TAG, "[" + key + "=" + bundle.get(key) + "]");
            }
            Log.e(LOG_TAG, "Dumping Intent end");
        } else {
            Log.e(LOG_TAG, "bundle is empty");
        }


        Intent intent_from = getIntent();

        childID = intent_from.getIntExtra("childID", 0);
        Log.d("ChildRecord", childID + "");

        ChildDB childDB_object = new ChildDB(ChildRecord.this);
        childDB_object.open();

        TextView record_child_name = (TextView) findViewById(R.id.record_child_name);
        record_child_birth = (TextView) findViewById(R.id.record_child_birth);
        ImageView record_child_gender = (ImageView) findViewById(R.id.record_child_gender_btn);
        ImageView record_child_img = (ImageView) findViewById(R.id.record_child_img);


        child_info = new ArrayList<>();
        if (childID != 0) {
            child_info = childDB_object.childInfo(childID);
        }

        record_child_name.setText(child_info.get(0));

        Log.d("mk", Integer.parseInt(childBirthCalc()[1]) + "");
        if ((int) (Double.parseDouble(childBirthCalc()[0])) <= 1) {
            if (Integer.parseInt(childBirthCalc()[1]) % 30 == 0 && (int) (Double.parseDouble(childBirthCalc()[0])) == 0) {
                record_child_birth.setText("Born today");
            } else {
                if ((int) (Double.parseDouble(childBirthCalc()[0])) == 1) {

                    if (Integer.parseInt(childBirthCalc()[1]) % 30 == 0) {
                        record_child_birth.setText("1 month ");

                    } else {
                        record_child_birth.setText("1 month, " + (Integer.parseInt(childBirthCalc()[1]) % 30 == 1 ?
                                Integer.parseInt(childBirthCalc()[1]) % 30 + (" day") : Integer.parseInt(childBirthCalc()[1]) % 30 + " days"));
                    }
                } else
                    record_child_birth.setText(Integer.parseInt(childBirthCalc()[1]) % 30 == 1 ?
                            Integer.parseInt(childBirthCalc()[1]) % 30 + (" day") : Integer.parseInt(childBirthCalc()[1]) % 30 + " days");
            }
        } else {
            if (Integer.parseInt(childBirthCalc()[1]) % 30 == 0) {
                record_child_birth.setText((int) (Double.parseDouble(childBirthCalc()[0])) + " months");

            } else
                record_child_birth.setText((int) (Double.parseDouble(childBirthCalc()[0])) + " months, " + (Integer.parseInt(childBirthCalc()[1]) % 30 == 1 ?
                        Integer.parseInt(childBirthCalc()[1]) % 30 + (" day") : Integer.parseInt(childBirthCalc()[1]) % 30 + " days"));
        }

        int child_img_res;
        if (child_info.get(2).equals("Female")) {
            child_img_res = R.drawable.female;
        } else {
            child_img_res = R.drawable.male;
        }
        record_child_gender.setImageResource(child_img_res);


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

        setAlarm();

    }

    public void setAlarm() {
        long days = Long.parseLong(childBirthCalc()[1]);
        double mMonths = Double.parseDouble(childBirthCalc()[0]);

        Calendar cal = Calendar.getInstance();
        Log.d("xfdf", cal.getTime().toString());

        int childAge = setAlarm_1stTime(days, mMonths, cal);

        Log.d("xfdf", cal.getTime().toString());

        //18-23 mMonths



        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");

        notificationIntent.putExtra("childID", childID);
        notificationIntent.putExtra("username", getIntent().getStringExtra("username"));
        notificationIntent.putExtra("child#", getIntent().getIntExtra("child#", 0));
        notificationIntent.putExtra("childAge", childAge);


        notificationIntent.addCategory("android.intent.category.VAC");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, childID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (mMonths < 18) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            }
        }
    }

    public int setAlarm_1stTime(long days, double mMonths, Calendar cal) {

        //BIRTH
        if (mMonths < 1) {
            cal.add(Calendar.DAY_OF_MONTH, (int) (30 - days));
            return 1;
        }
        //1-2 mMonths
        if (mMonths >= 1 && mMonths < 2) {
            cal.add(Calendar.DAY_OF_MONTH, (int) (60 - days));
            return 2;

            //2 mMonths
        } else if (mMonths >= 2 && mMonths < 3) {
            cal.add(Calendar.DAY_OF_MONTH, (int) (120 - days));
            return 4;

            //4 mMonths
        } else if (mMonths >= 4 && mMonths < 5) {
            cal.add(Calendar.DAY_OF_MONTH, (int) (180 - days));
            return 6;

            //6-12 mMonths
        } else if (mMonths >= 6 && mMonths < 12) {
            cal.add(Calendar.DAY_OF_MONTH, (int) (360 - days));
            return 12;

            //12-15 mMonths
        } else if (mMonths >= 12 && mMonths < 15) {
            cal.add(Calendar.DAY_OF_MONTH, (int) (450 - days));
            return 15;

            //15-18 mMonths
        } else if (mMonths >= 15 && mMonths < 18) {
            cal.add(Calendar.DAY_OF_MONTH, (int) (540 - days));
            return 18;

        } else {
            return 0;
        }
    }

    //region only used when the user is coming from the notification
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Log.d("onOptionsItemSelected", "yeeeeeeeeeeeeeeees");
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
    //endregion

    @Override
    public void onBackPressed() {
        Intent intent_from = getIntent();
        Intent intent_to = new Intent(ChildRecord.this, Record.class);
        intent_to.putExtra("username", intent_from.getStringExtra("username"));
        intent_to.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_to);
        finish();

        ChildRecord.super.onBackPressed();
    }

    public void edit(View view) {
        Intent intent_to = new Intent(ChildRecord.this, ChildRegister.class);
        Log.d("ChildRecord", childID + "");
        intent_to.putExtra("childID", childID);
        intent_to.putExtra("Edit", 1);
        Intent intent_from = getIntent();
        intent_to.putExtra("username", intent_from.getStringExtra("username"));

        // finish();


        startActivity(intent_to);

    }

    public void delete(View view) {
        Intent intent_from = getIntent();
        final String username = intent_from.getStringExtra("username");
        final int childNumber = intent_from.getIntExtra("child#", 0);


        final Dialog dialog = new Dialog(ChildRecord.this);
        dialog.getWindow();
        dialog.setContentView(R.layout.delete_record);
        dialog.show();


        Button yes_btn = (Button) dialog.findViewById(R.id.yes_btn);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChildDB childDB_obj = new ChildDB(ChildRecord.this);
                childDB_obj.open();
                childDB_obj.deleteRecord(childID);
                childDB_obj.close();


                Helper helper_obj = new Helper(ChildRecord.this);
                helper_obj.open();
                Log.d("kdd", childNumber + ", " + username);
                helper_obj.updateChild("-", childNumber, username);
                helper_obj.close();
                dialog.dismiss();
                finish();
                Intent intent_to = new Intent(ChildRecord.this, Record.class);
                intent_to.putExtra("username", username);
                startActivity(intent_to);
            }
        });

        Button no_btn = (Button) dialog.findViewById(R.id.no_btn);
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    public class listAdapter extends ArrayAdapter<String> {

        Context context;
        String[] vacNames_adapter;
        Double months;

        listAdapter(Context c, String[] vacNames) {
            super(c, R.layout.child_vac_list, R.id.vac_name_list_item, vacNames);
            this.context = c;
            this.vacNames_adapter = vacNames;

            months = Double.parseDouble(childBirthCalc()[0]);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ChildRecord.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.child_vac_list, parent, false);
            }

            TextView vac_name_list_item = (TextView) convertView.findViewById(R.id.vac_name_list_item);

            vac_name_list_item.setText(vacNames_adapter[position]);

            convertView.setBackgroundResource(R.color.transparent);

            doneVacs(convertView, vacNames_adapter[position], months);

            currentVacs(convertView, vacNames_adapter[position], months);


            return convertView;
        }


    }

    private void doneVacs(View convertView, String vacName, Double mMonths) {
        Log.d("doneVacs", mMonths + "");

        ArrayList<String> doneVacs_list = new ArrayList<>();

        if (mMonths >= 1) {
            Log.d("BIRTH", mMonths + ", " + vacName);
            doneVacs_list.add("Hepatitis B #1");

        }

        if (mMonths >= 2) {
            Log.d("1_2", mMonths + ", " + vacName);
            doneVacs_list.add("Hepatitis B #2");


        }

        if (mMonths >= 3) {
            Log.d("2mMonths", mMonths + ", " + vacName);
            doneVacs_list.addAll(Arrays.asList("Rotavirus #1", "Diphtheria/Tetanus/Pertussis (DTaP) #1",
                    "Haemophilus influenzae type b #1", "Pneumococcal #1", "Polio #1"));

        }

        if (mMonths >= 5) {
            Log.d("4mMonths", mMonths + ", " + vacName);
            doneVacs_list.addAll(Arrays.asList("Rotavirus #2", "Diphtheria/Tetanus/Pertussis (DTaP) #2",
                    "Haemophilus influenzae type b #2", "Pneumococcal #2", "Polio #2"));


        }

        if (mMonths >= 7) {
            Log.d("6mMonths", mMonths + ", " + vacName);
            doneVacs_list.addAll(Arrays.asList("Rotavirus #3", "Diphtheria/Tetanus/Pertussis (DTaP) #3",
                    "Haemophilus influenzae type b #3", "Pneumococcal #3", "Influenza"));
        }

        if (mMonths >= 12) {
            Log.d("6_12", mMonths + ", " + vacName);
            doneVacs_list.addAll(Arrays.asList("Hepatitis B #3", "Polio #3"));
        }

        if (mMonths >= 15) {
            Log.d("12_15", mMonths + ", " + vacName);
            doneVacs_list.addAll(Arrays.asList("Measles/Mumps/Rubella #1", "Chickenpox #1", "Haemophilus influenzae type b #4",
                    "Pneumococcal #4"));
        }

        if (mMonths >= 18) {
            Log.d("15_18", mMonths + ", " + vacName);
            doneVacs_list.add("Diphtheria/Tetanus/Pertussis (DTaP) #4");
        }

        if (mMonths >= 23) {
            Log.d("18_23", mMonths + ", " + vacName);
            doneVacs_list.add("Hepatitis A");
        }

        for (int i = 0; i < doneVacs_list.size(); i++) {
            if (doneVacs_list.get(i).equals(vacName)) {
                convertView.setBackgroundResource(R.color.doneVac);
            }
        }
        TextView vac_left = (TextView) findViewById(R.id.vac_left);
        vac_left.setText(26 - doneVacs_list.size() + "");
    }

    private void currentVacs(View convertView, String vacName, Double mMonths) {
        Log.d("currentVacs", mMonths + "");
        //BIRTH
        if (mMonths < 1) {
            Log.d("BIRTH", mMonths + ", " + vacName);
            if (vacName.equals("Hepatitis B #1")) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //1-2 mMonths
        } else if (mMonths >= 1 && mMonths < 2) {
            Log.d("1_2", mMonths + ", " + vacName);
            if (vacName.equals("Hepatitis B #2")) {

                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //2 mMonths
        } else if (mMonths >= 2 && mMonths < 3) {
            Log.d("2mMonths", mMonths + ", " + vacName);
            if (
                    vacName.equals("Rotavirus #1")
                            ||
                            vacName.equals("Diphtheria/Tetanus/Pertussis (DTaP) #1")
                            ||
                            vacName.equals("Haemophilus influenzae type b #1")
                            ||
                            vacName.equals("Pneumococcal #1")
                            ||
                            vacName.equals("Polio #1")) {

                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //4 mMonths
        } else if (mMonths >= 4 && mMonths < 5) {
            Log.d("4mMonths", mMonths + ", " + vacName);
            if (vacName.equals("Rotavirus #2")
                    ||
                    vacName.equals("Diphtheria/Tetanus/Pertussis (DTaP) #2")
                    ||
                    vacName.equals("Haemophilus influenzae type b #2")
                    ||
                    vacName.equals("Pneumococcal #2")
                    ||
                    vacName.equals("Polio #2")) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //6-12 mMonths
        } else if (mMonths >= 6 && mMonths < 12) {

            //6 mMonths
            if (mMonths >= 6 && mMonths < 7) {
                Log.d("6mMonths", mMonths + ", " + vacName);
                if (vacName.equals("Rotavirus #3")
                        ||
                        vacName.equals("Diphtheria/Tetanus/Pertussis (DTaP) #3")
                        ||
                        vacName.equals("Haemophilus influenzae type b #3")
                        ||
                        vacName.equals("Pneumococcal #3")
                        ||
                        vacName.equals("Influenza")) {
                    convertView.setBackgroundResource(R.color.tranPurple);
                }
            }

            Log.d("6_12", mMonths + ", " + vacName);
            if (vacName.equals("Hepatitis B #3") || vacName.equals("Polio #3")) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //12-15 mMonths
        } else if (mMonths >= 12 && mMonths < 15) {
            Log.d("12_15", mMonths + ", " + vacName);
            if (vacName.equals("Measles/Mumps/Rubella #1")
                    ||
                    vacName.equals("Chickenpox #1")

                    ||
                    vacName.equals("Haemophilus influenzae type b #4")
                    ||
                    vacName.equals("Pneumococcal #4")) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //15-18 mMonths
        } else if (mMonths >= 15 && mMonths < 18) {
            Log.d("15_18", mMonths + ", " + vacName);
            if (vacName.equals("Diphtheria/Tetanus/Pertussis (DTaP) #4")) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //18-23 mMonths
        } else if (mMonths >= 18 && mMonths < 23) {
            Log.d("18_23", mMonths + ", " + vacName);
            if (vacName.equals("Hepatitis A")) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }
        }
    }

    public String[] childBirthCalc() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        // String currentDateandTime = sdf.format(new Date());
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());


        String childBirth = child_info.get(1);//record_child_birth.getText().toString();
        DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        Date date1 = null, date2 = null;
        try {
            date1 = format.parse(childBirth);
            date2 = format.parse(currentDateandTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        long diffInMillies = date2.getTime() - date1.getTime();
        long dateDiff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        Log.d("dateDiff", dateDiff + ", " + currentDateandTime + ", " + record_child_birth.getText().toString());

        return new String[]{dateDiff / 30.0 + "", dateDiff + ""};
    }
}


