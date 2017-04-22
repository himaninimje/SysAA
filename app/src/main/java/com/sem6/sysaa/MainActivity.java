package com.sem6.sysaa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    int k=1;
    private FirebaseAuth.AuthStateListener mAuthListener,ma;
    String name,n;
    LinearLayout li;
    long back_pressed;
    String macAddress,uid;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        info=(TextView) findViewById(R.id.sysaa);
        macAddress= android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        mAuthListener=new FirebaseAuth.AuthStateListener(){
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {uid=firebaseAuth.getCurrentUser().getUid().trim();
                Firebase te=new Firebase("https://sysaa-be58b.firebaseio.com/TEACHER/"+uid+"/Name");
                te.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        n=dataSnapshot.getValue(String.class);k++;
                        //info.setText(info.getText()+"\nteacher check: "+n);
                        if(n!=null)
                        {
                            //info.setText(info.getText()+"\nuser exists:(teacher");
                            Intent intentx= new Intent(MainActivity.this,TeacherDash.class);
                            startActivity(intentx);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                Firebase stu=new Firebase("https://sysaa-be58b.firebaseio.com/StuUsers/"+macAddress+"/UID");
                stu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name=dataSnapshot.getValue(String.class);k++;
                        //info.setText(info.getText()+"\nstudent check: "+name);
                        //info.setText(info.getText()+"\nstudent wuid: "+uid);
                        if(name.trim()!=null&&uid.trim()!=null&&name.trim().equals(uid.trim())) {
                            //info.setText(info.getText()+"\nuser exists:( studentttttt");
                            Intent intentx = new Intent(MainActivity.this, Nav.class);
                            startActivity(intentx);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
            else
                {
                    startActivity(new Intent(MainActivity.this,StuLogin.class));
                }
            }

        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            if(name==null&&n==null)
                startActivity(new Intent(MainActivity.this,StuLogin.class));
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void kook()
    {
        if(k==1||k>=3)
            startActivity(new Intent(MainActivity.this,StuLogin.class));
    }
    @Override
    public void onBackPressed() {
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





