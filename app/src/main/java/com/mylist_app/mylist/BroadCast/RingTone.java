package com.mylist_app.mylist.BroadCast;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;



public class RingTone extends Service {


    private Ringtone ringtone;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(this, alarmUri);
            ringtone.play();


        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        ringtone.stop();
        NotificationManager notificationManager =
                (NotificationManager)getSystemService
                        (NOTIFICATION_SERVICE);
        notificationManager.cancel(119988);

    }}