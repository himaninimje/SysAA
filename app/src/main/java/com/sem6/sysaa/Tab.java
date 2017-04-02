package com.sem6.sysaa;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Menu;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

public class Tab extends AppCompatActivity{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    MenuItem name;
    Menu menu,m;
    Firebase fbname;
    String names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0xFFFF8800);
        }
        /**
         *Setup the DrawerLayout and NavigationView
         */
        String macAddress= android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        fbname=new Firebase("https://sysaa-be58b.firebaseio.com/example/"+macAddress+"/Name");
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
                    Intent intent= new Intent(Tab.this, MainActivity.class);
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
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        this.menu=menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.drawermenu, menu);
        //updateMenuItems(menu);
        return true;
    }
    //@Override
    public void updateMenuItems(Menu menu)
    {
        MenuItem menuI = menu.findItem(R.id.nav_item_profile);
        menuI.setTitle(names);
    }*/
}


