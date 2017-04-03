package com.sem6.sysaa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//SIGN IN HERE
public class MainActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    TextView info;
    private Button mLoginBtn;
    private TextView mSignupBtn;
    String checkuid1;
    AlertDialog alert11;
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
    String macAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        li = (LinearLayout) findViewById(R.id.lay123);

        //Initialize the Firebase Authentication Database Object
        mAuth = FirebaseAuth.getInstance();
        macAddress= android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);

        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mSignupBtn = (TextView) findViewById(R.id.signupBtn);
        mProgress = new ProgressDialog(this);
        info=(TextView) findViewById(R.id.info);
        System.out.println(macAddress);

        System.out.println(checkuid1);

        Firebase fb1=new Firebase("https://sysaa-be58b.firebaseio.com/example/"+macAddress+"/UID");
        fb1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    checkuid1 = dataSnapshot.getValue().toString();
                    System.out.println("\n checkamc here " + checkuid1);
                }
                catch (NullPointerException ne){}
            }

            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
                    System.out.println("okay............................................");
                    Intent intentx= new Intent(MainActivity.this,Tab.class);
                    startActivity(intentx);
                    finish();
                    /*  This Real-time Database object checks in the Real-time database
                        for a node (already created) named "Users". For first time, user
                        needs to sign up to programmatically create the node in real-time
                        database. Please note the nodes should exist in order to run the code.
                     */

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
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar
                                .make(li, R.string.auth_success, Snackbar.LENGTH_LONG);
                        snackbar.show();

                        if(checkuid1==null)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "UNAUTHORIZED SIGN IN", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent=new Intent(MainActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(!checkuid1.equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim()))
                        {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "UNAUTHORIZED SIGN IN", Toast.LENGTH_LONG).show();
                            /*final AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setTitle("Sign In Failed");
                            builder1.setMessage("UnAuthorized Sign-in Attempt");
                            builder1.setCancelable(true);


                            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    alert11.cancel();


                                }
                            });
                            alert11=builder1.create();
                            alert11.show();*/
                            FirebaseAuth.getInstance().signOut();
                            Intent intent=new Intent(MainActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            //Date tttt= Calendar.getInstance().getTime();
                            /*DateFormat df=new SimpleDateFormat("HHmm");
                            String tttt=df.format(Calendar.getInstance().getTime());
                            Toast.makeText(MainActivity.this, tttt, Toast.LENGTH_LONG).show();*/
                            Intent intent = new Intent(MainActivity.this, Tab.class);
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





