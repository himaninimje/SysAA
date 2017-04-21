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

public class Profile extends Fragment {
    Firebase prof;
    TextView profText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.profile_layout,null);
        profText=(TextView) view.findViewById(R.id.profile);
        String macAddress= android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        prof=new Firebase("https://sysaa-be58b.firebaseio.com/StuUsers/"+macAddress);
        prof.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profText.setText("Name:\t"+dataSnapshot.child("Name").getValue(String.class));
                profText.setText(profText.getText()+"\nRoll no.:\t"+dataSnapshot.child("Roll").getValue(String.class));
                profText.setText(profText.getText()+"\nBatch:\t"+dataSnapshot.child("Batch").getValue(String.class));
                profText.setText(profText.getText()+"\nSemester:\t"+dataSnapshot.child("Semester").getValue(String.class));
                profText.setText(profText.getText()+"\nShift:\t"+dataSnapshot.child("Shift").getValue(String.class));
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return view;
    }
}
