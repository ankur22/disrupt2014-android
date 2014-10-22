package com.example.mytest.model;

import com.google.android.gms.maps.model.Marker;

public class HelperMarker {
	private Helper helper;
	private Marker marker;
	
	public HelperMarker(final Helper helper, final Marker marker) {
		this.helper = helper;
		this.marker = marker;
	}
	
	public Helper getHelper() {
		return helper;
	}
	
	public Marker getMarker() {
		return marker;
	}
}
