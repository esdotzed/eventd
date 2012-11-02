package com.cs5150.augmentation;

import android.location.Location;

public class Destination {
	private Location Cur, Des;
	private float Compass;
	
	public Destination(){
		this.Cur = null;
		this.Des = null;
		this.Compass = 0;
	}
	
	public Destination(Location Cur, Location Des, float azimuth_angle){
		this.Cur = Cur;
		this.Des = Des;
		this.Compass = azimuth_angle;
	}
	
	private float getAngle(){
		double LongOffset = Math.toRadians(Des.getLongitude() - Cur.getLongitude());
		double LaOffset = Math.toRadians(Des.getLatitude() - Cur.getLatitude());
		double LaCur = Math.toRadians(Cur.getLatitude());
		double LaDes = Math.toRadians(Des.getLatitude());
		double Y = Math.sin(LongOffset) * Math.cos(LaDes);;
		double X = Math.cos(LaCur)*Math.sin(LaDes) - Math.sin(LaCur)*Math.cos(LaDes)*Math.cos(LongOffset);
		double angle = Math.toDegrees(Math.atan2(Y, X));
	    if(angle < 0) {
	        angle = 360 - Math.abs(angle);
	    }
		return (float)angle - Compass;
	}
	public boolean inRange(){
		return Math.abs(getAngle()) < 10;
	}
}
