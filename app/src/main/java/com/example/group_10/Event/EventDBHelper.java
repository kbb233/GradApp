package com.example.group_10.Event;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.group_10.Announcement.AnnouncementDBHelper;

public class EventDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "event.db";
    private static final int DATABASE_VERSION = 1;
    public static final String CREATE_EVENT = "Create table event("
            +"id integer primary key autoincrement,"
            +"title text," +"content text,"
            +"year integer," +"month integer," +"day integer," +"hour integer," +"minute integer)";


    public EventDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(AnnouncementDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(sqLiteDatabase);
    }

}

