package com.sem6.sysaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class Schedule extends Fragment {
    TextView sch;
    String batch;
    Firebase batchh,wbatch;
    View view;
    ListView listView,lv2;
    Iterator<DataSnapshot> dsi;
    Bundle extras;String check;Button back;
    private ArrayList<String> mTimeSlots = new ArrayList<>();
    private ArrayList<String> mSubName = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        view =  lf.inflate(R.layout.schedule_layout,null);
        sch = (TextView) view.findViewById(R.id.timetable);
        extras = getActivity().getIntent().getExtras();
        check=null;
        if(extras!=null)
            check=extras.getString("day");
        if(check!=null) {
            sch.setText(check +" Timetable");
            back=(Button) view.findViewById(R.id.backpress);
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = getActivity().getIntent();
                    String h=null;
                    myIntent.putExtra("day",h);
                    startActivity(myIntent);
                }
            });
            listView = (ListView)view.findViewById(R.id.listview1);
            listView.setVisibility(View.VISIBLE);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(container.getContext(),
                    android.R.layout.simple_list_item_1,mTimeSlots);
            listView.setAdapter(arrayAdapter);
            lv2 = (ListView)view.findViewById(R.id.listview2);
            lv2.setVisibility(View.VISIBLE);
            final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(container.getContext(),
                    android.R.layout.simple_list_item_1,mSubName);
            lv2.setAdapter(arrayAdapter2);
            String macAddress = android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            batchh = new Firebase("https://sysaa-be58b.firebaseio.com/StuUsers/" + macAddress + "/Batch/");
            batchh.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    batch = dataSnapshot.getValue(String.class);
                    wbatch = new Firebase("https://sysaa-be58b.firebaseio.com/TIMETABLE/" + batch.toUpperCase()+"/"+check.toUpperCase());
                    wbatch.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                                    dsi=dataSnapshot.getChildren().iterator();
                                    while(dsi.hasNext())
                                    {
                                        String ds=dsi.next().toString();
                                        int k=ds.indexOf("key = ");
                                        String x=ds.substring(k+6,k+10);
                                        int v=ds.indexOf("value = ");
                                        int curl=ds.indexOf('}');
                                        String y=ds.substring(v+8,curl);
                                        mTimeSlots.add(x);
                                        mSubName.add(y);
                                    }



                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        else
        {

            sch.setText("SCHEDULE SELECT");
            listView = (ListView) view.findViewById(R.id.list);
            listView.setVisibility(View.VISIBLE);
            String[] values = new String[]{"Monday",
                    "Tuesday",
                    "Wednesday",
                    "Thursday",
                    "Friday",
                    "Saturday",
                    "Today's TimeTable"

            };
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    // ListView Clicked item index
                    int itemPosition = position;

                    // ListView Clicked item value
                    String itemValue = (String) listView.getItemAtPosition(position);

                    Intent myIntent = getActivity().getIntent();
                    if (!itemValue.equals("Today's TimeTable")) {
                        myIntent.putExtra("day", itemValue);

                    } else {
                        Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_WEEK);

                        switch (day) {
                            case Calendar.SATURDAY:
                                myIntent.putExtra("day", "Saturday");
                                break;
                            case Calendar.MONDAY:
                                myIntent.putExtra("day", "Monday");
                                break;
                            case Calendar.TUESDAY:
                                myIntent.putExtra("day", "Tuesday");break;
                            case Calendar.WEDNESDAY:
                                myIntent.putExtra("day", "Wednesday");break;
                            case Calendar.FRIDAY:
                                myIntent.putExtra("day", "Friday");break;
                            case Calendar.THURSDAY:
                                myIntent.putExtra("day", "Thursday");break;
                            case Calendar.SUNDAY:
                                Toast.makeText(container.getContext(),
                                        "Today is Sunday, no classes" + itemValue, Toast.LENGTH_LONG)
                                        .show();
                        }
                    }
                    startActivity(myIntent);
                }

            });

        }
        return view;


    }
}
