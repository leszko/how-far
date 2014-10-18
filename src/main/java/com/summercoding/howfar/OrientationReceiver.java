package com.summercoding.howfar;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;


public class OrientationReceiver implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor sensorAccelerometer;
    private final Sensor sensorMagneticField;

    private float[] valuesAccelerometer = new float[3];
    private float[] valuesMagneticField = new float[3];
    private float[] matrixValues = new float[3];
    private float[] matrixR = new float[9];
    private float[] matrixI = new float[9];

    private DirectionListener directionListener;

    private static final int SENSOR_RATE = 1000;

    public OrientationReceiver(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start() {
        if (hasOrientationCapabilities()) {
            sensorManager.registerListener(this,
                    sensorAccelerometer,
                    SENSOR_RATE);
            sensorManager.registerListener(this,
                    sensorMagneticField,
                    SENSOR_RATE);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this,
                sensorAccelerometer);
        sensorManager.unregisterListener(this,
                sensorMagneticField);
    }

    public boolean hasOrientationCapabilities() {
        List<Sensor> accelerometers = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        List<Sensor> magneticFieldSensors = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);

        return !(accelerometers.isEmpty() || magneticFieldSensors.isEmpty());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, valuesAccelerometer, 0, 3);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, valuesMagneticField, 0, 3);
        }


        boolean success = SensorManager.getRotationMatrix(
                matrixR,
                matrixI,
                valuesAccelerometer,
                valuesMagneticField);

        if (success) {
            SensorManager.getOrientation(matrixR, matrixValues);
            double azimuth = Math.toDegrees(matrixValues[0]);
            notifyListeners(azimuth);
        } else {
            Log.d(((Object)this).getClass().getName(),"Fetching orientation failed");
        }
    }

    public void notifyListeners(double azimuth) {
        if (directionListener != null) {
            directionListener.onDirectionChanged(azimuth);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // empty
    }

    public DirectionListener getDirectionListener() {
        return directionListener;
    }

    public void setDirectionListener(DirectionListener directionListener) {
        this.directionListener = directionListener;
    }
}
