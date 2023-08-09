package com.mylist_app.mylist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mylist_app.mylist.model.List_model;

import java.util.ArrayList;



public class DatabaseH extends SQLiteOpenHelper {

    private static final int version=1;
    private static final String DB_name="list";
    private static final String table_name ="my_table";

    private static final String Id="Id";
    private static final String TITLE="Title";
    private static final String AddNote ="Note";
    private static final String Time ="time";
    private static final String Date ="date";
    private static final String Req ="req";
    private static final String Status ="status";


    SQLiteDatabase db ;


    public DatabaseH(@Nullable Context context) {
        super(context,DB_name ,
                null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String t ="CREATE TABLE " +table_name + "("+ Id +
                " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + AddNote +" TEXT," +TITLE +" TEXT,"+Time+" TEXT,"+Date+"" +
                " TEXT,"+Status+" BOOLEAN,"+Req+" INTEGER )";

        db.execSQL(t);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }



    public boolean insert_Note(String s_Title, String s_AddNote,String s_Time, String s_Date,int req,int status){

        db =getWritableDatabase();

        ContentValues v=new ContentValues();

        v.put(AddNote, s_AddNote);
        v.put(TITLE,s_Title);
        v.put(Time, s_Time);
        v.put(Date, s_Date);
        v.put(Req , req);
        v.put(Status,status);



        long result = db.insert(table_name,null,v);
        return result !=-1; }



    public void update_Note(int id , String s_Title, String s_AddNote,
                            String s_Time,String s_Date,int req,int status){

        db =getWritableDatabase();
        ContentValues v=new ContentValues();

        v.put(TITLE, s_Title);
        v.put(AddNote, s_AddNote);
        v.put(Time,s_Time);
        v.put(Date,s_Date);
        v.put(Req,req);
        v.put(Status,status);


        String [] args = {id+" "};
        db.update(table_name,v,"id=?",args);

    }



    public boolean delete_Note(int id){

        db =getWritableDatabase();

        String [] args = {id+" "};

        int result = db.delete(table_name,"id=?",args);

        return result  > 0;
    }


    public ArrayList<List_model> get_all_note( ){

        ArrayList<List_model> p =new ArrayList<>();

        db=getReadableDatabase();

        Cursor c=db.rawQuery("SELECT * FROM "+table_name,null);

        if(c.moveToFirst()){
            do{
                int req = c.getInt(c.getColumnIndex(Req));
                int id = c.getInt(c.getColumnIndex(Id));
                int status = c.getInt(c.getColumnIndex(Status));
                String note =c.getString(c.getColumnIndex(AddNote));
                String title=c.getString(c.getColumnIndex(TITLE));
                String time=c.getString(c.getColumnIndex(Time));
                String s_Date=c.getString(c.getColumnIndex(Date));

                List_model l =new List_model(id,req,note,title,time,s_Date,status);
                p.add(l);
            }
            while (c.moveToNext());

            c.close();
        }

        return p;

    }


}
