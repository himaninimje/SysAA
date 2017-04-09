package com.sem6.sysaa;


import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
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
    String xs,id,name;
    Iterator<DataSnapshot> omg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //FOR TEACHER LOGIN
        //TEMP HERE
        LayoutInflater lf = getActivity().getLayoutInflater();

        View view =  lf.inflate(R.layout.attendance_layout,null);
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
        return view;

    }


}

