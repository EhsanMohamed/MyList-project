package com.mylist_app.mylist.listadapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mylist_app.mylist.AddToList;
import com.mylist_app.mylist.R;
import com.mylist_app.mylist.Utils.DatabaseH;
import com.mylist_app.mylist.model.List_model;

import java.util.ArrayList;


public class List_Adapter extends RecyclerView.Adapter<List_Adapter.List_holder> {

    private final ArrayList<List_model> l_model;
    private DatabaseH db;
    private final Context c;

    public List_Adapter(ArrayList<List_model> l_model, Context c) {
        this.l_model = l_model;
        this.c=c;
    }




    @NonNull
    @Override
    public List_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext())
               .inflate(R.layout.list_xml,null,false);
        return new List_holder(v);
    }

    public class List_holder extends RecyclerView.ViewHolder {
        TextView tx_main,tx_time,tx_date;
        LinearLayout linearLayout;
        public List_holder(@NonNull View itemView) {
            super(itemView);


            tx_main=itemView.findViewById(R.id.textView_main);
            tx_time=itemView.findViewById(R.id.textView_time);
            tx_date=itemView.findViewById(R.id.textView_date);
            linearLayout =itemView.findViewById(R.id.linearLayout);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull List_holder holder, int position) {


        List_model item=l_model.get(position);


        db=new DatabaseH(c);

        holder.tx_main.setText(item.getTitle());
        holder.tx_time.setText(item.getTime_p());
        holder.tx_date.setText(item.getDate_p());


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddClass(item);
            }
        });


    }

    private void goToAddClass(List_model item) {

        Intent n =new Intent(c, AddToList.class);
        n.putExtra("id",item.getId());
        n.putExtra("Title",item.getTitle());
        n.putExtra("ِAddNote",item.getAddNote());
        n.putExtra("Time",item.getTime_p());
        n.putExtra("Date",item.getDate_p());
        n.putExtra("req",item.getReq());
        n.putExtra("status",item.getStatus());

        c.startActivity(n);
    }


    @Override
    public int getItemCount() {
        return l_model.size();
    }




}
