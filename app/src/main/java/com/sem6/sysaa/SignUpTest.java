package com.sem6.sysaa;

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

    //TextView info;
    private Firebase fb1;
    private EditText mName;
    private EditText emailtext;
    private EditText passwordtext;
    private EditText repasswordtext;
    private EditText batch;
    private EditText roll;
    private EditText sem;
    private EditText shift;
    private Button signUpBtn;
    private RelativeLayout li;
    private RadioGroup designation;

    public String departmentSelected;
    public String email;
    public String password;
    public String repassword;
    private String checkuid;

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

        //info=(TextView) findViewById(R.id.info);
        //TelephonyManager tm=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //macAddress=tm.getDeviceId();
        macAddress= android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        signUpBtn = (Button) findViewById(R.id.signupBtn);

        signUpBtn.setOnClickListener(this);
        TextView haveacc=(TextView) findViewById(R.id.haveAccount);
        fb1=new Firebase("https://sysaa-be58b.firebaseio.com/StuUsers/"+macAddress+"/UID");
        fb1.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                checkuid=dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
        boolean rollOK = false;
        boolean semOK = false;
        boolean shiftOK = false;
        boolean batchOK = false;

        roll=(EditText) findViewById(R.id.rollNo);
        sem=(EditText) findViewById(R.id.semester);
        batch=(EditText) findViewById(R.id.batch);
        shift=(EditText) findViewById(R.id.shift);
        mName = (EditText) findViewById(R.id.editName);
        emailtext = (EditText) findViewById(R.id.sign_up_email_text);
        passwordtext = (EditText) findViewById(R.id.sign_up_password_text);
        repasswordtext = (EditText) findViewById(R.id.sign_up_repassword_text);
        li = (RelativeLayout) findViewById(R.id.sign_up_relative);


        final String Name = mName.getText().toString().trim();
        final String rollno= roll.getText().toString().trim();
        final String batchnum= batch.getText().toString().trim();
        final String shiftt=shift.getText().toString().trim();
        final String semester=sem.getText().toString().trim();
        //Validate for empty Name field
        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(SignUpTest.this, "Please Enter Name", Toast.LENGTH_LONG).show();
        } else {
            nameAllGood = true;
        }
        //valid rollnumber
        if (TextUtils.isEmpty(rollno))
        {
            Toast.makeText(SignUpTest.this, "Please Enter Roll no.", Toast.LENGTH_LONG).show();
        }
        else if(Integer.parseInt(rollno)<0)
        {
            Toast.makeText(SignUpTest.this, "Enter Valid Roll no", Toast.LENGTH_LONG).show();
        }
        else
            rollOK=true;

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

        //shift check
        if (TextUtils.isEmpty(shiftt))
        {
            Toast.makeText(SignUpTest.this, "Please Enter Shift", Toast.LENGTH_LONG).show();
        }
        else if(shiftt.equals("1")||shiftt.equals("2"))
            shiftOK=true;
        else
            Toast.makeText(SignUpTest.this, "Please Enter Valid Shift", Toast.LENGTH_LONG).show();


        //sem check
        if (TextUtils.isEmpty(semester))
        {
            Toast.makeText(SignUpTest.this, "Please Enter Semester", Toast.LENGTH_LONG).show();
        }
        else if(Integer.parseInt(semester)>0&&Integer.parseInt(semester)<9)
            semOK=true;
        else
            Toast.makeText(SignUpTest.this, "Please Enter Valid Semester", Toast.LENGTH_LONG).show();


        //batch check
        if (TextUtils.isEmpty(batchnum))
        {
            Toast.makeText(SignUpTest.this, "Please Enter Batch", Toast.LENGTH_LONG).show();
        }
        else if ((batchnum.toUpperCase().equals("A1")||batchnum.toUpperCase().equals("A2")||batchnum.toUpperCase().equals("A3")||
                batchnum.toUpperCase().equals("A4"))&&shiftt.equals("1"))
        {
            shiftOK=true;
            batchOK=true;
        }
        else if((batchnum.toUpperCase().equals("B1")||batchnum.toUpperCase().equals("B2")||batchnum.toUpperCase().equals("B3")||
                batchnum.toUpperCase().equals("B4"))&&shiftt.equals("2"))
        {
            shiftOK=true;
            batchOK=true;
        }
        else
            Toast.makeText(SignUpTest.this, "Please Enter Valid Batch", Toast.LENGTH_LONG).show();


        //Complete SignUpAction
        if (nameAllGood && emailAllGood && passwordAllGood && rollOK&& batchOK&&semOK&&shiftOK) {
            signUpOk = true;
        }


        if (signUpOk) {

            //Initializing FireBase Auth object
            firebaseAuth = FirebaseAuth.getInstance();

            //chck mac id exits or not!

            if(checkuid==null)
            {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                //checking if success
                                //info.setText(macAddress+"\n"+checkuid);
                                if (task.isSuccessful()) {
                                    //display some message here
                                    Firebase fb=new Firebase("https://sysaa-be58b.firebaseio.com/StuUsers/");
                                    Firebase fb1=new Firebase("https://sysaa-be58b.firebaseio.com/Att/");
                                    //Set an attribute child in it as name (as user entered during sign up
                                    final String user_id = firebaseAuth.getCurrentUser().getUid();  //Set parent as UID
                                    //info.setText(info.getText()+"\n adding to maindb"+(i++));
                                    fb.child(macAddress).child("UID").setValue(user_id);
                                    fb.child(macAddress).child("Name").setValue(Name.toUpperCase());
                                    fb.child(macAddress).child("Roll").setValue(rollno);
                                    fb.child(macAddress).child("Batch").setValue(batchnum.toUpperCase());
                                    fb.child(macAddress).child("Semester").setValue(semester);
                                    fb.child(macAddress).child("Shift").setValue(shiftt);
                                    fb1.child(macAddress).child("Total").child("AI").setValue(0);
                                    fb1.child(macAddress).child("Total").child("DBMS").setValue(0);
                                    fb1.child(macAddress).child("Total").child("IWCS").setValue(0);
                                    fb1.child(macAddress).child("Total").child("DAA").setValue(0);
                                    startActivity(new Intent(SignUpTest.this, Nav.class));
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

            /*final Handler handler = new Handler();
            if(checkuid==null)
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            //Create a new node named "Users" in your real-time database.
                            mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Users");
                            //info.setText(info.getText()+"\n handler"+i++);
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
*/


        }
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return (Patterns.EMAIL_ADDRESS.matcher(target).matches()&&target.toString().contains("rknec.edu"));
        }
    }
}