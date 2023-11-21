package com.example.iat359_finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private static final float MAX_LIGHT_VALUE = 255;
    private static final int MIN_BRIGHTNESS = 80;
    EventDatabase db;
    EventAdapter adapter;
    RecyclerView recyclerView;
    Button addEventButton;
    SensorManager senMan;
    Sensor lightSensor;
    static final int REQUEST_CODE=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView) findViewById(R.id.homeRecyclerView);
        addEventButton=(Button) findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(this);

        db=new EventDatabase(this);
        adapter=new EventAdapter(db.getEventList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        senMan=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor=senMan.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                this.startActivity(intent);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(lightSensor!=null)
            senMan.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        refreshEventList();
        Log.d("Database Size",Integer.toString(adapter.getItemCount()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        senMan.unregisterListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent addEventIntent=new Intent(this,AddEventActivity.class);
        startActivityForResult(addEventIntent,REQUEST_CODE);
    }
    private void refreshEventList() {
        EventDatabase db = new EventDatabase(this);
        List<Event> events = db.getEventList();

        if (adapter == null) {
            adapter = new EventAdapter(events);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setEventsList(events);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_CODE==requestCode){
            if(resultCode==RESULT_OK){

                Log.d("DB ID",Long.toString(data.getLongExtra("DB_ID",-1L)));
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            adjustScreenBrightness(sensorEvent.values[0]); // Adjust brightness based on light value
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void adjustScreenBrightness(float lightValue) {
        // Convert light sensor value to a screen brightness value
        // This conversion will depend on your specific requirements

        // Example: Adjusting brightness between 0 and 255
        int brightness = (int) (lightValue / MAX_LIGHT_VALUE * 255);
        brightness = Math.max(brightness, MIN_BRIGHTNESS); // Ensure a minimum brightness

        // Apply the brightness setting to the current window
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = brightness / 255f; // Value must be between 0 and 1
        getWindow().setAttributes(layoutParams);
    }
}