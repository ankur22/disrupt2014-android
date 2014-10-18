package com.example.mytest.dto;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.mytest.model.GPSCoordinates;

public class RequestDTO {
	private String userId;
	private GPSCoordinates location;
	private String eventId;
	
	public RequestDTO(String userId, GPSCoordinates location) {
		this.userId = userId;
		this.location = location;
		this.eventId = null;
	}
	
	public RequestDTO(String userId, GPSCoordinates location, String eventId) {
		this.userId = userId;
		this.location = location;
		this.eventId = eventId;
	}
	
	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("userId", userId);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.w("toJSON", "Unable to convert userId into its JSON form.");
		}
		
		try {
			jsonObject.put("location", location.toJSON());
		} catch (JSONException e) {
			e.printStackTrace();
			Log.w("toJSON", "Unable to convert location into its JSON form.");
		}
		
		if (eventId != null && eventId.length() > 0) {
			try {
				jsonObject.put("eventId", eventId);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.w("toJSON", "Unable to convert eventId into its JSON form.");
			}
		}
		
		return jsonObject;
	}
}
