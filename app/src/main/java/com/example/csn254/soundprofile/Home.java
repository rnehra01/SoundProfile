package com.example.csn254.soundprofile;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    ImageButton floatButton;
    ListView slot_list;
    Context ctx=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        display_slot_list();
        floatButton = (ImageButton) findViewById(R.id.addButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
                launch_add_slot_activity();
            }
        });
    }
    public void launch_add_slot_activity(){
        Intent i=new Intent(this, add_slot.class);
        finish();
        startActivity(i);
    }

    public void display_slot_list(){
        int count = 0;
        time_slot_db DOP = new time_slot_db(ctx);
        final Cursor CR = DOP.getInformation(DOP);


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
                Log.e("err", slot_day_list[i]+slot_time_list[i]);
                i++;
            } while (CR.moveToNext());


            ListAdapter adapter = new custom_slot_adapter(this, slot_day_list, slot_time_list);
            slot_list = (ListView) findViewById(R.id.slot_list);
            slot_list.setAdapter(adapter);

        } catch (CursorIndexOutOfBoundsException e) {
            Toast.makeText(getBaseContext(), "Please add a slot FIRST", Toast.LENGTH_LONG).show();
        }

    }
}
