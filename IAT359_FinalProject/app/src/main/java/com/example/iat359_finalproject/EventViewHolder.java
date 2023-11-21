package com.example.iat359_finalproject;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

public class EventViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
    TextView eventName, eventDateTime, eventLocation;
    LocalDateTime calInviteDateTime;
    ImageButton addToCalendarButton;
    Context context;
    long db_ID;
    public EventViewHolder(View itemView) {
        super(itemView);
        eventName=itemView.findViewById(R.id.eventNameField);
        eventDateTime=itemView.findViewById(R.id.eventDateTimeField);
        eventLocation=itemView.findViewById(R.id.eventLocation);
        addToCalendarButton=(ImageButton) itemView.findViewById(R.id.addToCalendarButton);
        addToCalendarButton.setOnClickListener(this);
        context=itemView.getContext();

    }

    @Override
    public void onClick(View view) {
        Log.d("Share","On click activated");
        addEventToCalendar();
    }

    //Function to launch an implicit intent to add the event to a calendar app
    private void addEventToCalendar() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, eventName.getText().toString());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocation.getText().toString());

        // Convert LocalDateTime to milliseconds since epoch
        long startTime = convertToMillis(calInviteDateTime);
        long endTime = startTime + (60 * 60 * 1000); // For example, 1 hour later

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);

        if (intent.resolveActivity(itemView.getContext().getPackageManager()) != null) {
            itemView.getContext().startActivity(intent);
        }
    }

    // Convert the date time to milleseconds since epoc as this is what the calendar apps use for timing
    private long convertToMillis(LocalDateTime localDateTime) {
        long millis = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
            millis = zonedDateTime.toInstant().toEpochMilli();
        } else {
            // For older versions, use Calendar and SimpleDateFormat
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Date date = sdf.parse(localDateTime.toString());
                if (date != null) {
                    millis = date.getTime();
                }
            } catch (ParseException e) {
                Toast.makeText(context, "Failed to parse datetime for calendar invite",Toast.LENGTH_SHORT);
            }
        }
        return millis;
    }


}
