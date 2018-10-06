package com.example.csn254.soundprofile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

public class Home extends AppCompatActivity {

    ImageButton floatButton;
    Switch Switch_slot;
    ListView slot_list;
    Context ctx = this;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        display_slot_list();
        floatButton = (ImageButton) findViewById(R.id.addButton);
        //listener for add button
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_add_slot_activity();
            }
        });

        Switch_slot = (Switch) findViewById(R.id.switch_slot);
        Switch_slot.setChecked(true);
        //listener for switch_slot
        Switch_slot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    Log.e("toggle", "added");
                    add_all_slots();
                }else{
                    Log.e("toggle", "cancelled");
                    try{
                        cancel_all_slot();
                    }catch (CursorIndexOutOfBoundsException e){
                        Toast.makeText(getBaseContext(), "Nothing to cancel", Toast.LENGTH_SHORT).show();
                        Switch_slot.setChecked(true);
                    }

                }

            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //launch the add activity
    public void launch_add_slot_activity() {
        Intent i = new Intent(this, add_slot.class);
        finish();
        startActivity(i);
    }

    //fetch all the entries from slot db and display them
    public void display_slot_list() {
        int count = 0;
        time_slot_db DOP = new time_slot_db(ctx);
        Cursor CR = DOP.getInformation(DOP);

        CR.moveToFirst();
        do {
            count++;
        } while (CR.moveToNext());


        try {
            String[] slot_day_list = new String[count];
            String[] slot_time_list = new String[count];
            int i = 0;
            CR.moveToFirst();
            do {
                slot_day_list[i] = CR.getString(0);
                slot_time_list[i] = CR.getString(1) + " - " + CR.getString(2);
                //Log.d("display",slot_day_list[i]+slot_time_list[i]);
                i++;
            } while (CR.moveToNext());


            ListAdapter adapter = new custom_slot_adapter(this, slot_day_list, slot_time_list);
            slot_list = (ListView) findViewById(R.id.slot_list);
            slot_list.setAdapter(adapter);
            //listener for each element in the list while long-pressing
            slot_list.setOnItemLongClickListener(
                    new AdapterView.OnItemLongClickListener(){
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            String day = String.valueOf(parent.getItemAtPosition(position));
                            String time_duration=String.valueOf(((TextView)parent.getChildAt(position).findViewById(R.id.time_duration)).getText().toString());
                            String times[] = time_duration.split("-");
                            time_slot_db DB = new time_slot_db(ctx);
                            DB.deleteSlot(DB, day, times[0].trim(), times[1].trim());
                            cancelSlot(day, times[0].trim(), times[1].trim());
                            finish();
                            startActivity(getIntent());
                            Toast.makeText(getBaseContext(),"Cancelled "+day+" "+time_duration, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
            );

        } catch (CursorIndexOutOfBoundsException e) {
            Toast.makeText(getBaseContext(), "Please add a slot FIRST", Toast.LENGTH_LONG).show();
        }


    }

    //cancel all the slote on toggle off
    public void cancel_all_slot(){
        time_slot_db DOP = new time_slot_db(ctx);
        Cursor CR = DOP.getInformation(DOP);

        CR.moveToFirst();
        do {
            cancelSlot(CR.getString(0).trim(), CR.getString(1).trim(), CR.getString(2).trim());
        } while (CR.moveToNext());
    }

    //cancel a particular slot
    public void cancelSlot(String day, String start_time, String end_time){
        int hours = Integer.parseInt(start_time.split(":")[0].trim());
        int mins = Integer.parseInt(start_time.split(":")[1].trim());
        if (mins == 0){
            mins = 59;
            hours = (hours == 0) ? 23:(hours-1);
        }else {
            mins = (mins-1)%60;
        }
        int start_req_ID = hours*100+mins;
        hours = Integer.parseInt(end_time.split(":")[0].trim());
        mins = Integer.parseInt(end_time.split(":")[1].trim());
        if (mins == 0){
            mins = 59;
            hours = (hours == 0) ? 23:(hours-1);
        }else {
            mins = (mins-1)%60;
        }
        int end_req_ID = hours*100+mins;
        String dayz[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i=1; i<=7; i++){
            if (dayz[i-1].equals(day.trim())){
                start_req_ID += i*10000;
                end_req_ID += i*10000;
            }
        }

        PendingIntent pendingstartIntent = PendingIntent.getBroadcast(getApplicationContext(), start_req_ID, new Intent(getApplicationContext(), Alarm_Receiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingendIntent = PendingIntent.getService(getApplicationContext(), end_req_ID, new Intent(getApplicationContext(), Alarm_Receiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_manager.cancel(pendingstartIntent);
        Log.e("cancelslot",""+start_time+"-"+start_req_ID);
        alarm_manager.cancel(pendingendIntent);
        Log.e("cancelslot",""+end_time+"-"+end_req_ID);
    }

    //add all alots in DB for sound-profile changing
    public void add_all_slots(){
        time_slot_db DOP = new time_slot_db(ctx);
        Cursor CR = DOP.getInformation(DOP);

        CR.moveToFirst();
        do {
            int sHour = Integer.parseInt(CR.getString(1).split(":")[0].trim());
            int sMin = Integer.parseInt(CR.getString(1).split(":")[1].trim());
            setSlotTime(CR.getString(0).trim(), sHour, sMin, true);
            int eHour = Integer.parseInt(CR.getString(2).split(":")[0].trim());
            int eMin = Integer.parseInt(CR.getString(2).split(":")[1].trim());
            setSlotTime(CR.getString(0).trim(), eHour, eMin, false);
        } while (CR.moveToNext());
    }

    //set a particular slot for sound profile changing
    public void setSlotTime(String day, int hours, int minutes, boolean switchToVibrate){
        Calendar calendar = Calendar.getInstance();
        Intent my_intent = new Intent(getApplicationContext(), Alarm_Receiver.class);
        my_intent.putExtra("switchToVibrate", switchToVibrate);

        int dayOfWeek=-1;
        String dayz[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i=1; i<=7; i++){
            if (dayz[i-1].equals(day.trim())){
                dayOfWeek = i;
            }
        }

        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        if (minutes == 0){
            minutes = 59;
            hours = (hours == 0) ? 23:(hours-1);
        }else {
            minutes = (minutes-1)%60;
        }
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        int reqID = dayOfWeek*10000 + hours*100 + minutes;
        PendingIntent pending_intent = PendingIntent.getBroadcast(getApplicationContext(), reqID, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pending_intent);
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
