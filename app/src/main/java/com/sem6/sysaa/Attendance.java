package com.sem6.sysaa;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
        import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;
import java.util.Iterator;

public class Attendance extends Fragment {
    Firebase attf;
    String check;
    Button setdate;
    private Calendar calendar;
    private int year, month, day;
    Bundle extras;
    Iterator<DataSnapshot> subjctatt;
    public DatePickerDialog.OnDateSetListener myOnDateSetListener;
    TextView att,testdate;String macAddress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.attendance_layout,null);
        macAddress= android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        extras = getActivity().getIntent().getExtras();
        if(extras!=null)
        {
            check=extras.getString("date");
            if(check!=null)
            {
                testdate=(TextView) view.findViewById(R.id.testDate);
                testdate.setText(check);
                final TextView dateatt=(TextView) view.findViewById(R.id.dateatt);
                Firebase dateattendance= new Firebase("https://sysaa-be58b.firebaseio.com/Att/"+macAddress+"/"+check);
                dateattendance.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subjctatt=dataSnapshot.getChildren().iterator();
                        while(subjctatt.hasNext())
                        {
                            String sub=subjctatt.next().toString();
                            int k=sub.indexOf("key = ");
                            int v=sub.indexOf(("value = "));
                            int curl=sub.indexOf('}');
                            dateatt.setText(dateatt.getText()+"\n"+sub.substring(k+6,k+10)+": "+sub.substring(v+8,curl));
                        }
                        /*dateatt.setText("AI:\t"+dataSnapshot.child("AI").getValue(String.class));
                        dateatt.setText(dateatt.getText()+"\nIWCS:\t"+dataSnapshot.child("IWCS").getValue(String.class));
                        dateatt.setText(dateatt.getText()+"\nDAA:\t"+dataSnapshot.child("DAA").getValue(String.class));
                        dateatt.setText(dateatt.getText()+"\nDBMS:\t"+dataSnapshot.child("DBMS").getValue(String.class));*/
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        att = (TextView) view.findViewById(R.id.att);
        String macAddress= android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        attf=new Firebase("https://sysaa-be58b.firebaseio.com/Att/"+macAddress+"/Total");
        attf.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                att.setText("TOTAL\nAI:\t"+dataSnapshot.child("AI").getValue(String.class));
                att.setText(att.getText()+"\nIWCS:\t"+dataSnapshot.child("IWCS").getValue(String.class));
                att.setText(att.getText()+"\nDAA:\t"+dataSnapshot.child("DAA").getValue(String.class));
                att.setText(att.getText()+"\nDBMS:\t"+dataSnapshot.child("DBMS").getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        setdate=(Button) view.findViewById(R.id.setdate);
        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().showDialog(999);
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }
        });

        return view;
    }

}


