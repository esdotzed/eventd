package com.eventd.augment.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.eventd.R;
import com.eventd.augment.ui.IconMarker;
import com.eventd.augment.ui.Marker;

/**
 * This class extends DataSource to fetch data from Google Buzz.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class BuzzDataSource extends DataSource {
	private static final Logger logger = Logger.getLogger(BuzzDataSource.class.getSimpleName());
	private static final String BASE_URL = "https://www.googleapis.com/buzz/v1/activities/search?alt=json&max-results=40";

	private static Bitmap icon = null;
	
	public BuzzDataSource(Resources res) {
		if (res==null) return;
		
		createIcon(res);
	}
	
	protected void createIcon(Resources res) {
		if (res==null) return;
		
		icon=BitmapFactory.decodeResource(res, R.drawable.buzz);
	}
	
	public Bitmap getBitmap() {
		return icon;
	}
	
	public String createRequestURL(double lat, double lon, double alt, float radius, String locale) {
		return BASE_URL+
		"&lat=" + lat+
        "&lon=" + lon +
        "&radius="+ radius*1000;
	}
	
	public List<Marker> parse(JSONObject root) {
		if (root==null) return null;
		
		JSONObject jo = null;
		JSONArray dataArray = null;
    	List<Marker> markers=new ArrayList<Marker>();

		try {
			if(root.has("data") && root.getJSONObject("data").has("items")) dataArray = root.getJSONObject("data").getJSONArray("items");
			if (dataArray == null) return markers;
				int top = Math.min(MAX, dataArray.length());
				for (int i = 0; i < top; i++) {					
					jo = dataArray.getJSONObject(i);
					Marker ma = processJSONObject(jo);
					if(ma!=null) markers.add(ma);
				}
		} catch (JSONException e) {
			logger.info("Exception: "+e.getMessage());
		}
		return markers;
	}
	
	public Marker processJSONObject(JSONObject jo) {
		if (jo==null) return null;
		
        Marker ma = null;
        if (	jo.has("title") && 
        		jo.has("geocode") && 
        		jo.has("links")
        ) {
        	try {
        		ma = new IconMarker(
        				jo.getString("title"),
        				Double.valueOf(jo.getString("geocode").split(" ")[0]),
        				Double.valueOf(jo.getString("geocode").split(" ")[1]),
        				0,
        				Color.GREEN,
        				icon);
        	} catch (Exception e) {
        		logger.info("Exception: "+e.getMessage());
        	}
        }
        return ma;
	}
}
