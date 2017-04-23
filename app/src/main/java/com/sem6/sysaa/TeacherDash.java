package com.sem6.sysaa;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherDash extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button so;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener,ma;
    ListView listView;
    long back_pressed;
    String names,uid;Menu m;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dash);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuthListener=new FirebaseAuth.AuthStateListener(){
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {String uid=firebaseAuth.getCurrentUser().getUid().trim();
                Firebase fbname=new Firebase("https://sysaa-be58b.firebaseio.com/TEACHER/"+uid+"/Name");
                fbname.addValueEventListener(new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                        names=dataSnapshot.getValue(String.class);

                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_teach) ;
                        m=navigationView.getMenu();
                        MenuItem mi=m.findItem(R.id.tnav_item_profile);
                        mi.setTitle("Hello, "+names);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }}

        };


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_teach);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_teach);
        navigationView.setNavigationItemSelectedListener(this);
        listView = (ListView) findViewById(R.id.listteach);

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "Present",
                "Schedule",
                "Subjects"
        };



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch (position)
                {
                    case 0:
                        Intent myIntent1 = new Intent(TeacherDash.this, TeacherPresent.class);
                        startActivity(myIntent1);break;
                    case 1:
                        Intent myIntent2 = new Intent(TeacherDash.this, TeacherSchedule.class);
                        startActivity(myIntent2);break;
                    case 2:
                        Intent myIntent3 = new Intent(TeacherDash.this, TeacherSubject.class);
                        startActivity(myIntent3);break;
                }
            }

        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.tnav_item_logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent= new Intent(TeacherDash.this, TeacherLogin.class);
            startActivity(intent);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_teach);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {
        if (back_pressed + 3000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            back_pressed = System.currentTimeMillis();
            Snackbar snackbar = Snackbar
                    .make(drawer, R.string.backpress, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
