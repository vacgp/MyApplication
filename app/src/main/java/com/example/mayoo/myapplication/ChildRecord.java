package com.example.mayoo.myapplication;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.Locale;
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
    int childNo;

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
        Helper helperObject = new Helper(ChildRecord.this);
        helperObject.open();
        childNo =helperObject.getChildNumber(childID+"", getIntent().getStringExtra("username"));
        helperObject.close();

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

        int days = Integer.parseInt(childBirthCalc(child_info.get(1))[1]);
        double months = Double.parseDouble(childBirthCalc(child_info.get(1))[0]);

        Log.d("mk", days + "");
        if ((int) months <= 1) {
            if (days % 30 == 0 && (int) months == 0) {
                record_child_birth.setText(R.string.born_today);
            } else {
                if ((int) months == 1) {

                    if (days % 30 == 0) {
                        record_child_birth.setText(R.string.month);

                    } else {
                        record_child_birth.setText(getString(R.string.month_) + (days % 30 == 1 ? days % 30 + (getString(R.string.day)) : days % 30 + getString(R.string.days)));
                    }
                } else
                    record_child_birth.setText(days % 30 == 1 ? days % 30 + (getString(R.string.day)) : days % 30 + getString(R.string.days));
            }
        } else {
            if (days % 30 == 0) {
                record_child_birth.setText((int) (months) + getString(R.string.months));

            } else
                record_child_birth.setText((int) (months) + getString(R.string.months_) + (days % 30 == 1 ? days % 30 + (getString(R.string.day)) : days % 30 + getString(R.string.days)));
        }

        int child_img_res;
        if (child_info.get(2).equals (getString(R.string.female))) {
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

                Locale locale = Locale.getDefault();
                if (locale.toString().startsWith("ar")){
                    switch (vacNames[position]) {
                        case "الالتهاب الكبدي ب #1":
                        case "الالتهاب الكبدي ب #2":
                        case "الالتهاب الكبدي ب #3":
                        case "الالتهاب الكبدي ب #4":
                            intent_to.putExtra("layoutID", R.layout.vac_hepatits_b);
                            break;
                        case "فيروس الروتا #1":
                        case "فيروس الروتا #2":
                            intent_to.putExtra("layoutID", R.layout.vac_rotavirus);
                            break;
                        case "الثلاثي البكتيري #1":
                        case "الثلاثي البكتيري #2":
                        case "الثلاثي البكتيري #3":
                        case "الثلاثي البكتيري #4":

                            intent_to.putExtra("layoutID", R.layout.vac_dtap);
                            break;
                        case "المستدمية النزلية #1":
                        case "المستدمية النزلية #2":
                        case "المستدمية النزلية #3":
                        case "المستدمية النزلية #4":

                            intent_to.putExtra("layoutID", R.layout.vac_hib);
                            break;
                        case "المكورات الرئوية #1":
                        case "المكورات الرئوية #2":
                        case "المكورات الرئوية #3":
                        case "المكورات الرئوية #4":

                            intent_to.putExtra("layoutID", R.layout.vac_pneumococcal);
                            break;
                        case "شلل الأطفال #1":
                        case "شلل الأطفال #2":
                        case "شلل الأطفال#3":
                        case "شلل الأطفال #4":
                        case "شلل الأطفال #5":

                            intent_to.putExtra("layoutID", R.layout.vac_polio);
                            break;
                        case "الثلاثي الفيروسي #1":
                        case "الثلاثي الفيروسي #2":
                        case "الثلاثي الفيروسي #3":

                            intent_to.putExtra("layoutID", R.layout.vac_mmr);
                            break;
                        case "الجدري":
                            intent_to.putExtra("layoutID", R.layout.vac_chickenpox);
                            break;
                        case "الالتهاب الكبدي أ #1":
                        case "الالتهاب الكبدي أ #2":

                            intent_to.putExtra("layoutID", R.layout.vac_hepatits_a);
                            break;

                        case "الدرن":
                            intent_to.putExtra("layoutID", R.layout.vac_bcg);
                            break;
                        case "الحمى الشوكية #1":
                        case "الحمى الشوكية #2":

                            intent_to.putExtra("layoutID", R.layout.vac_mcv4);
                            break;

                        default:
                            break;
                    }
                } else {
                    switch (vacNames[position]) {
                        case "Hepatitis B #1":
                        case "Hepatitis B #2":
                        case "Hepatitis B #3":
                        case "Hepatitis B #4":
                            intent_to.putExtra("layoutID", R.layout.vac_hepatits_b);
                            break;
                        case "Rotavirus #1":
                        case "Rotavirus #2":
                            intent_to.putExtra("layoutID", R.layout.vac_rotavirus);
                            break;
                        case "Diphtheria/Tetanus/Pertussis (DTaP) #1":
                        case "Diphtheria/Tetanus/Pertussis (DTaP) #2":
                        case "Diphtheria/Tetanus/Pertussis (DTaP) #3":
                        case "Diphtheria/Tetanus/Pertussis (DTaP) #4":
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
                        case "Polio #4":
                        case "Polio #5":
                            intent_to.putExtra("layoutID", R.layout.vac_polio);
                            break;
                        case "Measles/Mumps/Rubella #1":
                        case "Measles/Mumps/Rubella #2":
                        case "Measles/Mumps/Rubella #3":

                            intent_to.putExtra("layoutID", R.layout.vac_mmr);
                            break;
                        case "Chickenpox":
                            intent_to.putExtra("layoutID", R.layout.vac_chickenpox);
                            break;
                        case "Hepatitis A #1":
                        case "Hepatitis A #2":
                            intent_to.putExtra("layoutID", R.layout.vac_hepatits_a);
                            break;

                        case "BCG":
                            intent_to.putExtra("layoutID", R.layout.vac_bcg);
                            break;
                        case "MCV4 #1":
                        case "MCV4 #2":
                            intent_to.putExtra("layoutID", R.layout.vac_mcv4);
                            break;

                        default:
                            break;
                    }
                }




                startActivity(intent_to);

            }
        });

        setAlarm(childID, child_info.get(1), getIntent().getStringExtra("username"), childNo);

    }

    public void setAlarm(int childID, String childBirth,  String username, int childNumber) {
        long days = Long.parseLong(childBirthCalc(childBirth)[1]);
        double mMonths = Double.parseDouble(childBirthCalc(childBirth)[0]);

        Calendar cal = Calendar.getInstance();
        Log.d("xfdf", cal.getTime().toString());

        int childAge = setCalender(days, mMonths, cal);

        Log.d("xfdf", cal.getTime().toString());

        //18-23 mMonths

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");

        notificationIntent.putExtra("childID", childID);
        notificationIntent.putExtra("username", username);
        notificationIntent.putExtra("child#", childNumber);
        notificationIntent.putExtra("childAge", childAge);


        notificationIntent.addCategory("android.intent.category.VAC");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, childID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (mMonths <= 18 && childAge != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            }
        }
    }

    public int setCalender(long days, double mMonths, Calendar cal) {

        Log.d("setCalender", mMonths+"");

        //BIRTH
        if (mMonths <= 1) {
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

            //6 mMonths
        } else if (mMonths >= 6 && mMonths < 9) {

            cal.add(Calendar.DAY_OF_MONTH, (int) (270 - days));
            return 9;

            //9 mMonths
        } else if (mMonths >= 9 && mMonths < 12) {
            cal.add(Calendar.DAY_OF_MONTH, (int) (360 - days));
            return 12;


            //12 mMonths
        } else if (mMonths >= 12 && mMonths < 18) {
            cal.add(Calendar.DAY_OF_MONTH, (int) (540 - days));
            return 18;

            //18 mMonths
        } else if (mMonths >= 18 && mMonths < 24) {
            cal.add(Calendar.DAY_OF_MONTH, (int) (720 - days));
            return 24;

        }  else {
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
        //final int childNumber = intent_from.getIntExtra("child#", 0);


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
                Log.d("kdd", childNo + ", " + username);
                helper_obj.shiftingChildIDs(username, childID);
                helper_obj.close();
                new Home().cancelAlarm(childID, ChildRecord.this);
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

            months = Double.parseDouble(childBirthCalc(child_info.get(1))[0]);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ChildRecord.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.child_vac_list, parent, false);
            }

            TextView vac_name_list_item = (TextView) convertView.findViewById(R.id.vac_name_list_item);

            vac_name_list_item.setText(vacNames_adapter[position]);

            convertView.setBackgroundResource(R.color.white);

            doneVacs(convertView, vacNames_adapter[position], months);

            currentVacs(convertView, vacNames_adapter[position], months);


            return convertView;
        }


    }

    private void doneVacs(View convertView, String vacName, Double mMonths) {
        Log.d("doneVacs", mMonths + "");

        ArrayList<String> doneVacs_list = new ArrayList<>();

        if (mMonths >= 0.2) {
            Log.d("BIRTH", mMonths + ", " + vacName);
            doneVacs_list.add(getString(R.string.bcg_vac));
            doneVacs_list.add(getString(R.string.hep_b_1));

        }

        if (mMonths >= 3) {
            Log.d("2", mMonths + ", " + vacName);
            doneVacs_list.addAll(Arrays.asList(getString(R.string.hep_b_2), getString(R.string.rotavirus_1), getString(R.string.dtap_1),
                    getString(R.string.hib_1), getString(R.string.pneumococcal_1), getString(R.string.polio_1)));

        }

        if (mMonths >= 5) {
            Log.d("4", mMonths + ", " + vacName);
            doneVacs_list.addAll(Arrays.asList(getString(R.string.hep_b_3), getString(R.string.rotavirus_2), getString(R.string.dtap_2),
                    getString(R.string.hib_2), getString(R.string.Pneumococcal_2), getString(R.string.Polio_2)));

        }

        if (mMonths >= 7) {
            Log.d("6", mMonths + ", " + vacName);
            doneVacs_list.addAll(Arrays.asList(getString(R.string.hep_b_4), getString(R.string.polio_3), getString(R.string.dtap_3),
                    getString(R.string.hib_3), getString(R.string.Pneumococcal_3)));


        }

        if (mMonths >= 10) {
            Log.d("9", mMonths + ", " + vacName);
            doneVacs_list.addAll(Arrays.asList(getString(R.string.mmr_1), getString(R.string.MCV4_1)));
        }

        if (mMonths >= 13) {
            Log.d("12", mMonths + ", " + vacName);
            doneVacs_list.addAll(Arrays.asList(getString(R.string.Pneumococcal_4), getString(R.string.MCV4_2), getString(R.string.mmr_2), getString(R.string.polio_4)));
        }

        if (mMonths >= 19) {
            Log.d("18", mMonths + ", " + vacName);
            doneVacs_list.addAll(Arrays.asList(getString(R.string.polio_5), getString(R.string.mmr_3), getString(R.string.dtap_4),
                    getString(R.string.Chickenpox), getString(R.string.hib_4), getString(R.string.hep_a_1)));
        }

        if (mMonths >= 25) {
            Log.d("24", mMonths + ", " + vacName);
            doneVacs_list.add(getString(R.string.hep_a_2));
        }


        for (int i = 0; i < doneVacs_list.size(); i++) {
            if (doneVacs_list.get(i).equals(vacName)) {
                convertView.setBackgroundResource(R.color.doneVac);
            }
        }
        TextView vac_left = (TextView) findViewById(R.id.vac_left);
        vac_left.setText(32 - doneVacs_list.size() + "");
    }

    //region At Birth
   /* <item>BCG</item>
    <item>Hepatitis B #1</item>*/
    //endregion

    //region 2 Months
   /* <item>Polio #1</item>
    <item>Diphtheria/Tetanus/Pertussis (DTaP) #1</item>
    <item>Hepatitis B #2</item>
    <item>Haemophilus influenzae type b #1</item>
    <item>Pneumococcal #1</item>
    <item>Rotavirus #1</item>*/
    //endregion

    //region 4 Months
    /*<item>Polio #2</item>
        <item>Diphtheria/Tetanus/Pertussis (DTaP) #2</item>
        <item>Hepatitis B #3</item>
        <item>Haemophilus influenzae type b #2</item>
        <item>Pneumococcal #2</item>
        <item>Rotavirus #2</item>*/
    //endregion

    //region 6 Months
   /* <item>Polio #3</item>
    <item>Diphtheria/Tetanus/Pertussis (DTaP) #3</item>
    <item>Hepatitis B #4</item>
    <item>Haemophilus influenzae type b #3</item>
    <item>Pneumococcal #3</item>*/
    //endregion

    //region 9 Months
