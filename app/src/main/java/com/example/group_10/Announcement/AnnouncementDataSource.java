package com.example.group_10.Announcement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AnnouncementDataSource {
    private SQLiteDatabase database;
    private AnnouncementDBHelper dbHelper;

    public AnnouncementDataSource(Context context){dbHelper = new AnnouncementDBHelper(context);}

    public void open() throws SQLException{database = dbHelper.getWritableDatabase();}
    public void close(){dbHelper.close();}

    public boolean insertAnnouncement(Announcement_Unit announce) {
        boolean didSucceed = false;
        try{
            ContentValues value = new ContentValues();
            value.put("title",announce.getTitle());
            value.put("content",announce.getContent());
            value.put("year",announce.getYear());
            value.put("month",announce.getMonth());
            value.put("day",announce.getDay());
            value.put("hour",announce.getHour());
            value.put("minute",announce.getMinute());
            value.put("second",announce.getSecond());
            value.put("time",announce.getTime());

            didSucceed = database.insert("announcement",null,value)>0;
        }
        catch (Exception e){

        }
        return didSucceed;
    }

    public boolean updateAnnouncement(Announcement_Unit announce) {
        boolean didSucceed = false;
        try{
            Long rowId = (long) announce.getAnnounceId();

            ContentValues value = new ContentValues();
            value.put("title",announce.getTitle());
            value.put("content",announce.getContent());
            value.put("year",announce.getYear());
            value.put("month",announce.getMonth());
            value.put("day",announce.getDay());
            value.put("hour",announce.getHour());
            value.put("minute",announce.getMinute());
            value.put("second",announce.getSecond());
            value.put("time",announce.getTime());

            didSucceed = database.update("announcement",value,"id="+rowId,null)>0;
        }
        catch (Exception e){

        }
        return didSucceed;
    }
    public int getLastAnnouncementId() {
        int lastId;
        try {
            String query = "Select MAX(id) from announcement";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public Announcement_Unit getSpecificAnnouncement(int id) {
        Announcement_Unit ann = new Announcement_Unit();
        String query = "SELECT * FROM announcement WHERE id="+id;
        Cursor cursor = database.rawQuery(query,null);

        if(cursor.moveToFirst()){
            ann.setAnnounceId(cursor.getInt(0));
            ann.setTitle(cursor.getString(1));
            ann.setContent(cursor.getString(2));
            ann.setYear(cursor.getInt(3));
            ann.setMonth(cursor.getInt(4));
            ann.setDay(cursor.getInt(5));
            ann.setHour(cursor.getInt(6));
            ann.setMinute(cursor.getInt(7));
            ann.setSecond(cursor.getInt(8));
            ann.setTime(cursor.getInt(9));
            cursor.close();
        }
        return ann;
    }

    public ArrayList<Announcement_Unit> getAnnouncements(String field,String order){
        ArrayList<Announcement_Unit> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM announcement ORDER BY "+ field+" "+order;
            Cursor cursor = database.rawQuery(query,null);
            Announcement_Unit ann;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                ann = new Announcement_Unit();
                ann.setAnnounceId(cursor.getInt(0));
                ann.setTitle(cursor.getString(1));
                ann.setContent(cursor.getString(2));
                ann.setYear(cursor.getInt(3));
                ann.setMonth(cursor.getInt(4));
                ann.setDay(cursor.getInt(5));
                ann.setHour(cursor.getInt(6));
                ann.setMinute(cursor.getInt(7));
                ann.setSecond(cursor.getInt(8));
                ann.setTime(cursor.getInt(9));
                list.add(ann);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e){
            list = new ArrayList<>();
        }
        return list;
    }

    public boolean deleteAnnouncement(int announceId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("announcement", "id=" + announceId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

}
