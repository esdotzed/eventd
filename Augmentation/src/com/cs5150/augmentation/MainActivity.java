package com.cs5150.augmentation;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	
	private static final Context Context = null;
	private SensorManager mSensorManager;
	private Sensor mOrientation;
	private TextView tvDirection;
    private Camera mCamera;
    private CameraPreview mPreview;
    
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    tvDirection = (TextView)findViewById(R.id.direction);
        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }
	
	  public void onAccuracyChanged(Sensor sensor, int accuracy) {
		    // Do something here if sensor accuracy changes.
		    // You must implement this callback in your code.
		  }

		  @Override
		  protected void onResume() {
		    super.onResume();
		    mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
		  }

		  @Override
		  protected void onPause() {
		    super.onPause();
		    mSensorManager.unregisterListener(this);
		  }


		  public void onSensorChanged(SensorEvent event) {
		    float azimuth_angle = event.values[0];
		    float pitch_angle = event.values[1];
		    float roll_angle = event.values[2];
		    try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    // Do something with these orientation angles.
		    Log.v ("azimuth_angle", "" + azimuth_angle);
		    Log.v ("pitch_angle","" + pitch_angle);
		    Log.v ("roll_angle",""+roll_angle);
		    tvDirection.setText(""+azimuth_angle);
		  }
	
	   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	   
	   
	public static Camera getCameraInstance(){
		Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	   
	   
	   
	   
	   
}