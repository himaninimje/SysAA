package com.sem6.sysaa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TeacherPresent extends AppCompatActivity //implements AdapterView.OnItemSelectedListener
{
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private Button setDate;
    private int year, month, day;
    private Firebase teacher;
    private TextView tv;
    private String today;
    private String today_date;
    private String subs;
    private String sub;
    private Spinner spinner;
    private ListView std_list;
    private Iterator<DataSnapshot> itr;
    private Iterator<DataSnapshot> itr2;
    private List<String> subjects;
    private List<String> students;
    private Firebase student;
    private FirebaseAuth mAuth;
    private Firebase student2;
    private ArrayAdapter<String> arrayAdapter;
    private  ArrayAdapter<String> arrayAdapter2;
    private String subb;
    private String uid;
    private String tmpp;
    private int i=0;
    private ArrayList<String> uid_list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_present);
        dateView = (TextView) findViewById(R.id.date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        setDate=(Button) findViewById(R.id.setdate);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
                Toast.makeText(getApplicationContext(), "Select Date",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int year, int month, int day) {
                    // TODO Auto-generated method stub
                    showDate(year, month+1, day);
                }
            };

    private void showDate(int year, int month, int day) {
        if (month<10)
            dateView.setText(day+"-0"+month+"-"+year);
        else
            dateView.setText(day+"-"+month+"-"+year);
        Date d=new Date(year,month,day);
        int weekdaynum=d.getDay();
        String weekDay="";

        switch (weekdaynum)
        {
            case 0:
                weekDay="THURSDAY";break;
            case 1:
                weekDay="FRIDAY";break;
            case 2:
                weekDay="SATURDAY";break;
            case 3:
                weekDay="SUNDAY";break;
            case 4:
                weekDay="MONDAY";break;
            case 5:
                weekDay="TUESDAY";break;
            case 6:
                weekDay="WEDNESDAY";break;
        }
        dateView.setText(dateView.getText()+"....\n"+weekDay);
        //showPresent(weekDay);
    }
    /*public  void showPresent(String day)
    {
        tv=(TextView)findViewById(R.id.textView5);
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        students=new ArrayList<String>();
        students.add("init");
        std_list=(ListView) findViewById(R.id.std_list);

        SimpleDateFormat sddf = new SimpleDateFormat("EEEE");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        today=sddf.format(c.getTime());
        today_date=df.format(c.getTime());
        subjects=new ArrayList<String>();
        //today="MONDAY";
        today=day;
        mAuth=FirebaseAuth.getInstance();

        teacher=new Firebase("https://sysaa-be58b.firebaseio.com/TEACHER/"+mAuth.getCurrentUser().getUid().toString().trim()
                +"/Timetable/"+today);
        teacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itr=dataSnapshot.getChildren().iterator();

                while(itr.hasNext())
                {
                    subs=itr.next().toString();
                    sub=subs.substring(subs.lastIndexOf("=")+2,subs.lastIndexOf("}")-1);
                    Log.d("sub_len",sub+" "+Integer.toString(sub.length()));
                    subjects.add(sub);


                }
                Log.d("list_uid",uid_list.toString());

                arrayAdapter=new ArrayAdapter<String>(TeacherPresent.this,android.R.layout.simple_spinner_item,subjects);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tv.setText("");
        uid_list.clear();
        i=0;
        students.clear();

        subb=parent.getItemAtPosition(position).toString();
        Log.d("subject",Integer.toString(subb.length()));
        final String attn_date="21-04-2017";

        student=new Firebase("https://sysaa-be58b.firebaseio.com/Att");
        student.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String temp=dataSnapshot.getValue().toString();
                Log.d("listsub",temp);
                itr2=dataSnapshot.getChildren().iterator();

                while(itr2.hasNext())
                {

                    String obj=itr2.next().toString();
                    Log.d("whole object",obj);
                    uid =obj.substring(obj.indexOf("=")+2,obj.indexOf(","));
                    Log.d("student id",uid);
                    uid_list.add(uid);

                }
                Log.d("list_uid",uid_list.toString());
                Iterator itr3=uid_list.iterator();
                while ((itr3.hasNext()))
                {
                    student2=new Firebase("https://timetable-7e2a8.firebaseio.com/Student/"+itr3.next()+"/"+attn_date);
                    student2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            tmpp=uid_list.get(i);
                            i++;
                            if(dataSnapshot.getValue().toString().contains(subb))
                            {
                                Toast.makeText(TeacherPresent.this, "inside studentttt", Toast.LENGTH_SHORT).show();

                                students.add(tmpp);
                                Log.d("listvw",students.toString());
                                arrayAdapter2=new ArrayAdapter<String>(TeacherPresent.this,android.R.layout.simple_list_item_1,students);
                                std_list.setAdapter(arrayAdapter2);
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}
