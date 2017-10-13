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
 // main function of the app which reads the various time slots in the list and toggles sound profile according to the input schedule

public class ProfileChangeService extends Service {
    private AudioManager am;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId){
        boolean switchToVibrate = intent.getExtras().getBoolean("switchToVibrate");
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (switchToVibrate){Log.e("err", "1-SILENTMODE");
            am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }else {Log.e("err", "0-NORMALMODE");
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }

        return START_NOT_STICKY;
    }
}
