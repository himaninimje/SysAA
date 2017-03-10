package com.sem6.sysaa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.core.Context;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//SIGN IN HERE
public class MainActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mLoginBtn;
    private TextView mSignupBtn;
    private String checkuid;
    //Declaring Firebase Database object
    private FirebaseAuth mAuth;

    //Declaring Firebase AuthStateListener object for auto-signing in
    //after closing the app
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Declaring Firebase Realtime-Database object to interact with
    //Real-time Database
    private DatabaseReference mDatabase;

    private ProgressDialog progressDialog;

    private int backButtonCount = 0;
    private ProgressDialog mProgress;

    LinearLayout li;
    long back_pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        li = (LinearLayout) findViewById(R.id.lay123);

        //Initialize the Firebase Authentication Database Object
        mAuth = FirebaseAuth.getInstance();

        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);

        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mSignupBtn = (TextView) findViewById(R.id.signupBtn);
        mProgress = new ProgressDialog(this);

        /*  On this activity start (This is the launcher activity)
            the app first checks the AuthState of the app, with
            AuthStateListener object. firebaseAuth is a FirebaseAuth
            variable which has methods such has getCurrentUser() to
            automatically get the current logged in user.

            NOTE: AuthState listener is set to check if user has already
            logged into the app. IF it is his/her first time, it is not
            set and he/she needs to sign in to the app.
         */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null)
                {
                    /*  User has logged-in already
                        Move user directly to the Account Activity
                        or the activity you need to display after
                        logging the user in
                    */

                    mProgress.setMessage("Give Us A Moment...");
                    mProgress.show();
                    mProgress.dismiss();
                    Intent intentx= new Intent(MainActivity.this,DashboardPage.class);
                    startActivity(intentx);
                    finish();
                    /*  This Real-time Database object checks in the Real-time database
                        for a node (already created) named "Users". For first time, user
                        needs to sign up to programmatically create the node in real-time
                        database. Please note the nodes should exist in order to run the code.
                     */
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            /* In this method of onDataChange, the operation are performed on the
                                data of the real-time database by taking or retrieving values from
                                it using a method called "datasnapshot".

                                Ex: final String string = dataSnapshot.child("level").getValue().toString().trim();

                                This statement declares a final string which stores the value of an ATTRIBUTE named
                                "level" under the "Users" parent in the database and gets its value and converts it
                                to string. The trim() method avoids spacing issues such has " 2"  is taken as "2"
                             */

                            /*
                                whatever operations you need to perform, by retrieving values from database,
                                first give it a reference like under which child and what attribute, and then
                                create variables to store the datasnapshot values

                             */

                            /*
                            final String string = dataSnapshot.child("level").getValue().toString().trim();
                            if (string.equals("4")) {
                                mProgress.dismiss();
                                Intent intent = new Intent(MainActivity.this, AccountAdminPanel.class);
                                startActivity(intent);
                                finish();
                            } else if (string.equals("99")) {
                                mProgress.dismiss();
                                Intent intent = new Intent(MainActivity.this, AwatingForApproval.class);
                                startActivity(intent);
                                finish();
                            } else if (string.equals("2")) {
                                mProgress.dismiss();
                                Intent intent = new Intent(MainActivity.this, AccountActivityAdmin.class);
                                startActivity(intent);
                                finish();
                            } else {
                                mProgress.dismiss();
                                Intent intent = new Intent(MainActivity.this, AccountActivityUser.class);
                                startActivity(intent);
                                finish();
                            }
                            */
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSignIn();

            }
        });

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SignUpTest.class));
            }
        });

        progressDialog = new ProgressDialog(MainActivity.this);

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
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void startSignIn()
    {

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            //User did not enter any email or password.
            Snackbar snackbar = Snackbar
                    .make(li, R.string.empty_field, Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar snackbar = Snackbar
                    .make(li, R.string.incorrect_email, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else {

            progressDialog.setMessage("Logging in...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        //if incorrect email/password entered.
                        progressDialog.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(li, R.string.auth_failed, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(li, R.string.auth_success, Snackbar.LENGTH_LONG);
                        snackbar.show();

                        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

                        String macAddress= android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                        
                        Firebase fb1=new Firebase("https://sysaa-be58b.firebaseio.com/example/"+macAddress+"/UID");
                        fb1.addValueEventListener(new com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                checkuid= dataSnapshot.getValue(String.class);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        System.out.println(checkuid);
                        if(!checkuid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim()))
                        {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setMessage("UnAuthorized Sign-in Attempt");
                            builder1.setCancelable(true);
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent=new Intent(MainActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Intent intent = new Intent(MainActivity.this, DashboardPage.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }


                    }
                }
            });
        }

    }

    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */

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





