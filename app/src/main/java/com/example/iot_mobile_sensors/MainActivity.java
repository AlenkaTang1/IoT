package com.example.iot_mobile_sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope, magnetometer, proximity, gameRotation, light;
    private Timer uploadTimer;
    private SimpleDateFormat dateFormat;
    private float[] accelerometerValues = new float[3];
    private float[] gyroscopeValues = new float[3];
    private float[] magneticfieldValues = new float[3];
    private float[] gameRotationValues = new float[3];
    private float proximityValues;
    private float lightValues;
    public FirebaseFirestore db;
    private TextView start;
    private TextView stop;
    private TextView collectionName;
    private EditText userInput;
    private String name;
    private boolean canCollect;

    public void collectionFolder(View v) {
        userInput = findViewById(R.id.user_input);
        collectionName = findViewById(R.id.collection_name);
        collectionName.setText(userInput.getText().toString() + " collection folders has been created");
        name = userInput.getText().toString();
    }

    public void startCollecting(View v) {
        canCollect = true;
        start = findViewById(R.id.start_text);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        start.setText("start collecting at: " + currentDateTime);
        Map<String, Object> start_data = new HashMap<>();
        start_data.put("start time", currentDateTime);
        db.collection("start&stop time").add(start_data);
        uploadTimer = new Timer();
        uploadTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                startupload_a(canCollect);
                startupload_g(canCollect);
            }
        }, 0, 1000);
    }

    public void stopCollecting(View v) {
        canCollect = false;
        stop = findViewById(R.id.stop_text);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        Map<String, Object> stop_data = new HashMap<>();
        stop.setText("collection stopped, please quit the app now");
        stop_data.put("stop time", currentDateTime);
        db.collection("start&stop time").add(stop_data);
    }

    public void startupload_a(boolean b) {

        if (b) {
            Log.d(TAG, "running upLoad a");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new Date());
            Map<String, Object> a_data = new HashMap<>();
            Map<String, Object> m_data = new HashMap<>();
            Map<String, Object> p_data = new HashMap<>();
            Map<String, Object> gr_data = new HashMap<>();
            Map<String, Object> l_data = new HashMap<>();

            a_data.put("time", currentTime);
            a_data.put("x", accelerometerValues[0]);
            a_data.put("y", accelerometerValues[1]);
            a_data.put("z", accelerometerValues[2]);


            m_data.put("time", currentTime);
            m_data.put("x", magneticfieldValues[0]);
            m_data.put("y", magneticfieldValues[1]);
            m_data.put("z", magneticfieldValues[2]);

            p_data.put("time", currentTime);
            p_data.put("distance", proximityValues);

            gr_data.put("time", currentTime);
            gr_data.put("x", gameRotationValues[0]);
            gr_data.put("y", gameRotationValues[1]);
            gr_data.put("z", gameRotationValues[2]);

            l_data.put("time", currentTime);
            l_data.put("light", lightValues);

            db.collection(name + "_accelerometerData")
                    .add(a_data)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "accelerometerData Upload successful");
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "accelerometerData Upload failed");
                    });

            db.collection(name + "_magnetometerData")
                    .add(m_data)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "magnetometerData Upload successful");
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "magnetometerData Upload failed");
                    });
            db.collection(name + "_proximityData")
                    .add(p_data)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "proximityData Upload successful");
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "proximityData Upload failed");
                    });
            db.collection(name + "_orientationData")
                    .add(gr_data)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "orientationData Upload successful");
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "orientationData Upload failed");
                    });
            db.collection(name + "_lightData")
                    .add(l_data)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "lightData Upload successful");
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "lightData Upload failed");
                    });
        } else {
            Log.d(TAG, "upload stopped");
        }
    }

    public void startupload_g(boolean b) {
        if (b) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new Date());
            Map<String, Object> g_data = new HashMap<>();
            g_data.put("time", currentTime);
            g_data.put("x", gyroscopeValues[0]);
            g_data.put("y", gyroscopeValues[1]);
            g_data.put("z", gyroscopeValues[2]);

            db.collection(name + "_gyroscopeData")
                    .add(g_data)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "gyroscopeData Upload successful");
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "gyroscopeData Upload failed");
                    });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Initializing Sensor Service and database");
        db = FirebaseFirestore.getInstance();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            Log.d(TAG, "onCreated, registered accelerometer successful");
            sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscope != null) {
            Log.d(TAG, "onCreated, registered gyroscope successful");
            sensorManager.registerListener(MainActivity.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }

        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magnetometer != null) {
            Log.d(TAG, "onCreated, registered magnetometer successful");
            sensorManager.registerListener(MainActivity.this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        proximity = sensorManager.getDefaultSensor((Sensor.TYPE_PROXIMITY));
        if (proximity != null) {
            Log.d(TAG, "onCreated, registered proximity sensor successful");
        }

        gameRotation = sensorManager.getDefaultSensor((Sensor.TYPE_GAME_ROTATION_VECTOR));
        if (gameRotation != null) {
            Log.d(TAG, "onCreated, registered gameRotation sensor successful");
        }

        light = sensorManager.getDefaultSensor((Sensor.TYPE_LIGHT));
        if (light != null) {
            Log.d(TAG, "onCreated, registered light sensor successful");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "onSensorChanged accelerometer: X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);
            accelerometerValues[0] = event.values[0];
            accelerometerValues[1] = event.values[1];
            accelerometerValues[2] = event.values[2];

        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroscopeValues[0] = event.values[0];
            gyroscopeValues[1] = event.values[1];
            gyroscopeValues[2] = event.values[2];
            Log.d(TAG, "onSensorChanged gyroscope: X: " + gyroscopeValues[0] + "Y: " + gyroscopeValues[1] + "Z: " + gyroscopeValues[2]);

        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Log.d(TAG, "onSensorChanged magnetic field: X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);
            magneticfieldValues[0] = event.values[0];
            magneticfieldValues[1] = event.values[1];
            magneticfieldValues[2] = event.values[2];
        } else if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
            Log.d(TAG, "onSensorChanged proximity value: " + event.values[0]);
            proximityValues = event.values[0];
        } else if (sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            Log.d(TAG, "onSensorChanged game rotation vector field: X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);
            gameRotationValues[0] = event.values[0];
            gameRotationValues[1] = event.values[1];
            gameRotationValues[2] = event.values[2];
        } else if (sensor.getType() == Sensor.TYPE_LIGHT) {
            Log.d(TAG, "onSensorChanged light value: " + event.values[0]);
            lightValues = event.values[0];
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}