/*
    <item>Measles/Mumps/Rubella #1</item>
    <item>MCV4 #1</item>*/
    //endregion

    //region 12 Months
  /*  <item>Polio #4</item>
    <item>Measles/Mumps/Rubella #2</item>
    <item>Pneumococcal #4</item>
    <item>MCV4 #2</item>*/
    //endregion

    //region 18 Months
  /*  <item>Polio #5</item>
    <item>Diphtheria/Tetanus/Pertussis (DTaP) #4</item>
    <item>Haemophilus influenzae type b #4</item>
    <item>Measles/Mumps/Rubella #3</item>
    <item>Hepatitis A #1</item>
    <item>Chickenpox</item>*/

    //endregion

    //region 24 Months
/*
    <item>Hepatitis A #2</item>
*/

    //endregion
    private void currentVacs(View convertView, String vacName, Double mMonths) {
        Log.d("currentVacs", mMonths + "");
        //BIRTH
        if (mMonths < 1) {
            Log.d("BIRTH", mMonths + ", " + vacName);

            if (vacName.equals(getResources().getString(R.string.hep_b_1)) || vacName.equals(getResources().getString(R.string.bcg_vac))) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //2 mMonths
        } else if (mMonths >= 2 && mMonths < 3) {
            Log.d("2mMonths", mMonths + ", " + vacName);
            if (vacName.equals(getResources().getString(R.string.hep_b_2))
                    ||
                    vacName.equals(getResources().getString(R.string.rotavirus_1))
                    ||
                    vacName.equals(getResources().getString(R.string.dtap_1))
                    ||
                    vacName.equals(getResources().getString(R.string.hib_1))
                    ||
                    vacName.equals(getResources().getString(R.string.pneumococcal_1))
                    ||
                    vacName.equals(getResources().getString(R.string.polio_1))) {

                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //4 mMonths
        } else if (mMonths >= 4 && mMonths < 5) {
            Log.d("4mMonths", mMonths + ", " + vacName);
            if (vacName.equals(getResources().getString(R.string.hep_b_3))
                    ||
                    vacName.equals(getResources().getString(R.string.rotavirus_2))
                    ||
                    vacName.equals(getResources().getString(R.string.dtap_2))
                    ||
                    vacName.equals(getResources().getString(R.string.hib_2))
                    ||
                    vacName.equals(getResources().getString(R.string.Pneumococcal_2))
                    ||
                    vacName.equals(getResources().getString(R.string.Polio_2))) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //6 mMonths
        } else if (mMonths >= 6 && mMonths < 9) {

            Log.d("6", mMonths + ", " + vacName);
            if (vacName.equals(getResources().getString(R.string.hep_b_4))
                    ||
                    vacName.equals(getResources().getString(R.string.polio_3))
                    ||
                    vacName.equals(getResources().getString(R.string.dtap_3))
                    ||
                    vacName.equals(getResources().getString(R.string.hib_3))
                    ||
                    vacName.equals(getResources().getString(R.string.Pneumococcal_3))) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //9 mMonths
        } else if (mMonths >= 9 && mMonths < 12) {
            Log.d("9", mMonths + ", " + vacName);
            if (vacName.equals(getResources().getString(R.string.mmr_1)) || vacName.equals(getResources().getString(R.string.MCV4_1))) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }


            //12 mMonths
        } else if (mMonths >= 12 && mMonths < 18) {
            Log.d("12", mMonths + ", " + vacName);
            if (vacName.equals(getResources().getString(R.string.mmr_2))
                    ||
                    vacName.equals(getResources().getString(R.string.MCV4_2))
                    ||
                    vacName.equals(getResources().getString(R.string.Pneumococcal_4))
                    ||
                    vacName.equals(getResources().getString(R.string.polio_4))) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //18 mMonths
        } else if (mMonths >= 18 && mMonths < 24) {
            Log.d("18", mMonths + ", " + vacName);
            if (vacName.equals(getResources().getString(R.string.polio_5))
                    ||
                    vacName.equals(getResources().getString(R.string.dtap_4))
                    ||
                    vacName.equals(getResources().getString(R.string.hib_4))
                    ||
                    vacName.equals(getResources().getString(R.string.mmr_3))
                    ||
                    vacName.equals(getResources().getString(R.string.hep_a_1))
                    ||
                    vacName.equals(getResources().getString(R.string.Chickenpox))) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }

            //24 mMonths
        } else if (mMonths >= 24 && mMonths < 25) {
            Log.d("18", mMonths + ", " + vacName);
            if (vacName.equals(getResources().getString(R.string.hep_a_2))) {
                convertView.setBackgroundResource(R.color.tranPurple);
            }
        }
    }

    public static String[] childBirthCalc(String childBirth) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        // String currentDateandTime = sdf.format(new Date());
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());


        //String childBirth = child_info.get(1);//record_child_birth.getText().toString();
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
        //Log.d("dateDiff", dateDiff + ", " + currentDateandTime + ", " + record_child_birth.getText().toString());

        return new String[]{dateDiff / 30.0 + "", dateDiff + ""};
    }
}


