package com.example.mytest.model;

public class Helper {
	private GPSCoordinates location;
	private String id;
	
	public Helper(GPSCoordinates location, String id) {
		this.location = location;
		this.id = id;
	}
	
	public GPSCoordinates getLocation() {
		return location;
	}
	
	public String getId() {
		return id;
	}
}
