package com.example.mytest.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.mytest.R;
import com.example.mytest.controller.Alarm;
import com.example.mytest.controller.AlarmListener;
import com.example.mytest.dao.HelpYouDAO;
import com.example.mytest.dto.EventResponseDTO;
import com.example.mytest.dto.RequestDTO;
import com.example.mytest.dto.ResponseDTO;
import com.example.mytest.dto.ResponseDTOListener;
import com.example.mytest.model.GPSCoordinates;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends FragmentActivity implements LocationListener, AlarmListener {
	HelpYouDAO helpYouDAO;
	LocationManager locationManager;
	GoogleMap map;
	
	String userId;
	GPSCoordinates location;
	String eventId;
	EventResponseDTO eventResponseDTO;
	boolean zoomed = false;
	
	Marker helpee;
	
	Alarm alarm;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setupLocationManager();
		
		helpYouDAO = new HelpYouDAO(getApplicationContext());
		
		Intent intent = getIntent();
		this.userId = intent.getStringExtra(getString(R.string.extra_user_id));
		this.eventId = intent.getStringExtra(getString(R.string.extra_event_id));
		location = new GPSCoordinates();
		
		alarm = new Alarm(getApplicationContext(), this);
		
		setupMap();
		
		sendWillHelpYouRequest();
	}
	
	private void setupMap() {
		SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
		map = fm.getMap(); 
		map.setMyLocationEnabled(true);
	}
	
	private void zoomToMyLocation(GPSCoordinates myCoords) {
		if (!zoomed) {
			zoomed = true;
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myCoords.getLatitude(), myCoords.getLongitude()), 16));
		}

//        // You can customize the marker image using images bundled with
//        // your app, or dynamically generated bitmaps. 
//        map.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.house_flag))
//                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
//                .position(new LatLng(41.889, -87.622)));
	}
	
	private Marker updateMarkerPosition(Marker marker, GPSCoordinates location) {
		if (marker == null) {
			marker = map.addMarker(new MarkerOptions()
	          .icon(BitmapDescriptorFactory.fromResource(R.drawable.you_marker))
	          .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
	          .position(new LatLng(location.getLatitude(), location.getLongitude())));
		} else {
			marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
		}
		
		return marker;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearPollingUpdates();
	}
	
	private void setupLocationManager() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				10000, // 10 secs
				10, this);
	}
	
	private void clearPollingUpdates() {
		if (alarm != null) {
			alarm.clearAlarm();
		}
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
	}
	
	private void sendWillHelpYouRequest() {
		RequestDTO requestBody = new RequestDTO(userId, location, eventId);
		
		helpYouDAO.sendWillHelpYouRequest(requestBody, new ResponseDTOListener() {
			
			@Override
			public void successResponseRecieved(ResponseDTO responseDTO) {
//				eventId = responseDTO.getEventId();
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
//				eventId = responseDTO.getEventId();
			}
			
			@Override
			public void errorResponseRecieved(int status, String message) {
				Log.w("sendHelpMeRequest", "Error response recieved while trying to update help you: " + message);
			}
		});
	}
	
	private void sendHelpYouGetEventRequest() {
		helpYouDAO.sendHelpYouGetEventRequest(eventId, new ResponseDTOListener() {
			
			@Override
			public void successResponseRecieved(ResponseDTO responseDTO) {
//				eventId = responseDTO.getEventId();
				eventResponseDTO = (EventResponseDTO)responseDTO;
				helpee = updateMarkerPosition(helpee, eventResponseDTO.getHelpeeCoords());
//				setHelperPointsOnMap(eventResponseDTO.getHelpeeCoords(), true);
//				for(GPSCoordinates coords : eventResponseDTO.getHelpers()) {
//					setHelperPointsOnMap(coords, false);
//				}
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
		zoomToMyLocation(this.location);
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
	        	sendHelpYouGetEventRequest();
	        }
		});
	}
}