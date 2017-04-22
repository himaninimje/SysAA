package com.sem6.sysaa;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tab extends AppCompatActivity{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    MenuItem name;
    Menu menu,m;
    LinearLayout li;
    Firebase fbname;
    String names;
    long back_pressed;
    private int fbflag;
    private String sub;
    private Firebase Sub_attend;
    private Firebase timetbl;
    private Firebase student;
    private Firebase attend;
    private Firebase batchfb;

    private String hour;
    private String min;
    public  String sub_time;
    private String today;
    private String user_id;
    private String today_date;
    private TextView tv;
    String macAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        li = (LinearLayout) findViewById(R.id.linear);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0xFFFF8800);
        }

        macAddress= android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        fbflag=0;
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        SimpleDateFormat sddf = new SimpleDateFormat("EEEE");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        today= sddf.format(c.getTime());
        today=today.toUpperCase();
        today_date=df.format(c.getTime());

        String currtime = sdf.format(c.getTime());
        //currtime="1539";
        hour=currtime.substring(0,2);
        min=currtime.substring(2,4);
        //user_id="2d48969340b6550a";
        //today="MONDAY";
        if(Integer.parseInt(min)>30)
        {

            timetbl=new Firebase("https://sysaa-be58b.firebaseio.com/TIMETABLE/"+Nav.batchn+"/"+today+"/"+hour+"30");

            timetbl.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null) {
                        sub_time=hour+"30";
                        String sub2= dataSnapshot.getKey();

                        sub = dataSnapshot.getValue(String.class);
                        Log.d("SUBJECT ", sub+" "+sub_time);
                        fbflag=1;
                        markAttendance();
                    }
                    else {

                        timetbl=new Firebase("https://sysaa-be58b.firebaseio.com/TIMETABLE/"+Nav.batchn+"/"+today+"/"+hour+"00");

                        timetbl.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()!=null) {
                                    sub_time=hour+"00";
                                    sub = dataSnapshot.getValue(String.class);
                                    Log.d("SUBJECT ", sub);
                                    fbflag=1;
                                    markAttendance();
                                }
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


        }
        else
        {
            timetbl=new Firebase("https://sysaa-be58b.firebaseio.com/TIMETABLE/"+Nav.batchn+"/"+today+"/"+hour+"00");

            timetbl.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null) {
                        sub_time=hour+"00";
                        sub = dataSnapshot.getValue(String.class);
                        Log.d("SUBJECT ", sub+"  "+sub_time);
                        fbflag=1;
                        markAttendance();
                    }
                    else
                    {
                        hour =Integer.toString(Integer.parseInt(hour)-1);
                        timetbl=new Firebase("https://sysaa-be58b.firebaseio.com/TIMETABLE/"+Nav.batchn+"/"+today+"/"+hour+"30");

                        timetbl.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()!=null) {
                                    sub_time=hour+"30";
                                    sub = dataSnapshot.getValue(String.class);
                                    Log.d("SUBJECT ", sub);
                                    fbflag=1;
                                    markAttendance();
                                }
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

        }
        /**
         *Setup the DrawerLayout and NavigationView
         */
        fbname=new Firebase("https://sysaa-be58b.firebaseio.com/StuUsers/"+macAddress+"/Name");
        fbname.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                names=dataSnapshot.getValue(String.class);

                NavigationView navigationView = (NavigationView) findViewById(R.id.dashboardnav) ;
                m=navigationView.getMenu();
                MenuItem mi=m.findItem(R.id.nav_item_profile);
                mi.setTitle("Hello, "+names);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.dashboardnav) ;

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.nav_item_settings) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new Settings()).commit();


                }

                if (menuItem.getItemId() == R.id.nav_item_logout) {
                    /*FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();*/
                    FirebaseAuth.getInstance().signOut();
                    Intent intent= new Intent(Tab.this, StuLogin.class);
                    startActivity(intent);
                    finish();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }
    public void markAttendance()
    {
        Toast.makeText(this, "in mark attendanceee", Toast.LENGTH_SHORT).show();
        student=new Firebase("https://sysaa-be58b.firebaseio.com/Att/"+macAddress+"/"+today_date+"/"+sub_time);
        student.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null)
                {
                    attend=new Firebase("https://sysaa-be58b.firebaseio.com/Att/"+macAddress);
                    attend.child(today_date).child(sub_time).setValue(sub);
                    Sub_attend =new Firebase("https://sysaa-be58b.firebaseio.com/Att/"+macAddress+"/Total/"+sub);
                    Sub_attend.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value=dataSnapshot.getValue(String.class);
                            int value_num=Integer.parseInt(value)+1;
                            Sub_attend.setValue(Integer.toString(value_num));

                            Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content), "Attendance marked for "+sub, Snackbar.LENGTH_LONG);
                            View snackbarView = snackbar.getView();
                            snackbarView.setBackgroundColor(0xfff4b841);
                            snackbar.show();
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

    }
}


