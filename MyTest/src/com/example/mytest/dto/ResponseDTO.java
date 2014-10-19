package com.example.mytest.dto;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ResponseDTO {
	String eventId;
	
	public ResponseDTO(JSONObject body) {
		parseResponse(body);
	}
	
	protected void parseResponse(JSONObject body) {
		if (body != null && body.has("eventId")) {
			try {
				eventId = body.getString("eventId");
			} catch (JSONException e) {
				e.printStackTrace();
				Log.i("parseResponse","Exception while attempting to get eventId from response");
				eventId = "";
			}
		} else {
			Log.i("parseResponse","No eventId in response");
			eventId = "";
		}
	}
	
	public String getEventId() {
		return eventId;
	}
}
