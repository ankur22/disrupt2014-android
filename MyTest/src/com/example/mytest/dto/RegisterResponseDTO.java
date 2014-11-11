package com.example.mytest.dto;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RegisterResponseDTO implements ResponseDTO {
	String userId;
	
	public RegisterResponseDTO(JSONObject body) {
		parseResponse(body);
	}
	
	protected void parseResponse(JSONObject body) {
		if (body != null && body.has("userId")) {
			try {
				userId = body.getString("userId");
			} catch (JSONException e) {
				e.printStackTrace();
				Log.i("parseResponse","Exception while attempting to get userId from response");
				userId = "";
			}
		} else {
			Log.i("parseResponse","No eventId in response");
			userId = "";
		}
	}
	
	public String getUserId() {
		return userId;
	}
}
