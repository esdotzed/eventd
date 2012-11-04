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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity{

	private SensorManager mSensorManager;
	private TextView tvDirection, tvLocation, tvTestEvent;
    private Camera mCamera;
    private CameraPreview mPreview;
    private LocationManager locationManager;
    private Location curLocation = null;
    private float azimuth_angle;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mI = new float[9];
    private float[] mOrientation = new float[3];
    //testing.
    private Location desLocation = null;
    private RelativeLayout layout = null;
    private Button eventButton = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	    mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	    
	    tvDirection = (TextView)findViewById(R.id.direction);
	    tvLocation = (TextView)findViewById(R.id.location);
	    tvTestEvent = (TextView)findViewById(R.id.testEvent);
        // Create an instance of Camera
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        layout = (RelativeLayout) findViewById(R.id.relativeLayout);
        //testing using the longtitude and latitude of Upson Hall.
        desLocation = new Location ("test");
        desLocation.setLatitude(42.443892);
        desLocation.setLongitude(-76.482187);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
	        	RelativeLayout.LayoutParams.WRAP_CONTENT,
	    		RelativeLayout.LayoutParams.WRAP_CONTENT
	    );
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        eventButton = new Button(getBaseContext());
        eventButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// Perform action on click
        	}
        });
        eventButton.setText("Upson Test Event Ahead!!");
        layout.addView(eventButton, layoutParams);
        eventButton.setVisibility(View.INVISIBLE);
    }
	
	@Override
	protected void onResume() {
		super.onResume();
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLastAccelerometerSet = false;
        mLastMagnetometerSet = false;
        mSensorManager.registerListener(sensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(sensorListener, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        
        // Register the listener with the Location Manager to receive location updates
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,	locationListener);
			curLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			tvTestEvent.setText("Waiting for GPS Signal...");
		}

		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,	locationListener);
			curLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			tvTestEvent.setText("Waiting for Wifi Signal...");
		}
    }

	@Override
	protected void onPause() {
	    super.onPause();
	    mSensorManager.unregisterListener(sensorListener);
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
	    	tvTestEvent.setText("");
	    	Log.v("location","Current location: "+location.getLatitude()+", "+location.getLongitude());
	        tvLocation.setText("Current location: "+location.getLatitude()+", "+location.getLongitude());
	        curLocation = location;
	        //testing.
	        Destination destination  = new Destination(curLocation, desLocation, azimuth_angle);
	        eventButton.setVisibility(destination.inRange()?View.VISIBLE:View.INVISIBLE);
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {}
	    public void onProviderEnabled(String provider) {}
	    public void onProviderDisabled(String provider) {}
	};

	  
	  
	private SensorEventListener sensorListener = new SensorEventListener(){
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		public void onSensorChanged(SensorEvent event) {
	        if (event.sensor == mAccelerometer) {
	            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
	            mLastAccelerometerSet = true;
	        } else if (event.sensor == mMagnetometer) {
	            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
	            mLastMagnetometerSet = true;
	        }
	        if (mLastAccelerometerSet && mLastMagnetometerSet) {
	            SensorManager.getRotationMatrix(mR, mI, mLastAccelerometer, mLastMagnetometer);
	            SensorManager.getOrientation(mR, mOrientation);
	            Log.v("OrientationTestActivity", String.format("Orientation: %f, %f, %f",
	                                                           mOrientation[0], mOrientation[1], mOrientation[2]));
	            mOrientation[0] =(mOrientation[0] >= 0)?mOrientation[0]:(float)(Math.PI*2 + mOrientation[0]);
	            mOrientation[0] = (float)(mOrientation[0]/Math.PI*180);
	            azimuth_angle = mOrientation[0];
	            tvDirection.setText("Orientation: "+azimuth_angle);
	        }
	    }
	};

}