package com.example.iat359_finalproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//For date time picking
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    EventDatabase db;
    String name,dateTime,location;
    TextView nameText,locationText,dateText,timeText;
    Button selectDate,selectTime,addEvent;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        //Get event database
        db=new EventDatabase(this);

        nameText=(TextView) findViewById(R.id.nameInput);
        dateText=(TextView) findViewById(R.id.dateInput);
        timeText=(TextView) findViewById(R.id.timeInput);
        locationText=(TextView) findViewById(R.id.locationInput);

        //Get buttons and set on click listeners
        selectDate=(Button) findViewById(R.id.selectDateButton);
        selectTime=(Button) findViewById(R.id.selectTimeButton);
        addEvent=(Button) findViewById(R.id.submitEvent);

        selectDate.setOnClickListener(this);
        selectTime.setOnClickListener(this);
        addEvent.setOnClickListener(this);

        //Set the default values to current date (without seconds)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            String formattedDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String formattedTime = currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            dateText.setText(formattedDate); // Sets the date
            timeText.setText(formattedTime); // Sets the time
        } else {
            //For older versions
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            dateText.setText(dateFormat.format(calendar.getTime())); // Sets the date
            timeText.setText(timeFormat.format(calendar.getTime())); // Sets the time
        }
    }

    @Override
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name",name);
        outState.putString("dateTime",dateTime);
        outState.putString("location",location);

        outState.putString("nameText",nameText.getText().toString());
        outState.putString("dateText",dateText.getText().toString());
        outState.putString("timeText",timeText.getText().toString());
        outState.putString("locationText",locationText.getText().toString());
    }
    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name=savedInstanceState.getString("name");
        dateTime=savedInstanceState.getString("dateTime");
        location=savedInstanceState.getString("location");

        nameText.setText(savedInstanceState.getString("nameText"));
        dateText.setText(savedInstanceState.getString("dateText"));
        timeText.setText(savedInstanceState.getString("timeText"));
        locationText.setText(savedInstanceState.getString("locationText"));

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==selectDate.getId()){
            //Show date dialogue
            openDateDialogue();

        } else if (view.getId()==selectTime.getId()) {
            //Show time dialogue
            openTimeDialogue();
        }
        else{
            //Add event to database
            dateTime=dateText.getText().toString()+" "+timeText.getText().toString();
            name=nameText.getText().toString();
            location=locationText.getText().toString();

            //If the name is not entered ignore and make a toast
            if(!name.isEmpty()){
                long id=db.insertEvent(name,dateTime,location);
                Intent resultIntent=new Intent();
                resultIntent.putExtra("DB_ID",id);
                setResult(RESULT_OK, resultIntent);
                finish();

            }
            else{
               Toast.makeText(this,"Name is needed to add event",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        // Adjust the month value and format the date
        month = month + 1; // month is 0-indexed, adjust to 1-indexed
        String formattedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month, dayOfMonth);
        dateText.setText(formattedDate); // Update the date TextView
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        // Format the time
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        timeText.setText(formattedTime); // Update the time TextView
    }

    private void openDateDialogue(){
        DatePickerDialog dialogue;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime currentTimeDate=LocalDateTime.now();
            dialogue = new DatePickerDialog(this,this,currentTimeDate.getYear(),currentTimeDate.getMonthValue()-1,currentTimeDate.getDayOfMonth());
        }
        else{
            //For older versions
            Calendar calendar= Calendar.getInstance();
            dialogue = new DatePickerDialog(this,this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        }
        dialogue.show();
    }
    private void openTimeDialogue(){
        TimePickerDialog dialogue;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime currentTimeDate=LocalDateTime.now();
            dialogue = new TimePickerDialog(this,this,currentTimeDate.getHour(),currentTimeDate.getMinute(),true);
        }
        else{
            //For older versions
            Calendar calendar= Calendar.getInstance();
            dialogue = new TimePickerDialog(this,this,calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),true);
        }
        dialogue.show();
    }

}
