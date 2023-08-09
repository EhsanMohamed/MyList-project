package com.mylist_app.mylist.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mylist_app.mylist.MainActivity;


public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals("stop")) {
            Intent i = new Intent(context, RingTone.class);
            context.stopService(i);

        }else{
            Intent i = new Intent(context, RingTone.class);
            context.stopService(i);
            Intent tapIntent = new Intent(context, MainActivity.class);
            tapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(tapIntent);
        }



}


}