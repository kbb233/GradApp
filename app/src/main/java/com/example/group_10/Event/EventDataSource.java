package com.example.group_10.Event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

public class EventDataSource {
    private SQLiteDatabase database;
    private EventDBHelper dbHelper;

    public EventDataSource(Context context) {
        dbHelper = new EventDBHelper(context);
    }

    public void open() throws SQLException {database = dbHelper.getWritableDatabase();}
    public void close() {
        dbHelper.close();
    }

    public boolean insertEvent(Event_Unit c) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("title", c.getTitle());
            initialValues.put("content", c.getContent());
            initialValues.put("year",c.getYear());
            initialValues.put("month",c.getMonth());
            initialValues.put("day",c.getDay());
            initialValues.put("hour",c.getHour());
            initialValues.put("minute",c.getMinute());

            didSucceed = database.insert("event", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateEvent(Event_Unit c) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) c.getEventId();
            ContentValues updateValues = new ContentValues();

            updateValues.put("title", c.getTitle());
            updateValues.put("content", c.getContent());
            updateValues.put("year",c.getYear());
            updateValues.put("month",c.getMonth());
            updateValues.put("day",c.getDay());
            updateValues.put("hour",c.getHour());
            updateValues.put("minute",c.getMinute());

            didSucceed = database.update("event", updateValues, "id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastEventId() {
        int lastId;
        try {
            String query = "Select MAX(id) from event";
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

    public ArrayList<Event_Unit> getEvents(String sortField, String sortOrder) {
        ArrayList<Event_Unit> events = new ArrayList<Event_Unit>();
        try {
            String query = "SELECT  * FROM event ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Event_Unit newEvent;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newEvent = new Event_Unit();
                newEvent.setEventId(cursor.getInt(0));
                newEvent.setTitle(cursor.getString(1));
                newEvent.setContent(cursor.getString(2));
                newEvent.setYear(cursor.getInt(3));
                newEvent.setMonth(cursor.getInt(4));
                newEvent.setDay(cursor.getInt(5));
                newEvent.setHour(cursor.getInt(6));
                newEvent.setMinute(cursor.getInt(7));

                events.add(newEvent);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            events = new ArrayList<Event_Unit>();
        }
        return events;
    }

    public boolean deleteEvent(int eventId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("event", "id=" + eventId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

    public Event_Unit getSpecificEvent(int eventId) {
        Event_Unit specificEvent = new Event_Unit();
        String query = "SELECT * FROM event WHERE id="+eventId;
        Cursor cursor = database.rawQuery(query,null);

        if(cursor.moveToFirst()){
            specificEvent.setEventId(cursor.getInt(0));
            specificEvent.setTitle(cursor.getString(1));
            specificEvent.setContent(cursor.getString(2));
            specificEvent.setYear(cursor.getInt(3));
            specificEvent.setMonth(cursor.getInt(4));
            specificEvent.setDay(cursor.getInt(5));
            specificEvent.setHour(cursor.getInt(6));
            specificEvent.setMinute(cursor.getInt(7));

            cursor.close();
        }
        return specificEvent;
    }
}
