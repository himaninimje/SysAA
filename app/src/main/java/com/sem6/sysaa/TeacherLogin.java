package com.sem6.sysaa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherLogin extends AppCompatActivity {
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;long back_pressed;
    private TextView mSignupBtn,stuLogin,testinfo;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private ProgressDialog progressDialog;
    LinearLayout li;
    String checkuid1,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        testinfo=(TextView) findViewById(R.id.testinfo);

        li = (LinearLayout) findViewById(R.id.lay1234);
        mProgress = new ProgressDialog(this);
        progressDialog= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mLoginBtn = (Button) findViewById(R.id.loginBtnn);
        mSignupBtn = (TextView) findViewById(R.id.signupBtnn);
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherLogin.this,TeacherSignup.class));
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
        mEmailField = (EditText) findViewById(R.id.eemailField);
        mPasswordField = (EditText) findViewById(R.id.ppasswordField);
        stuLogin=(TextView) findViewById(R.id.stuLogin);
        stuLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherLogin.this,StuLogin.class));
            }
        });


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
                        //snackbar.show();
                        //uid=mAuth.getCurrentUser().getUid();
                        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                try {
                                    if(firebaseAuth.getCurrentUser()!=null){
                                    Firebase tteach = new Firebase("https://sysaa-be58b.firebaseio.com/TEACHER/" + firebaseAuth.getCurrentUser().getUid() + "/Name");
                                    tteach.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            testinfo.setText(testinfo.getText() + "\nin state auth listener");
                                            checkuid1 = dataSnapshot.getValue(String.class);
                                            testinfo.setText(testinfo.getText() + "\nuid: " + checkuid1);
                                            if (checkuid1 == null) {
                                                progressDialog.dismiss();
                                                Toast.makeText(TeacherLogin.this, "UNAUTHORIZED SIGN IN", Toast.LENGTH_LONG).show();
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(TeacherLogin.this, TeacherLogin.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(TeacherLogin.this, TeacherDash.class);
                                                startActivity(intent);
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }}
                                catch(NullPointerException exp)
                                {
                                    /*progressDialog.dismiss();
                                    Toast.makeText(TeacherLogin.this, "UNAUTHORIZED SIGN IN", Toast.LENGTH_LONG).show();
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(TeacherLogin.this, TeacherLogin.class);
                                    startActivity(intent);
                                    finish();*/
                                }
                            }

                        });



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
