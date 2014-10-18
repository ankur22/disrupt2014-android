package com.example.mytest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.example.mytest.R;
import com.example.mytest.controller.Alarm;
import com.example.mytest.controller.AlarmListener;
import com.example.mytest.dao.HelpYouDAO;
import com.example.mytest.dto.RequestDTO;
import com.example.mytest.dto.ResponseDTO;
import com.example.mytest.dto.ResponseDTOListener;
import com.example.mytest.model.GPSCoordinates;


public class MapActivity extends Activity implements LocationListener, AlarmListener {
	MapView mMapView;
	HelpYouDAO helpYouDAO;
	LocationManager locationManager;
	
	String userId;
	GPSCoordinates location;
	String eventId;
	
	Alarm alarm;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setupLocationManager();

		// Retrieve the map and initial extent from XML layout
		mMapView = (MapView)findViewById(R.id.map);
		// Add dynamic layer to MapView
		mMapView.addLayer(new ArcGISTiledMapServiceLayer("" +
		"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));

		helpYouDAO = new HelpYouDAO(getApplicationContext());
		
		Intent intent = getIntent();
		this.userId = intent.getStringExtra(getString(R.string.extra_user_id));
		this.eventId = intent.getStringExtra(getString(R.string.extra_event_id));
		location = new GPSCoordinates();
		
		sendWillHelpYouRequest();
		
		alarm = new Alarm(getApplicationContext(), this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.unpause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearPollingUpdates();
	}
	
	private void setupLocationManager() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				7000, // 7 secs
				10, this);
	}
	
	private void clearPollingUpdates() {
		alarm.clearAlarm();
		locationManager.removeUpdates(this);
	}
	
	private void sendWillHelpYouRequest() {
		RequestDTO requestBody = new RequestDTO(userId, location, eventId);
		
		helpYouDAO.sendWillHelpYouRequest(requestBody, new ResponseDTOListener() {
			
			@Override
			public void successResponseRecieved(ResponseDTO responseDTO) {
				eventId = responseDTO.getEventId();
			}
			
			@Override
			public void errorResponseRecieved(int status, String message) {
				Log.w("sendHelpMeRequest", "Error response recieved while trying to agree to help you: " + message);
			}
		});
	}
	
	private void sendWillHelpYouUpdateRequest() {
		RequestDTO requestBody = new RequestDTO(userId, location, eventId);
		
		helpYouDAO.sendHelpingYouUpdateRequest(requestBody, new ResponseDTOListener() {
			
			@Override
			public void successResponseRecieved(ResponseDTO responseDTO) {
				eventId = responseDTO.getEventId();
			}
			
			@Override
			public void errorResponseRecieved(int status, String message) {
				Log.w("sendHelpMeRequest", "Error response recieved while trying to update help you: " + message);
			}
		});
	}
	
	@Override
	public void onLocationChanged(Location location) {
		this.location.setLatitude(location.getLatitude());
		this.location.setLongitude(location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.i("onProviderDisabled", "User disabled GPS");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.i("onProviderEnabled", "User enabled GPS");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i("onStatusChanged", "Status change of location manager");
	}

	@Override
	public void alarm() {
		runOnUiThread(new Runnable() {
	        public void run() {
	        	sendWillHelpYouUpdateRequest();
	        }
		});
	}
}