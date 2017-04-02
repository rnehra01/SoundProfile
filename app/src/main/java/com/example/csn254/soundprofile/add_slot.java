package com.example.csn254.soundprofile;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    //time_slot_db dbHandler;
    private TextView start_time_display;
    private TextView end_time_display;
    private Button pick_start_time;
    private Button pick_end_time;
    String stime;
    String etime;
    //int sMin, eMin;
    private int pHour;
    private int pMinute;
    private int TIME_DIALOG_ID = 0;
    Context ctx = this;

    Spinner day_spinner;
    ArrayAdapter<CharSequence> adapter;
    String DayStatus;

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Context context;
    PendingIntent pending_intent;

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHour = hourOfDay;
                    pMinute = minute;
                    updateDisplay();
                    //displayToast();
                }
            };

    /** Updates the time in the TextView */
    private void updateDisplay() {
        if (TIME_DIALOG_ID == 0) {
            start_time_display.setText(
                    new StringBuilder()
                            .append(pad(pHour)).append(":")
                            .append(pad(pMinute)));

            stime = (String)(pad(pHour)) + ":" + (String)(pad(pMinute));
        }
        else{
            end_time_display.setText(
                    new StringBuilder()
                            .append(pad(pHour)).append(":")
                            .append(pad(pMinute)));

            etime = (String)(pad(pHour)) + ":" + (String)(pad(pMinute));

        }

    }

    /** Displays a notification when the time is updated */
//    private void displayToast() {
//        Toast.makeText(this, new StringBuilder().append("Time choosen is ").append(displayTime.getText()),   Toast.LENGTH_SHORT).show();
//
//    }

    /** Add padding to numbers less than ten */
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
        day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                DayStatus = parent.getItemAtPosition(position) + "";
                Toast.makeText(getBaseContext(), "entered day " + DayStatus, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        start_time_display = (TextView) findViewById(R.id.start_time_display);
        end_time_display = (TextView) findViewById(R.id.end_time_display);
        pick_start_time = (Button) findViewById(R.id.start_time);
        pick_end_time = (Button) findViewById(R.id.end_time);

        pick_start_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TIME_DIALOG_ID = 0;
                showDialog(0);
            }
        });

        pick_end_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TIME_DIALOG_ID = 1;
                showDialog(0);
            }
        });


        //initialize our alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize our time picker
        //alarm_timepicker = (TimePicker) findViewById(R.id.clock);
        final Calendar calendar = Calendar.getInstance();

        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        Button save_button = (Button) findViewById(R.id.save_button);

        save_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_slot_db DB = new time_slot_db(ctx);
                DB.addSlot(DB, DayStatus, stime, etime);

                finish();
            }
        });
//        start_alarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //setting calendar instance with the hour and minute that we picked
//                //on tne time picker
//                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getCurrentHour());
//                calendar.set(Calendar.MINUTE, alarm_timepicker.getCurrentMinute());
//
//                //get the string value of the hour and minute
//                int hour = alarm_timepicker.getCurrentHour();
//                int minute = alarm_timepicker.getCurrentMinute();
//
//                //convert the int values to strings
//                String hour_string = String.valueOf(hour);
//                String minute_string = String.valueOf(minute);
//
//                //convert 24-hour to 12-hour time
//                /*if(hour>12)
//                    hour_string = String.valueOf(hour-12);
//                if(minute<10){
//                    minute_string = "0" + String.valueOf(minute);
//                }*/
//
//                pending_intent = PendingIntent.getBroadcast(add_slot.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
//
//            }
//        });
        //initialize the stop Button
        Button stop_alarm = (Button) findViewById(R.id.stop_button);

//        stop_alarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alarm_manager.cancel(pending_intent);
//            }
//        });
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
