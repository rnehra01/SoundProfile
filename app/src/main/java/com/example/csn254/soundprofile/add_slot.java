package com.example.csn254.soundprofile;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by suraj on 1/4/17.
 */

public class add_slot extends AppCompatActivity {
    private TextView start_time_display;
    private TextView end_time_display;
    private Button pick_start_time;
    private Button pick_end_time;
    int sHour = -1,sMin = -1;
    int eHour =-1,eMin =-1;
    String stime;
    String etime;

    private int pHour;
    private int pMinute;
    private int TIME_DIALOG_ID = 0;
    Context ctx = this;

    Spinner day_spinner;
    ArrayAdapter<CharSequence> adapter;
    String DayStatus;
    int dayStatusId;
    int start_time_req_id;
    int end_time_req_id;

    AlarmManager alarm_manager;
    PendingIntent pending_intent;
    Context context;

    // open the time picker dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHour = hourOfDay;
                    pMinute = minute;
                    updateDisplay();
                }
            };

    /** Updates the time in the TextView */
    private void updateDisplay() {
        if (TIME_DIALOG_ID == 0) {
            start_time_display.setText(
                    new StringBuilder()
                            .append(pad(pHour)).append(":")
                            .append(pad(pMinute)));
            sHour = pHour;
            sMin = pMinute;
            stime = (String)(pad(pHour)) + ":" + (String)(pad(pMinute));
        }
        else{
            end_time_display.setText(
                    new StringBuilder()
                            .append(pad(pHour)).append(":")
                            .append(pad(pMinute)));

            etime = (String)(pad(pHour)) + ":" + (String)(pad(pMinute));
            eHour = pHour;
            eMin = pMinute;
        }

    }

    // Add padding to numbers less than ten
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_slot);
        this.context = this;

        day_spinner = (Spinner) findViewById(R.id.day_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.day_list, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        day_spinner.setAdapter(adapter);
        //ge the vlaue of the selected day
        day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                dayStatusId = position+1;
                DayStatus = parent.getItemAtPosition(position) + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        start_time_display = (TextView) findViewById(R.id.start_time_display);
        end_time_display = (TextView) findViewById(R.id.end_time_display);
        pick_start_time = (Button) findViewById(R.id.start_time);
        pick_end_time = (Button) findViewById(R.id.end_time);
        //listener for start time button
        pick_start_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TIME_DIALOG_ID = 0;
                showDialog(0);
            }
        });
        //listener for end time button
        pick_end_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TIME_DIALOG_ID = 1;
                showDialog(0);
            }
        });


        //initialize our alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Button save_button = (Button) findViewById(R.id.save_button);
        final Intent home=new Intent(this, Home.class);
        //listener for save button
        save_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check valid time
                if (sHour == -1 || sMin ==-1 || eHour == -1 || eMin ==-1){
                    Toast.makeText(getBaseContext(), "Select valid start or end time", Toast.LENGTH_SHORT).show();
                }else {
                    time_slot_db DB = new time_slot_db(ctx);
                    start_time_req_id = dayStatusId*10000+sHour*100+sMin;
                    end_time_req_id = dayStatusId*10000+eHour*100+eMin;
                    //check duplicate entry
                    if (DB.addSlot(DB, DayStatus, start_time_req_id, end_time_req_id, stime, etime)){
                        setSwitchTime(dayStatusId, sHour, sMin, true);
                        setSwitchTime(dayStatusId, eHour, eMin, false);
                        startActivity(home);
                        finish();
                    }else {
                        Toast.makeText(getBaseContext(), "This slot already exists", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        Button home_button = (Button) findViewById(R.id.back_button);
        //listener for home button to go back to home
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(home);
                finish();
            }
        });
    }

    //set profile change for a given slot
    public void setSwitchTime(int dayOfWeek, int hours, int minutes, boolean switchToVibrate){
        Calendar calendar = Calendar.getInstance();
        Intent my_intent = new Intent(getApplicationContext(), Alarm_Receiver.class);

        my_intent.putExtra("switchToVibrate", switchToVibrate);

        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        if (minutes == 0){
            minutes = 59;
            hours = (hours == 0) ? 23:(hours-1);
        }else {
            minutes = (minutes-1)%60;
        }
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        //calendar.set(Calendar.SECOND, 0);
        int reqID = dayOfWeek*10000 + hours*100 + minutes;
        Log.d("setslot","Inside : "+dayOfWeek+"-"+hours+"-"+minutes+"-"+reqID);
        pending_intent = PendingIntent.getBroadcast(getApplicationContext(), reqID, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pending_intent);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new TimePickerDialog(this,
                        mTimeSetListener, pHour, pMinute, false);

        }
        return null;
    }
}
