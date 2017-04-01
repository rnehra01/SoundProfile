package com.example.csn254.soundprofile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    ImageButton floatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        floatButton = (ImageButton) findViewById(R.id.addButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_add_slot_activity();
            }
        });
    }

    public void launch_add_slot_activity(){
        Intent i=new Intent(this, add_slot.class);
        startActivity(i);
    }
}
