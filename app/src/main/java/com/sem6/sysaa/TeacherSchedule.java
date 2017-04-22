package com.sem6.sysaa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class TeacherSchedule extends AppCompatActivity {
    TextView sch;
    String batch;
    Firebase batchh,wbatch;
    View view;
    ListView listView,lv2;
    Iterator<DataSnapshot> dsi;
    FirebaseAuth mauth;
    Bundle extras;String check;Button back;
    private ArrayList<String> mTimeSlots = new ArrayList<>();
    private ArrayList<String> mSubName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_schedule);
        sch = (TextView) findViewById(R.id.ttimetable);
        extras = getIntent().getExtras();
        check=null;
        if(extras!=null)
            check=extras.getString("day");
        if(check!=null) {
            sch.setText(check +" TIMETABLE");
            back=(Button) findViewById(R.id.backpresst);
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = getIntent();
                    String h=null;
                    myIntent.putExtra("day",h);
                    startActivity(myIntent);
                }
            });
            listView = (ListView)findViewById(R.id.listview1t);
            listView.setVisibility(View.VISIBLE);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TeacherSchedule.this,
                    android.R.layout.simple_list_item_1,mTimeSlots);
            listView.setAdapter(arrayAdapter);
            lv2 = (ListView)findViewById(R.id.listview2t);
            lv2.setVisibility(View.VISIBLE);
            final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(TeacherSchedule.this,
                    android.R.layout.simple_list_item_1,mSubName);
            lv2.setAdapter(arrayAdapter2);
            mauth=FirebaseAuth.getInstance();
            String uuid=mauth.getCurrentUser().getUid().trim();
            String macAddress = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            batchh = new Firebase("https://sysaa-be58b.firebaseio.com/TEACHER/" + uuid + "/Timetable/"+check);
            batchh.addValueEventListener(new ValueEventListener() {
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
        else
        {

            sch.setText("SCHEDULE SELECT");
            listView = (ListView) findViewById(R.id.tlistt);
            listView.setVisibility(View.VISIBLE);
            String[] values = new String[]{"Monday",
                    "Tuesday",
                    "Wednesday",
                    "Thursday",
                    "Friday",
                    "Saturday",
                    "Today's TimeTable"

            };
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeacherSchedule.this,
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

                    Intent myIntent = getIntent();
                    if (!itemValue.equals("Today's TimeTable")) {
                        myIntent.putExtra("day", itemValue.toUpperCase());

                    } else {
                        Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_WEEK);

                        switch (day) {
                            case Calendar.SATURDAY:
                                myIntent.putExtra("day", "Saturday".toUpperCase());
                                break;
                            case Calendar.MONDAY:
                                myIntent.putExtra("day", "Monday".toUpperCase());
                                break;
                            case Calendar.TUESDAY:
                                myIntent.putExtra("day", "Tuesday".toUpperCase());break;
                            case Calendar.WEDNESDAY:
                                myIntent.putExtra("day", "Wednesday".toUpperCase());break;
                            case Calendar.FRIDAY:
                                myIntent.putExtra("day", "Friday".toUpperCase());break;
                            case Calendar.THURSDAY:
                                myIntent.putExtra("day", "Thursday".toUpperCase());break;
                            case Calendar.SUNDAY:
                                Toast.makeText(TeacherSchedule.this,
                                        "Today is Sunday, no classes" + itemValue, Toast.LENGTH_LONG)
                                        .show();
                        }
                    }
                    startActivity(myIntent);
                }

            });

        }
    }
}
