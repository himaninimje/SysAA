package com.sem6.sysaa;

import android.*;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class Nav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BeaconConsumer, RangeNotifier {
    long back_pressed;
    protected static final String TAG = "MonitoringActivity";
    Button home,nav;
    DrawerLayout drawer;
    private BluetoothAdapter mBluetoothAdapter;
    private BeaconManager mBeaconManager;
    Firebase fbname,checkteacher;
    String names,uid;
    Menu m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        checkteacher= new Firebase("https://sysaa-be58b.firebaseio.com/TEACHER/"+uid+"/Name");
        checkteacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String x = dataSnapshot.getValue().toString();

                    if (x != null)
                        startActivity(new Intent(Nav.this, TeacherDash.class));
                }
                catch(NullPointerException exp){}
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //nav ends
        //profile setup
        String macAddress= android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        fbname=new Firebase("https://sysaa-be58b.firebaseio.com/StuUsers/"+macAddress+"/Name");
        fbname.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                names=dataSnapshot.getValue(String.class);

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view) ;
                m=navigationView.getMenu();
                MenuItem mi=m.findItem(R.id.nav_item_profile);
                mi.setTitle("Hello, "+names);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //beacon code
        if (ContextCompat.checkSelfPermission(Nav.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Nav.this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);


        }
        if (ContextCompat.checkSelfPermission(Nav.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Nav.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    2);


        }

        home=(Button) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Nav.this,Tab.class);
                startActivity(intent);
            }
        });
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
    }


    @Override
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item_logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent= new Intent(Nav.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_item_settings) {
            Intent intent= new Intent(Nav.this, Settings.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_item_tt) {

        } else if (id == R.id.nav_item_profile) {
            Intent intent= new Intent(Nav.this, Profile.class);
            startActivity(intent);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onResume() {

        super.onResume();
        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        // Detect the main Eddystone-UID frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        // Detect the telemetry Eddystone-TLM frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        mBeaconManager.bind(this);

    }
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBeaconManager.addRangeNotifier(this);
        Toast.makeText(this, "inside service connect", Toast.LENGTH_SHORT).show();
    }
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        for (Beacon beacon: beacons) {
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                // This is a Eddystone-UID frame
                Identifier namespaceId = beacon.getId1();
                Identifier instanceId = beacon.getId2();
                Log.d(TAG, "I see a beacon transmitting namespace id: "+namespaceId+
                        " and instance id: "+instanceId+
                        " approximately "+beacon.getDistance()+" meters away.");
                Intent intent=new Intent(this,Tab.class);
                startActivity(intent);

                // Do we have telemetry data?
                if (beacon.getExtraDataFields().size() > 0) {
                    long telemetryVersion = beacon.getExtraDataFields().get(0);
                    long batteryMilliVolts = beacon.getExtraDataFields().get(1);
                    long pduCount = beacon.getExtraDataFields().get(3);
                    long uptime = beacon.getExtraDataFields().get(4);

                    Log.d(TAG, "The above beacon is sending telemetry version "+telemetryVersion+
                            ", has been up for : "+uptime+" seconds"+
                            ", has a battery level of "+batteryMilliVolts+" mV"+
                            ", and has transmitted "+pduCount+" advertisements.");

                }
            }
        }
    }

    public void onPause() {
        super.onPause();

        mBeaconManager.unbind(this);
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
