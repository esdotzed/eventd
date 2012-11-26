package com.eventd.augment.activity;

import java.text.DecimalFormat;
import java.util.logging.Logger;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.eventd.augment.camera.CameraSurface;
import com.eventd.augment.data.ARData;


/**
 * This class extends the SensorsActivity and is designed tie the AugmentedView and zoom bar together.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class AugmentedReality extends SensorsActivity {
    private static final Logger logger = Logger.getLogger(AugmentedReality.class.getSimpleName());
    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");
    
    private static WakeLock wakeLock = null;
    private static CameraSurface camScreen = null;    
    private static SeekBar myZoomBar = null;
    private static FrameLayout frameLayout = null;
    private static AugmentedView augmentedView = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.info("onCreate()");

        camScreen = new CameraSurface(this);
        setContentView(camScreen);
        
        myZoomBar = new SeekBar(this);
        myZoomBar.setMax(100);
        myZoomBar.setProgress(25);
        myZoomBar.setOnSeekBarChangeListener(myZoomBarOnSeekBarChangeListener);

        frameLayout = new FrameLayout(this);
        frameLayout.setMinimumWidth(3000);
        frameLayout.addView(myZoomBar);
        frameLayout.setPadding(10, 0, 10, 10);
        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(  LayoutParams.FILL_PARENT, 
                                                                                    LayoutParams.WRAP_CONTENT, 
                                                                                    Gravity.BOTTOM);
        addContentView(frameLayout,frameLayoutParams);

        augmentedView = new AugmentedView(this);
        LayoutParams augLayout = new LayoutParams(  LayoutParams.WRAP_CONTENT, 
                                                    LayoutParams.WRAP_CONTENT);
        addContentView(augmentedView,augLayout);
        
        updateDataOnZoom();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	logger.info("onDestroy()");
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	logger.info("onStart()");
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	logger.info("onStop()");
    }

	@Override
	public void onResume() {
		super.onResume();

		wakeLock.acquire();
	}

	@Override
	public void onPause() {
		super.onPause();

		wakeLock.release();
	}
	
    @Override
    public void onSensorChanged(SensorEvent evt) {
        super.onSensorChanged(evt);

        if (    evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER || 
                evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
        ) {
            augmentedView.postInvalidate();
        }
    }
    
    private OnSeekBarChangeListener myZoomBarOnSeekBarChangeListener = new OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateDataOnZoom();
            camScreen.invalidate();
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            updateDataOnZoom();
            camScreen.invalidate();
        }
    };
    
    private static float calcZoomLevel(){
        int myZoomLevel = myZoomBar.getProgress();
        float myout = 5;
    
        if (myZoomLevel <= 26) {
            myout = myZoomLevel / 25f;
        } else if (25 < myZoomLevel && myZoomLevel < 50) {
            myout = (1 + (myZoomLevel - 25)) * 0.38f;
        } else if (25== myZoomLevel) {
            myout = 1;
        } else if (50== myZoomLevel) {
            myout = 10;
        } else if (50 < myZoomLevel && myZoomLevel < 75) {
            myout = (10 + (myZoomLevel - 50)) * 0.83f;
        } else {
            myout = (30 + (myZoomLevel - 75) * 2f);
        }

        return myout;
    }

    protected void updateDataOnZoom() {
        float zoomLevel = calcZoomLevel();
        ARData.setRadius(zoomLevel);
        ARData.setZoomLevel(FORMAT.format(zoomLevel));
        ARData.setZoomProgress(myZoomBar.getProgress());
    };
}
