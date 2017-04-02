package com.example.csn254.soundprofile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.util.Log;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.appindexing.Action;
/**
 * Created by suraj on 2/4/17.
 */

public class Alarm_Receiver extends BroadcastReceiver {
    //private GoogleApiClient client;
    //private

    @Override
    public void onReceive(Context context, Intent intent) {
  //      client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        Log.e("We are in the receiver.", "YAY!");
//        final AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //create an intent

        Intent service_intent = new Intent(context, ProfileChangeService.class);

        context.startService(service_intent);
    }
}
