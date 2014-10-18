package com.example.mytest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

import com.example.mytest.R;
import com.example.mytest.model.GPSCoordinates;


public class MapActivity extends Activity {
	MapView mMapView;
	
	String userId;
	GPSCoordinates location;
	String eventId;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// Retrieve the map and initial extent from XML layout
		mMapView = (MapView)findViewById(R.id.map);
		// Add dynamic layer to MapView
		mMapView.addLayer(new ArcGISTiledMapServiceLayer("" +
		"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));

//		Intent intent = getIntent();
//		this.userId = intent.getStringExtra(getString(R.string.login_to_main_extra_id));
//		location = new GPSCoordinates();
	}

	protected void onPause() {
		super.onPause();
		mMapView.pause();
	}

	protected void onResume() {
		super.onResume();
		mMapView.unpause();
	}
}