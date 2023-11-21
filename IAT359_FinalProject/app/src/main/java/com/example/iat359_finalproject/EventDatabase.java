package com.example.iat359_finalproject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class EventDatabase {
    private Context context;
    private final EventHelperClass helper;

    public EventDatabase(Context c) {
        context = c;
        helper = new EventHelperClass(context);
    }

    public long insertEvent(String name, String datetime, String location) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventHelperClass.NAME, name);
        contentValues.put(EventHelperClass.DATETIME, datetime);
        contentValues.put(EventHelperClass.LOCATION, location);
        long id = db.insert(EventHelperClass.TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }
    public List<Event> getEventList(){
        List<Event> eventlist=new ArrayList<>();
        SQLiteDatabase db=helper.getReadableDatabase();
        //To get the events from soonest to latest
        String selectAllQuery="SELECT * FROM "+helper.TABLE_NAME+" ORDER BY "+helper.DATETIME+" ASC";
        Cursor cursor=db.rawQuery(selectAllQuery,null);

        if(cursor.moveToFirst()){
            do{
                int nameIndex=cursor.getColumnIndex(helper.NAME);
                int dateIndex=cursor.getColumnIndex(helper.DATETIME);
                int locationIndex=cursor.getColumnIndex(helper.LOCATION);
                int idIndex=cursor.getColumnIndex(helper.UID);
                String name = cursor.getString(nameIndex);
                String dateTime = cursor.getString(dateIndex);
                String location = cursor.getString(locationIndex);
                long id=cursor.getLong(idIndex);
                Event event=new Event(name,dateTime,location);
                event.db_ID=id;
                eventlist.add(event);

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventlist;
    }
    public void deleteEvent(long eventId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(helper.TABLE_NAME, helper.UID + " = ?", new String[] { String.valueOf(eventId) });
        db.close();
    }

}
