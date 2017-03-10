package com.sem6.sysaa;

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
    private Firebase fb;
    private FirebaseAuth fAuth;
    private DatabaseReference mdb;
    private int backButtonCount = 0;
    long back_pressed;
    Button signout;
    CoordinatorLayout li;
    TextView db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        fAuth=FirebaseAuth.getInstance();
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        final String macAddress = wInfo.getMacAddress();
        fb=new Firebase("https://sysaa-be58b.firebaseio.com/example/"+macAddress+"/UID");

        setContentView(R.layout.activity_dashboard_page);
        db=(TextView) findViewById(R.id.textView2);
        //fAuth.getCurrentUser().toString();
        //mdb= FirebaseDatabase.getInstance().getReference().child("users").child(fAuth.getCurrentUser().getUid());
        //db.setText(mdb.getKey()+"\n"+fAuth.getCurrentUser().getDisplayName());

        fb.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                String name=dataSnapshot.getValue(String.class);
                db.setText(name);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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
