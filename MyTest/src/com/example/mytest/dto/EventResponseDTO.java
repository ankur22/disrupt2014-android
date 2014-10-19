package com.example.mytest.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mytest.model.GPSCoordinates;

public class EventResponseDTO extends ResponseDTO {
	
	List<GPSCoordinates> helperLocations;
	GPSCoordinates helpeeLocation;

	public EventResponseDTO(JSONObject jsonObject) {
		super(jsonObject);
	}
	
	@Override
	protected void parseResponse(JSONObject body) {
		try {
			helpeeLocation = new GPSCoordinates();
			JSONObject locationObject = body.getJSONObject("helpRequest").getJSONObject("location");
			helpeeLocation.setLatitude(locationObject.getDouble("latitude"));
			helpeeLocation.setLongitude(locationObject.getDouble("longitude"));
			
			helperLocations = new ArrayList<GPSCoordinates>();
			JSONArray array = body.getJSONArray("helpers");
			for (int i = 0; i < array.length(); ++i) {
				JSONObject object = ((JSONObject)array.get(i)).getJSONObject("location");
				GPSCoordinates coord = new GPSCoordinates();
				coord.setLatitude(object.getDouble("latitude"));
				coord.setLongitude(object.getDouble("longitude"));
				helperLocations.add(coord);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public GPSCoordinates getHelpeeCoords() {
		return helpeeLocation;
	}
	
	public List<GPSCoordinates> getHelpers() {
		return helperLocations;
	}
}
