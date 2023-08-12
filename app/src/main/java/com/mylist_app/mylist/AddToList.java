package com.mylist_app.mylist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mylist_app.mylist.BroadCast.Build_Notification;
import com.mylist_app.mylist.Utils.DatabaseH;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;


public class AddToList extends AppCompatActivity {

    DatabaseH databaseH;

    EditText Title,AddNote;
    TextView SelectTime ,SelectDate;
    Button Save;
    Switch aSwitch;

    Calendar calendar;
    AlarmManager alarmManager;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;

    //make Random code to pendingIntent request code
    Random r=new Random();
    int q=r.nextInt(20);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);
        setDefaultLanguage();

        aSwitch=findViewById(R.id.switch1);
        Title=findViewById(R.id.editText_Title);
        AddNote=findViewById(R.id.editText_AddNote);
        SelectTime=findViewById(R.id.textView_SelectTime);
        SelectDate=findViewById(R.id.textView_SelectDate);
        Save=findViewById(R.id.button_Save);
        databaseH =new DatabaseH(this);
        Context co=this;


        
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(aSwitch.isChecked()){
                    SelectTime.setVisibility(View.VISIBLE);
                    SelectDate.setVisibility(View.VISIBLE);
                }
                else {
                    SelectDate.setText("");
                    SelectTime.setText("");
                    SelectTime.setVisibility(View.GONE);
                    SelectDate.setVisibility(View.GONE);
                }
            }
        });

        SelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(co); }});

        SelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(co);
            }
        });



        Intent intent=getIntent();
        String intent_Title =intent.getStringExtra("Title");
        int id =intent.getIntExtra("id",0);
        int status =intent.getIntExtra("status",0);
        int req =intent.getIntExtra("req",0);
        String intent_AddNote =intent.getStringExtra("ِAddNote");
        String intent_time =intent.getStringExtra("Time");
        String intent_date =intent.getStringExtra("Date");


        if(intent_Title !=null){
            Title.setText(intent_Title);
            AddNote.setText(intent_AddNote);
            SelectTime.setText(intent_time);
            SelectDate.setText(intent_date);
            aSwitch.setChecked(status == 1);
        }





        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String s_Title=Title.getText().toString();
                String s_AddNote=AddNote.getText().toString();
                String time=SelectTime.getText().toString();
                String date=SelectDate.getText().toString();

                if (intent_Title ==null){
                if(aSwitch.isChecked() ) {

                    if(check_EditText(s_Title,s_AddNote,time,date)){

                    databaseH.insert_Note(s_Title, s_AddNote, time, date, q, 1);
                    setAlarm(s_Title,time,date);

                    }
                }

                else {
                    if(check_EditText(s_Title,s_AddNote)) {
                        databaseH.insert_Note(s_Title, s_AddNote, "", "", q, 0);

                    }
                }

                }

                else{
                    if(aSwitch.isChecked() ) {

                        if(check_EditText(s_Title,s_AddNote,time,date)){

                            databaseH.update_Note(id,s_Title,s_AddNote,time,date,req,1);

                            if(!intent_time.equals(time) || !intent_date.equals(date)){
                                setAlarm(s_Title,time,date);}

                        }
                    }

                    else {
                        if(check_EditText(s_Title,s_AddNote)) {

                            AlarmManager alarmManager = (AlarmManager) getSystemService
                                    (MainActivity.ALARM_SERVICE);
                            Intent intent = new Intent(AddToList.this,
                                    Build_Notification.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                    AddToList.this,req , intent, PendingIntent.FLAG_CANCEL_CURRENT);
                            alarmManager.cancel(pendingIntent);

                            databaseH.update_Note(id,s_Title,s_AddNote,"","",req,0);

                        }
                    }


                }



            }
        });


    }


    private boolean check_EditText(String title, String note, String time, String date) {

        if( !(title.isEmpty()||note.isEmpty() ||time.isEmpty()||date.isEmpty()) ){


            Intent n=new Intent(AddToList.this, MainActivity.class);
            startActivity(n);
            finish();
            return true;}

        else {
            Toast.makeText(AddToList.this,"Please write any Notes"
                    ,Toast.LENGTH_LONG).show();
            return false;}
    }


    private boolean check_EditText(String title, String note) {

        if( !(title.isEmpty()||note.isEmpty() ) ){


            Intent n=new Intent(AddToList.this, MainActivity.class);
            startActivity(n);
            finish();
            return true;}

        else {
            Toast.makeText(AddToList.this,"Please write any Notes"
                    ,Toast.LENGTH_LONG).show();
            return false;}
    }




    private void setAlarm( String s_Title,  String time,String date) {


        calendar  = Calendar.getInstance();

        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent i=new Intent(this, Build_Notification.class);

        i.putExtra("title",s_Title);
        i.putExtra("time",time);
        i.putExtra("q",q);

        PendingIntent pendingIntent=PendingIntent.getBroadcast
                (this,q,i,PendingIntent.FLAG_UPDATE_CURRENT);


        String[] _time = time.split("[: \\s+]");
        String[] _date= date.split("-");

        int hour=Integer.parseInt(_time[0]);
        int minute=Integer.parseInt(_time[1]);

        int year=Integer.parseInt(_date[0]);
        int month=Integer.parseInt(_date[1]);
        int day=Integer.parseInt(_date[2]);


        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);


        if (calendar.getTimeInMillis() >= System.currentTimeMillis()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle
                        (AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

            Toast.makeText(this,"Alarm Was Set ",Toast.LENGTH_LONG).show();

        }
        else {
                 Toast.makeText(this,"Time is invalid ",Toast.LENGTH_LONG).show();
        }

    }


    private void showTimePicker(Context co) {

        calendar = Calendar.getInstance();
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE)+1;

        // Launch Time Picker Dialog

          timePickerDialog = new TimePickerDialog(co,
                (view12, hourOfDay, minute) -> {

                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE,minute);
                    calendar.set(Calendar.SECOND,0);

                    String am_pm;
              if(hourOfDay>=12){
                  am_pm=" PM";
              }else{
                  am_pm=" AM";
              }

              String t_m=String.format
                      (Locale.getDefault(), "%02d:%02d",hourOfDay, minute)+am_pm;
              SelectTime.setText(t_m);


              setAutoDate();
                }, mHour, mMinute, false);

          timePickerDialog.show();
    }

    private void setAutoDate() {

        int mYear,mMonth, dayOfmonth;

        calendar = Calendar.getInstance();

        mYear=calendar.get(Calendar.YEAR);
        mMonth=calendar.get(Calendar.MONTH);
        dayOfmonth =calendar.get(Calendar.DAY_OF_MONTH);



        SelectDate.setText(String.format("%04d-%02d-%02d",
                mYear,mMonth+1,dayOfmonth));


    }


    private void showDatePicker(Context co) {


        int mYear,mMonth, dayOfmonth;

        calendar = Calendar.getInstance();

        mYear=calendar.get(Calendar.YEAR);
        mMonth=calendar.get(Calendar.MONTH);
        dayOfmonth =calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog=new DatePickerDialog(co,(view, year, month, dayOfMonth) -> {

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SelectDate.setText(String.format("%04d-%02d-%02d",
                    year,month+1,dayOfMonth));


            if(SelectTime.getText().toString().isEmpty()){
                setAutoTime();}

        },mYear,mMonth, dayOfmonth);

        datePickerDialog.show();



    }

    private void setAutoTime() {

        calendar = Calendar.getInstance();
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        String am_pm;
        if(mHour>=12){
            am_pm=" PM";
        }else{
            am_pm=" AM";
        }

        String t_m=String.format
                (Locale.getDefault(), "%02d:%02d",mHour, mMinute )+am_pm;
        SelectTime.setText(t_m);

    }


    private void setDefaultLanguage() {

        if(!Locale.getDefault().getLanguage().equals("en"))
        {
            String languageToLoad  = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            this.setContentView(R.layout.activity_add_to_list);

        }
    }



}