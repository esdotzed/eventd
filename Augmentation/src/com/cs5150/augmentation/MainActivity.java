package com.cs5150.augmentation;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity{

	private SensorManager mSensorManager;
	private Sensor mOrientation;
	private TextView tvDirection;
	private TextView tvLocation;
	private TextView tvTestEvent;
    private Camera mCamera;
    private CameraPreview mPreview;
    private LocationManager locationManager;
    private Location curLocation = null;
    private float azimuth_angle;
    //testing.
    private Location desLocation = null;
    
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    tvDirection = (TextView)findViewById(R.id.direction);
	    tvLocation = (TextView)findViewById(R.id.location);
	    tvTestEvent = (TextView)findViewById(R.id.testEvent);
        // Create an instance of Camera
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //testing using the longtitude and latitude of Upson Hall.
        desLocation = new Location ("test");
        desLocation.setLatitude(42.443892);
        desLocation.setLongitude(-76.482187);
    }
	


		  @Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(sensorListener, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

		  @Override
	protected void onPause() {
	    super.onPause();
	    mSensorManager.unregisterListener(sensorListener);
	    // Remove the listener previously added
	    locationManager.removeUpdates(locationListener);
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
	   
	// Define a listener that responds to location updates
	private LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	      // Called when a new location is found by the network location provider.
	      //makeUseOfNewLocation(location);
	      Log.v("location","Current location: "+location.getLatitude()+", "+location.getLongitude());
	      tvLocation.setText("Current location: "+location.getLatitude()+", "+location.getLongitude());
	      curLocation = location;
	      //testing.
	      Destination destination  = new Destination(curLocation, desLocation, azimuth_angle);
	      tvTestEvent.setText(destination.inRange()? "Upson Test Event Ahead!!" : "");
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    public void onProviderEnabled(String provider) {}

	    public void onProviderDisabled(String provider) {}
	  };

	  
	  
	private SensorEventListener sensorListener = new SensorEventListener(){
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		    // Do something here if sensor accuracy changes.
		    // You must implement this callback in your code.
		}

		public void onSensorChanged(SensorEvent event) {
		    azimuth_angle = event.values[0];
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
			tvDirection.setText("Direction: "+azimuth_angle);
		}
	};

}