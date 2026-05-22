package com.example.base;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private Ringtone ringtone;
    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        switch(intent.getAction().toString().trim()){
            case "Play":
                this.play();
                break;
            case "Stop":
                this.stop();
                break;
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void play(){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(),uri);
        ringtone.play();
    }

    public void stop(){
        if(ringtone == null )return;
        if(ringtone != null || ringtone.isPlaying()){
            ringtone.stop();
        }
    }
}