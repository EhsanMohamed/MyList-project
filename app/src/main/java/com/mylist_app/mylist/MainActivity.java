package com.mylist_app.mylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mylist_app.mylist.BroadCast.Build_Notification;
import com.mylist_app.mylist.Utils.DatabaseH;
import com.mylist_app.mylist.listadapter.List_Adapter;
import com.mylist_app.mylist.model.List_model;

import java.util.ArrayList;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List_Adapter adp;
    ArrayList<List_model> list_Model;
    DatabaseH databaseH;
    FloatingActionButton flo;
    ImageView main_background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultLanguage();

        main_background=findViewById(R.id.main_background);
        recyclerView =findViewById(R.id.recycler);
        flo=findViewById(R.id.flo);
        databaseH =new DatabaseH(this);




        Glide.with(getApplicationContext()).load(R.drawable.main).into(main_background);

        // Check if the user has not already whitelisted your app from battery optimizations
        checkBattery();

        list_Model = databaseH.get_all_note();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adp=new List_Adapter(list_Model,this);
        recyclerView.setAdapter(adp);
        adp.notifyDataSetChanged();
        main_background.setVisibility(list_Model.isEmpty() ? View.VISIBLE : View.GONE);



        flo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n=new Intent(MainActivity.this,AddToList.class);
                startActivity(n);
                finishAfterTransition();
            }
        });


        //Swipe left and right

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback
                (0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List_model i = list_Model.get(viewHolder.getAbsoluteAdapterPosition());

                int p=viewHolder.getAbsoluteAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {

                    //Alert Show

                    alert( p,i.getId(),i.getReq());

                }else if(direction==ItemTouchHelper.RIGHT) {


                    Intent n =new Intent(MainActivity.this,AddToList.class);
                    n.putExtra("id",i.getId());
                    n.putExtra("Title",i.getTitle());
                    n.putExtra("ِAddNote",i.getAddNote());
                    n.putExtra("Time",i.getTime_p());
                    n.putExtra("Date",i.getDate_p());
                    n.putExtra("req",i.getReq());
                    n.putExtra("status",i.getStatus());

                    startActivity(n);
                    adp.notifyItemChanged(p);

                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor
                                (ContextCompat.getColor(MainActivity.this,R.color.lja ))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                        .addSwipeLeftCornerRadius(2,9)
                        .addSwipeRightCornerRadius(2,9)
                        .addSwipeRightBackgroundColor
                                (ContextCompat.getColor(MainActivity.this,R.color.g))
                        .addSwipeRightActionIcon(R.drawable.ic_edit_sweep)
                        .create()
                        .decorate();



                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);




    }

    private void checkBattery() {
        PowerManager powerManager = (PowerManager) getSystemService(this.POWER_SERVICE);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
            // Display an in-app dialog which will allow the user to exempt your app without leaving it
            startActivity(new Intent(Settings
                    .ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                    Uri.parse("package:"+getPackageName())));
        }
    }


    private void alert(int p, int id,int req) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(" Delete ");
        alertDialog.setMessage("delete note ? ");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adp.notifyItemChanged(p);
            }
        });

        alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AlarmManager alarmManager = (AlarmManager) getSystemService
                        (MainActivity.ALARM_SERVICE);
                Intent intent = new Intent(MainActivity.this, Build_Notification.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        MainActivity.this,req , intent, PendingIntent.FLAG_CANCEL_CURRENT);

                alarmManager.cancel(pendingIntent);

                databaseH.delete_Note(id);
                list_Model.remove(p);
                adp.notifyItemRemoved(p);

                main_background.setVisibility(list_Model.isEmpty() ? View.VISIBLE : View.GONE);

            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();

}

    private void setDefaultLanguage() {

        if(!Locale.getDefault().getLanguage().equals("en"))
        {
            String languageToLoad  = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            this.setContentView(R.layout.activity_main);

        }
    }


}