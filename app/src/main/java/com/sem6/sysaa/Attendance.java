package com.sem6.sysaa;


import android.content.Context;
import android.os.Bundle;
        import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Iterator;

public class Attendance extends Fragment {
    Firebase attf,inside;
    String atttext;
    long x;
    TextView att;
    String ai,daa,dbms,iwcs;
    Iterator<DataSnapshot> omg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();

        View view =  lf.inflate(R.layout.attendance_layout,null);

        //FOR TEACHER LOGIN
        //TEMP HERE
        /*
        attf=new Firebase("https://sysaa-be58b.firebaseio.com/Att");

        att = (TextView) view.findViewById(R.id.att);
        attf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                x=dataSnapshot.getChildrenCount();
                omg=dataSnapshot.getChildren().iterator();
                att.setText("count "+x);
                while(omg.hasNext())
                {
                    xs=omg.next().toString();
                    id=xs.substring(xs.indexOf("=")+2,xs.indexOf(","));
                    inside=new Firebase("https://sysaa-be58b.firebaseio.com/StuUsers/"+id+"/Name");
                    inside.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            att.setText(att.getText()+"\n"+dataSnapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        */
        att = (TextView) view.findViewById(R.id.att);
        String macAddress= android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        attf=new Firebase("https://sysaa-be58b.firebaseio.com/Att/"+macAddress+"/Total");
        attf.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                att.setText("AI:\t"+dataSnapshot.child("AI").getValue(String.class));
                att.setText(att.getText()+"\nIWCS:\t"+dataSnapshot.child("IWCS").getValue(String.class));
                att.setText(att.getText()+"\nDAA:\t"+dataSnapshot.child("DAA").getValue(String.class));
                att.setText(att.getText()+"\nDBMS:\t"+dataSnapshot.child("DBMS").getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        return view;

    }


}

