package com.sem6.sysaa;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class StuLogin extends AppCompatActivity {
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;
    private TextView mSignupBtn,teacherLogin,info;
    String checkuid1;
    AlertDialog alert11;
    //Declaring Firebase Database object
    private FirebaseAuth mAuth;

    //Declaring Firebase AuthStateListener object for auto-signing in
    //after closing the app
    private FirebaseAuth.AuthStateListener mAuthListener,ma;

    //Declaring Firebase Realtime-Database object to interact with
    //Real-time Database
    private DatabaseReference mDatabase;
    String name,n;
    private ProgressDialog progressDialog;

    private int backButtonCount = 0;
    private ProgressDialog mProgress;

    LinearLayout li;
    long back_pressed;
    String macAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_login);
        teacherLogin=(TextView) findViewById(R.id.teacherLogin);
        li = (LinearLayout) findViewById(R.id.lay123);
        info=(TextView) findViewById(R.id.info);
        //Initialize the Firebase Authentication Database Object
        mAuth = FirebaseAuth.getInstance();
        macAddress= android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);

        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mSignupBtn = (TextView) findViewById(R.id.signupBtn);
        mProgress = new ProgressDialog(this);

        Firebase fb1=new Firebase("https://sysaa-be58b.firebaseio.com/StuUsers/"+macAddress+"/UID");
        fb1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try
                {
                    checkuid1 = dataSnapshot.getValue().toString();
                }
                catch (NullPointerException ne){}
            }

            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        teacherLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StuLogin.this,TeacherLogin.class));
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StuLogin.this, SignUpTest.class));
            }
        });

        progressDialog = new ProgressDialog(StuLogin.this);

    }
/*
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
*/
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
                            Toast.makeText(StuLogin.this, "UNAUTHORIZED SIGN IN", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            //System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&777777");
                            Intent intent=new Intent(StuLogin.this,StuLogin.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(!checkuid1.equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim()))
                        {
                            progressDialog.dismiss();
                            Toast.makeText(StuLogin.this, "UNAUTHORIZED SIGN IN", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            //System.out.println("...................................................................................");
                            Intent intent=new Intent(StuLogin.this,StuLogin.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Intent intent = new Intent(StuLogin.this, Nav.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }


                    }
                }
            });
        }

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
