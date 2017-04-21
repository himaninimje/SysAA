package com.sem6.sysaa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherSignup extends AppCompatActivity {
    private Firebase fb1;
    private EditText mName;
    private EditText emailtext;
    private EditText passwordtext;
    private EditText repasswordtext;
    private Button signUpBtn;
    private RelativeLayout li;
    public String email;
    public String password;
    public String repassword;
    private String checkuid;

    //defining FireBase Auth object
    public FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_signup);
        signUpBtn = (Button) findViewById(R.id.tsignupBtn);

        TextView haveacc = (TextView) findViewById(R.id.thaveAccount);
        haveacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TeacherSignup.this, TeacherLogin.class));
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean signUpOk = false;
                boolean nameAllGood = false;
                boolean emailAllGood = false;
                boolean passwordAllGood = false;

                mName = (EditText) findViewById(R.id.teditName);
                emailtext = (EditText) findViewById(R.id.tsign_up_email_text);
                passwordtext = (EditText) findViewById(R.id.tsign_up_password_text);
                repasswordtext = (EditText) findViewById(R.id.tsign_up_repassword_text);
                li = (RelativeLayout) findViewById(R.id.tsign_up_relative);


                final String Name = mName.getText().toString().trim();
                //Validate for empty Name field
                if (TextUtils.isEmpty(Name)) {
                    Toast.makeText(TeacherSignup.this, "Please Enter Name", Toast.LENGTH_LONG).show();
                } else {
                    nameAllGood = true;
                }
                //Validate Email
                email = emailtext.getText().toString().trim();
                if (!isValidEmail(email)) {
                    emailtext.setError("Enter a valid Email Address");
                    Toast.makeText(TeacherSignup.this, R.string.incorrect_email, Toast.LENGTH_LONG).show();
                } else {
                    emailAllGood = true;
                }

                //Validate Password
                password = passwordtext.getText().toString();
                repassword = repasswordtext.getText().toString();
                if (password.length() < 8) {
                    passwordtext.setError("Must be atleast 8 characters long");
                    Toast.makeText(TeacherSignup.this, "Must be atleast 8 characters long", Toast.LENGTH_LONG).show();
                } else if (password.length() >= 8) {
                    if (!password.equals(repassword)) {
                        passwordtext.setError("Passwords do not match.");
                        Toast.makeText(TeacherSignup.this, "Password do not match", Toast.LENGTH_LONG).show();
                    } else {
                        passwordAllGood = true;
                    }
                }

                //Complete SignUpAction
                if (nameAllGood && emailAllGood && passwordAllGood ) {
                    signUpOk = true;
                }


                if (signUpOk) {

                    firebaseAuth = FirebaseAuth.getInstance();

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(TeacherSignup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //checking if success
                                    if (task.isSuccessful()) {
                                        //display some message here
                                        Firebase fb = new Firebase("https://sysaa-be58b.firebaseio.com/TEACHER/");
                                        //Set an attribute child in it as name (as user entered during sign up
                                        final String user_id = firebaseAuth.getCurrentUser().getUid();  //Set parent as UID
                                        //info.setText(info.getText()+"\n adding to maindb"+(i++));
                                        fb.child(user_id).child("Name").setValue(Name.toUpperCase());
                                        startActivity(new Intent(TeacherSignup.this, TeacherDash.class));
                                        Toast.makeText(TeacherSignup.this, "Registration Success", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(TeacherSignup.this, "Registration Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return (Patterns.EMAIL_ADDRESS.matcher(target).matches()&&target.toString().contains("rknec.edu"));
        }
    }
}
