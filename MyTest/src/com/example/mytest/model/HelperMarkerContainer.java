package com.example.mytest.model;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.model.Marker;

public class HelperMarkerContainer {
	private Map<String, HelperMarker> helperMarkers;
	
	public HelperMarkerContainer() {
		this.helperMarkers = new HashMap<String, HelperMarker>();
	}
	
	public void addHelperMarker(Helper helper, Marker marker) {
		if (!helperMarkers.containsKey(helper.getId())) {
			helperMarkers.put(helper.getId(), new HelperMarker(helper, marker));
		}
	}
	
	public int getLength() {
		return helperMarkers.size();
	}
	
	public HelperMarker getHelperMarkerWithId(String id) {
		return helperMarkers.get(id);
	}
}
