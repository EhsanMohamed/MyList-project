package com.mylist_app.mylist.BroadCast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mylist_app.mylist.R;


public class Build_Notification extends  BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {



        //set notification channel
        notificationChannel(context);

        int q=intent.getIntExtra("q",20);

        Intent stopIntent = new Intent(context, AlarmReceiver.class);
        stopIntent.setAction("stop");
        PendingIntent p_stop = PendingIntent.getBroadcast(context, q, stopIntent, 0);


        Intent tapIntent = new Intent(context, AlarmReceiver.class);
        tapIntent.setAction("close");
        PendingIntent p_tapIntent = PendingIntent.getBroadcast(context, q, tapIntent, 0);




        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                "x1")
                .setContentTitle(title)
                .setContentText(time)
                .setAutoCancel(false)
                .setContentIntent(p_tapIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_edit_sweep, "Stop", p_stop)
                .setSmallIcon(R.drawable.ic_baseline_done_all_24);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        Intent i = new Intent(context, RingTone.class);
        nm.notify(119988, builder.build()) ;
        context.startService(i);

    }


    private void notificationChannel(Context context){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            NotificationChannel c =new NotificationChannel("x1","xe",
                    NotificationManager.IMPORTANCE_HIGH);

            c.setDescription("Channel of MyList");

            NotificationManager nm=context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(c); } }


}
