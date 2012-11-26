package com.eventd.augment.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import android.location.Location;
import android.os.Bundle;

import com.eventd.augment.data.ARData;
import com.eventd.augment.data.BuzzDataSource;
import com.eventd.augment.data.DataSource;
import com.eventd.augment.data.TwitterDataSource;
import com.eventd.augment.data.WikipediaDataSource;
import com.eventd.augment.ui.Marker;

/**
 * This class extends the AugmentedReality and is designed to be an example on how to extends the AugmentedReality
 * class to show multiple data sources.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class Demo extends AugmentedReality {
	private static final Logger logger = Logger.getLogger(Demo.class.getSimpleName());
	private static final String locale = Locale.getDefault().getLanguage();
	
	private static Collection<DataSource> sources = null;    
    private static Thread thread = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (sources==null) {
        	sources = new ArrayList<DataSource>();
            
            DataSource twitter = new TwitterDataSource(this.getResources());
            sources.add(twitter);
            DataSource wikipedia = new WikipediaDataSource();
            //sources.add(wikipedia);
            DataSource buzz = new BuzzDataSource(this.getResources());
            //sources.add(buzz);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        
        Location last = ARData.getCurrentLocation();
        if (last!=null) updateData(last.getLatitude(),last.getLongitude(),last.getAltitude());
    }
    
    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        
        updateData(location.getLatitude(),location.getLongitude(),location.getAltitude());
    }

    private void updateData(final double lat, final double lon, final double alt) {
    	if (thread!=null && thread.isAlive()) {
    		logger.info("Not updating since in the process");
    		return;
    	}
    	
    	thread = new Thread(
    		new Runnable(){
				
				public void run() {
					logger.info("Start");
					for (DataSource source : sources) {
						download(source, lat, lon, alt);
					}
					logger.info("Stop");
				}
			}
    	);
    	thread.start();
    }
    
    private static boolean download(DataSource source, double lat, double lon, double alt) {
		if (source==null) return false;
		
		String url = source.createRequestURL(lat, lon, alt, ARData.getRadius(), locale);    	
    	logger.info(url);
    	if (url==null) return false;
    	
    	List<Marker> markers = source.parse(url);
    	if (markers==null) return false;
    	
    	logger.info(source.getClass().getSimpleName()+" size="+markers.size());
    	
    	ARData.addMarkers(markers);
    	return true;
    }
}
