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



        ArrayList<SimpleSectionedListAdapter.Section> sections = new ArrayList<SimpleSectionedListAdapter.Section>();

        Resources resources = getResources();
        vacNames = resources.getStringArray(R.array.vaccines);
        String[] headerNames = resources.getStringArray(R.array.vaccines_headers);
        Integer[] headerPositions = {0, 1, 2, 7, 12, 17, 19, 23, 24};

        list = (ListView)findViewById(R.id.list);
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
                Log.d("ListView onItemClick", vacNames[position]);

                Intent intent_to = new Intent(VaccinesList.this, VacInfo.class);

                switch (vacNames[(int)row_id]) {
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


