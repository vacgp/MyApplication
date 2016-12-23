package com.example.mayoo.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import android.widget.ListView;
import android.widget.TextView;
import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter;
import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter.Section;

import java.util.ArrayList;

/**
 * Created by mayoo on 12/10/2016.
 */

public class VaccinesList extends AppCompatActivity {

    String[] vacNames;

    listAdapter listAdapter_object;
    ListView list;
    private ArrayList<Section> sections = new ArrayList<Section>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaccines);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 145, 192)));
        }

        Resources resources = getResources();
        vacNames = resources.getStringArray(R.array.vaccines);
        String[] headerNames = resources.getStringArray(R.array.vaccines_headers);
        Integer[] headerPositions = {0, 3, 5, 7, 8, 9, 11, 13, 15};

        list = (ListView)findViewById(R.id.list);
        listAdapter_object = new listAdapter(getApplicationContext(), vacNames);
        for (int i = 0; i < headerPositions.length; i++) {
            sections.add(new Section(headerPositions[i], headerNames[i]));
        }
        SimpleSectionedListAdapter simpleSectionedGridAdapter = new SimpleSectionedListAdapter(this, listAdapter_object,
                R.layout.list_item_header, R.id.header);
        simpleSectionedGridAdapter.setSections(sections.toArray(new Section[0]));
        list.setAdapter(simpleSectionedGridAdapter);



       /* Resources resources = getResources();
        vacNames = resources.getStringArray(R.array.vaccines);
        listAdapter_object = new listAdapter(getApplicationContext(), vacNames);

        ListView lv = (ListView) findViewById(R.id.vac_list);
        lv.setAdapter(listAdapter_object);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long row_id) {

Log.d("ListView onItemClick", vacNames[position]);
            }
        });*/
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


