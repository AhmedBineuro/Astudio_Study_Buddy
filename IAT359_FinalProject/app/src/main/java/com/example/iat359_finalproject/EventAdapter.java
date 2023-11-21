package com.example.iat359_finalproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
    List<Event> eventsList;
    public EventAdapter(List<Event> eventList){
        this.eventsList=eventList;
    }
    @Override
    public EventViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_view,parent,false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event currentEvent = eventsList.get(position);
        holder.eventName.setText(currentEvent.name);
        holder.eventLocation.setText(currentEvent.location);
        // Parse the original string
        //Android recommended putting an SDK check around these
        LocalDateTime dateTime = null;
        String formattedDateTime;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.parse(currentEvent.dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            holder.calInviteDateTime=dateTime;
            formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy h:mm a", Locale.ENGLISH));
            holder.eventDateTime.setText(formattedDateTime);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            Date date = null;
            try {
                date = sdf.parse(currentEvent.dateTime);
            } catch (ParseException e) {
                holder.eventDateTime.setText("Date/time format error");
            }
            holder.db_ID= currentEvent.db_ID;
            sdf.applyPattern("EEEE, MMMM d, yyyy h:mm a");
            formattedDateTime = sdf.format(date);
        }
    }

    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
