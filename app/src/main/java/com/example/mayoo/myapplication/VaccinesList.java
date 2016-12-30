package com.example.mayoo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mayoo on 12/10/2016.
 */

public class VaccinesList extends AppCompatActivity {

    String[] vacNames;

    listAdapter listAdapter_object;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaccines);

        /*ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 145, 192)));
        }*/

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

        ArrayList<SimpleSectionedListAdapter.Section> sections = new ArrayList<SimpleSectionedListAdapter.Section>();

        Resources resources = getResources();
        vacNames = resources.getStringArray(R.array.vaccines);
        String[] headerNames = resources.getStringArray(R.array.vaccines_headers);
        Integer[] headerPositions = {0, 2, 8, 14, 19, 21, 25, 31};

        list = (ListView) findViewById(R.id.list);
        for (int i = 0; i < headerPositions.length; i++) {
            sections.add(new SimpleSectionedListAdapter.Section(headerPositions[i], headerNames[i]));
        }

        listAdapter_object = new listAdapter(getApplicationContext(), vacNames);

        SimpleSectionedListAdapter simpleSectionedListAdapter = new SimpleSectionedListAdapter(this, listAdapter_object,
                R.layout.list_item_header, R.id.header);
        simpleSectionedListAdapter.setSections(sections.toArray(new SimpleSectionedListAdapter.Section[0]));
        list.setAdapter(simpleSectionedListAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long row_id) {
                Log.d("ListView onItemClick", vacNames[(int)row_id]);

                Intent intent_to = new Intent(VaccinesList.this, VacInfo.class);

                Locale locale = Locale.getDefault();
                if (locale.toString().startsWith("ar")){
                    switch (vacNames[(int)row_id]) {
                        case "الالتهاب الكبدي ب":
                            intent_to.putExtra("layoutID", R.layout.vac_hepatits_b);
                            break;
                        case "فيروس الروتا":
                            intent_to.putExtra("layoutID", R.layout.vac_rotavirus);
                            break;
                        case "الثلاثي البكتيري":
                            intent_to.putExtra("layoutID", R.layout.vac_dtap);
                            break;
                        case "المستدمية النزلية":
                            intent_to.putExtra("layoutID", R.layout.vac_hib);
                            break;
                        case "المكورات الرئوية":
                            intent_to.putExtra("layoutID", R.layout.vac_pneumococcal);
                            break;
                        case "شلل الأطفال":
                            intent_to.putExtra("layoutID", R.layout.vac_polio);
                            break;
                        case "الثلاثي الفيروسي":
                            intent_to.putExtra("layoutID", R.layout.vac_mmr);
                            break;
                        case "الجدري":
                            intent_to.putExtra("layoutID", R.layout.vac_chickenpox);
                            break;
                        case "الالتهاب الكبدي أ":
                            intent_to.putExtra("layoutID", R.layout.vac_hepatits_a);
                            break;

                        case "الدرن":
                            intent_to.putExtra("layoutID", R.layout.vac_bcg);
                            break;
                        case "الحمى الشوكية":
                            intent_to.putExtra("layoutID", R.layout.vac_mcv4);
                            break;

                        default:
                            break;
                    }
                } else {
                    switch (vacNames[(int)row_id]) {
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
    }

    public class listAdapter extends ArrayAdapter<String> {

        Context context;
        String[] vacNames_adapter;


        listAdapter(Context c, String[] vacNames) {
            super(c, R.layout.child_vac_list, R.id.vac_name_list_item, vacNames);
            this.context = c;
            this.vacNames_adapter = vacNames;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) VaccinesList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.child_vac_list, parent, false);
            }

            TextView vac_name_list_item = (TextView) convertView.findViewById(R.id.vac_name_list_item);

            vac_name_list_item.setText(vacNames_adapter[position]);

            return convertView;
        }


    }

}


