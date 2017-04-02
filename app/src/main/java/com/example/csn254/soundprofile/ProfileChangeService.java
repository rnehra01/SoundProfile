package com.example.csn254.soundprofile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.security.Provider;


/**
 * Created by suraj on 2/4/17.
 */

public class ProfileChangeService extends Service {
    private AudioManager am;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId){
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Log.i("Local Service", "Received start id" + startId + ": " + intent);
        Toast.makeText(this, "onStart called", Toast.LENGTH_SHORT).show();
        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        return START_NOT_STICKY;
    }
}
