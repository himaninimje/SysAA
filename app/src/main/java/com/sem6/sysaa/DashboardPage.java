package com.sem6.sysaa;

import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardPage extends AppCompatActivity {
    private Firebase fbname,fbroll,fbbatch,fbsem,fbshift;
    private FirebaseAuth fAuth;
    private DatabaseReference mdb;
    private int backButtonCount = 0;
    long back_pressed;
    Button signout;
    Button Go;
    Button scan;
    CoordinatorLayout li;
    TextView db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        fAuth=FirebaseAuth.getInstance();
        /*WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        final String macAddress = wInfo.getMacAddress();*/

        //FOR PROFILE PAGE
        String macAddress= android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        fbname=new Firebase("https://sysaa-be58b.firebaseio.com/example/"+macAddress+"/Name");
        fbbatch=new Firebase("https://sysaa-be58b.firebaseio.com/example/"+macAddress+"/Batch");
        fbroll=new Firebase("https://sysaa-be58b.firebaseio.com/example/"+macAddress+"/Roll");
        fbsem=new Firebase("https://sysaa-be58b.firebaseio.com/example/"+macAddress+"/Semester");
        fbshift=new Firebase("https://sysaa-be58b.firebaseio.com/example/"+macAddress+"/Shift");
        setContentView(R.layout.activity_dashboard_page);
        db=(TextView) findViewById(R.id.textView2);
        //fAuth.getCurrentUser().toString();
        //mdb= FirebaseDatabase.getInstance().getReference().child("users").child(fAuth.getCurrentUser().getUid());
        //db.setText(mdb.getKey()+"\n"+fAuth.getCurrentUser().getDisplayName());

        fbname.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                db.setText(db.getText()+"\nName: "+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        fbroll.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                db.setText(db.getText()+"\nRoll no.: "+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        fbsem.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                db.setText(db.getText()+"\nSemester: "+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        fbshift.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                db.setText(db.getText()+"\nShift: "+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        fbbatch.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                db.setText(db.getText()+"\nBatch: "+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        //SIGNOUT
        signout=(Button) findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(DashboardPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Go=(Button) findViewById(R.id.go);
        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DashboardPage.this, Tab.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void onBackPressed() {
        li=(CoordinatorLayout) findViewById(R.id.dashboardlay);
        if (back_pressed + 3000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            back_pressed = System.currentTimeMillis();
            Snackbar snackbar = Snackbar
                    .make(li, R.string.backpress, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
