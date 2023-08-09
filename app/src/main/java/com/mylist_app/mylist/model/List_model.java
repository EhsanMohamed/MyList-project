package com.mylist_app.mylist.model;

public class List_model {


    private int status;
    private int id , req;
    private String addNote;
    private String title;
    private String time_p;
    private String date_p;

    public List_model(){}

    public List_model(int id, int req,String addNote, String title,String time,String date,int status) {
        this.id = id;
        this.req=req;
        this.addNote = addNote;
        this.title=title;
        this.time_p =time;
        this.date_p=date;
        this.status=status;
    }




    public int getId() {
        return id;
    }

    public int getReq() {
        return req;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getAddNote() {
        return addNote;
    }

    public String getTime_p() {
        return time_p;
    }

    public String getDate_p() {
        return date_p;
    }





}
