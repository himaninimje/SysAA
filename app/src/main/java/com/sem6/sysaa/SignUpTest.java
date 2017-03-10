package com.sem6.sysaa;

/**
 * Created by pd on 27-02-2017.
 */
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.FirebaseDatabase;

import android.os.Handler;

import java.util.ArrayList;

public class SignUpTest extends AppCompatActivity implements View.OnClickListener {

    TextView info;
    private EditText mName;
    private EditText emailtext;
    private EditText passwordtext;
    private EditText repasswordtext;
    private Button signUpBtn;
    private RelativeLayout li;
    private RadioGroup designation;

    public String departmentSelected;
    public String email;
    public String password;
    public String repassword;
    String checkuid;

    //defining FireBase Auth object
    public FirebaseAuth firebaseAuth;
    int i=0;
    //defining FireBase real-time database object
    public DatabaseReference mDatabase1;
    String macAddress;
            //Toast.makeText(SignUpTest.this, macAddress, Toast.LENGTH_LONG).show();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_test);
        /*WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        macAddress = wInfo.getMacAddress();*/

        //TelephonyManager tm=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //macAddress=tm.getDeviceId();
        macAddress= android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        signUpBtn = (Button) findViewById(R.id.signupBtn);

        signUpBtn.setOnClickListener(this);
        TextView haveacc=(TextView) findViewById(R.id.haveAccount);
        haveacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignUpTest.this, MainActivity.class));
            }
        });
    }


    //After clicking Sign Up button, following method is executed
    @Override
    public void onClick(final View view) {

        boolean signUpOk = false;
        boolean nameAllGood = false;
        boolean emailAllGood = false;
        boolean passwordAllGood = false;


        mName = (EditText) findViewById(R.id.editName);
        emailtext = (EditText) findViewById(R.id.sign_up_email_text);
        passwordtext = (EditText) findViewById(R.id.sign_up_password_text);
        repasswordtext = (EditText) findViewById(R.id.sign_up_repassword_text);
        li = (RelativeLayout) findViewById(R.id.sign_up_relative);


        final String Name = mName.getText().toString().trim();

        //Validate for empty Name field
        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(SignUpTest.this, "Please Enter Name", Toast.LENGTH_LONG).show();
        } else {
            nameAllGood = true;
        }


        //Validate Email
        email = emailtext.getText().toString().trim();
        if (!isValidEmail(email)) {
            emailtext.setError("Enter a valid Email Address");
            Toast.makeText(this, R.string.incorrect_email, Toast.LENGTH_LONG).show();
        } else {
            emailAllGood = true;
        }

        //Validate Password
        password = passwordtext.getText().toString();
        repassword = repasswordtext.getText().toString();
        if (password.length() < 8) {
            passwordtext.setError("Must be atleast 8 characters long");
            Toast.makeText(this, "Must be atleast 8 characters long", Toast.LENGTH_LONG).show();
        } else if (password.length() >= 8) {
            if (!password.equals(repassword)) {
                passwordtext.setError("Passwords do not match.");
                Toast.makeText(this, "Password do not match", Toast.LENGTH_LONG).show();
            } else {
                passwordAllGood = true;
            }
        }


        //Complete SignUpAction
        if (nameAllGood && emailAllGood && passwordAllGood) {
            signUpOk = true;
        }


        if (signUpOk) {

            //Initializing FireBase Auth object
            firebaseAuth = FirebaseAuth.getInstance();

            //chck mac id exits or not!
            /*Firebase fb=new Firebase("https://sysaa-be58b.firebaseio.com/example");
            final ArrayList<String> userlist=new ArrayList<>();

            fb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value =dataSnapshot.getValue(String.class);
                    userlist.add(value);
                    System.out.println(userlist);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });*/
            Firebase fb1=new Firebase("https://sysaa-be58b.firebaseio.com/example/"+macAddress+"/UID");
            fb1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    checkuid= dataSnapshot.getValue(String.class);
                    info.setText(info.getText()+"\n checkamc here "+checkuid);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            //checkmac=fb1.child(macAddress).child("uid").getSpec().toString();
            info=(TextView) findViewById(R.id.info);
            info.setText(macAddress+"\n"+checkuid);
            info.setText(info.getText()+"\n before adding to maindb"+(i++));
            //creating a new user in FireBase Auth Database
            if(checkuid==null)
            {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                //checking if success
                                info.setText(macAddress+"\n"+checkuid);
                                if (task.isSuccessful()) {
                                    //display some message here
                                    Firebase fb=new Firebase("https://sysaa-be58b.firebaseio.com/example/");
                                    //Set an attribute child in it as name (as user entered during sign up
                                    final String user_id = firebaseAuth.getCurrentUser().getUid();  //Set parent as UID
                                    info.setText(info.getText()+"\n adding to maindb"+(i++));
                                    fb.child(macAddress).child("UID").setValue(user_id);
                                    Toast.makeText(SignUpTest.this, "Registration Success", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
            else
            {
                Toast.makeText(SignUpTest.this, "Registration Failed, Duplicate MAC ID", Toast.LENGTH_LONG).show();
            }

            /*  This code is executed after 4000ms or 4 seconds because first the user is created with
                a randomly generated firebase unique UID in the Authentication Database and ONLY THEN that UID is taken to create a
                child "Users" in the Real-time Database from the Authentication Database. If this delay is not given, it will
                result in a NULL pointer exception as the user will not be allotted a UID in sufficient time and by then the Firebase
                will search for an empty "Users" child in Real-time database thus resulting in app crash with Null Pointer Exception.
             */

            final Handler handler = new Handler();
            if(checkuid==null)
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            //Create a new node named "Users" in your real-time database.
                            mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Users");
                            info.setText(info.getText()+"\n handler"+i++);
                            final String Name = mName.getText().toString().trim();      //Set an attribute child in it as name (as user entered during sign up
                            final String user_id = firebaseAuth.getCurrentUser().getUid();  //Set parent as UID

                            final String emailid = emailtext.getText().toString().trim();
                            final String pass = passwordtext.getText().toString().trim();

                            //add name attribute and set its value as Name entered by user under "Users"
                            mDatabase1.child(user_id).child("name").setValue(Name);

                            //add emailid attribute and set its value as E-Mail entered by user under "Users"
                            mDatabase1.child(user_id).child("email").setValue(emailid);

                            //pushing a new parent "Second Empty Patent
                            mDatabase1.child("Second Empty Parent");

                            //Creating a new child password under the parent "Second Empty Parent"
                            mDatabase1.child("Second Empty Parent").child("password").setValue(pass);

                        } catch (Exception e) {
                            Toast.makeText(SignUpTest.this, "", Toast.LENGTH_LONG).show();
                        }
                    }
                }, 4000);   //4 seconds delay



        }
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}