package com.example.iat359_finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.widget.Toast;

public class EventHelperClass extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "eventDatabase";
    public static final String TABLE_NAME = "eventTable";
    public static final String UID = "_id";
    public static final String NAME = "Name";
    public static final String DATETIME = "Datetime";
    public static final String LOCATION = "Location";
    public static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                    " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME + " TEXT NOT NULL, " +
                    DATETIME + " TEXT NOT NULL, " +
                    LOCATION + " TEXT NOT NULL);";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public EventHelperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (SQLException e) {
            Toast.makeText(context, "Exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            Toast.makeText(context, "Exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}
