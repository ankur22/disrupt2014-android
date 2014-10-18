package com.example.mytest.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class GPSCoordinates {
	private double longitude;
	private double latitude;
	
	public void setLongitude(double longitude) {
		this.longitude = longitude; 
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public JSONObject toJSON() {
		JSONObject locationJSONObject = new JSONObject();
		
		try {
			locationJSONObject.put("longitude", longitude);
			locationJSONObject.put("latitude", latitude);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.w("toJSON", "Exception when convertig GPSCoordinates into its JSON form.");
		}
		
		return locationJSONObject;
	}
}
