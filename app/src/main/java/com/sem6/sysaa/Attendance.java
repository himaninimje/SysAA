package com.sem6.sysaa;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
        import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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
    public DatePickerDialog.OnDateSetListener myOnDateSetListener;
    TextView att,testdate;
    private Firebase tot;

    private String daa;
    private String dbms;
    private String ai;
    private String iwcs;
    int pdaa=0;
    int pdbms=0;
    int pai=0;
    int piwcs=0;
    private String macAddress;

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

                        dateatt.setText("AI:\t"+dataSnapshot.child("AI").getValue(String.class));
                        dateatt.setText(dateatt.getText()+"\nIWCS:\t"+dataSnapshot.child("IWCS").getValue(String.class));
                        dateatt.setText(dateatt.getText()+"\nDAA:\t"+dataSnapshot.child("DAA").getValue(String.class));
                        dateatt.setText(dateatt.getText()+"\nDBMS:\t"+dataSnapshot.child("DBMS").getValue(String.class));
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
         macAddress= android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
// here
        tot=new Firebase("https://sysaa-be58b.firebaseio.com/Total classes");
        tot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daa=dataSnapshot.child("DAA").getValue().toString();

                dbms=dataSnapshot.child("DBMS").getValue().toString();
                ai=dataSnapshot.child("AI").getValue().toString();
                iwcs=dataSnapshot.child("IWCS").getValue().toString();


                attf=new Firebase("https://sysaa-be58b.firebaseio.com/Att/"+macAddress+"/Total");
                attf.addValueEventListener(new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                        pdaa=(Integer.parseInt(daa)>0)?Integer.parseInt(dataSnapshot.child("DAA").getValue(String.class))*100/Integer.parseInt(daa):0;
                pdbms=(Integer.parseInt(dbms)>0)?Integer.parseInt(dataSnapshot.child("DBMS").getValue(String.class))*100/Integer.parseInt(dbms):0;
                pai=(Integer.parseInt(ai)>0)?Integer.parseInt(dataSnapshot.child("AI").getValue(String.class))*100/Integer.parseInt(ai):0;
                piwcs=(Integer.parseInt(iwcs)>0)?Integer.parseInt(dataSnapshot.child("IWCS").getValue(String.class))*100/Integer.parseInt(iwcs):0;



                        att.setText("TOTAL\nAI:\t"+dataSnapshot.child("AI").getValue(String.class)+"/"+ai+"  "+pai +"%");
                        att.setText(att.getText()+"\nIWCS:\t"+dataSnapshot.child("IWCS").getValue(String.class)+"/"+iwcs+"  "+piwcs +"%");
                        att.setText(att.getText()+"\nDAA:\t"+dataSnapshot.child("DAA").getValue(String.class)+"/"+daa+"  "+pdaa +"%");
                        att.setText(att.getText()+"\nDBMS:\t"+dataSnapshot.child("DBMS").getValue(String.class)+"/"+dbms+"  "+pdbms +"%");
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




