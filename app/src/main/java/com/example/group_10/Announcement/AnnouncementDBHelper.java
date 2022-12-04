package com.example.group_10.Announcement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AnnouncementDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "announcement.db";
    private static final int DATABASE_VERSION = 1;
    public static final String CREATE_ANNOUNCEMENT = "Create table announcement("
            +"id integer primary key autoincrement,"
            +"title text," +"content text,"
            +"year integer," +"month integer," +"day integer," +"hour integer," +"minute integer," +"second integer,"
            +"time long)";


    public AnnouncementDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ANNOUNCEMENT);
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
