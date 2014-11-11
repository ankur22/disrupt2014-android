package com.example.mytest.dto;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.mytest.model.GPSCoordinates;

public class RegisterDTO {
	private String email;
	private String password;
	
	public RegisterDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();
		
		if (email != null) {
			try {
				jsonObject.put("email", email);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.w("toJSON", "Unable to convert email into its JSON form.");
			}
		}
		
		if (password != null) {
			try {
				jsonObject.put("password", password);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.w("toJSON", "Unable to convert password into its JSON form.");
			}
		}
		
		return jsonObject;
	}
}
