package com.example.mytest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.toolkit.map.MapViewHelper;
import com.esri.core.geometry.Point;
import com.esri.core.tasks.geocode.Locator;
import com.example.mytest.R;
import com.example.mytest.controller.Alarm;
import com.example.mytest.controller.AlarmListener;
import com.example.mytest.dao.HelpYouDAO;
import com.example.mytest.dto.EventResponseDTO;
import com.example.mytest.dto.RequestDTO;
import com.example.mytest.dto.ResponseDTO;
import com.example.mytest.dto.ResponseDTOListener;
import com.example.mytest.model.GPSCoordinates;



public class MapActivity extends Activity implements LocationListener, AlarmListener {
	Locator locator;
	
	MapView mMapView;
	GraphicsLayer mLocationLayer;
	String mMapViewState;
	MapViewHelper mvHelper;
	
	HelpYouDAO helpYouDAO;
	LocationManager locationManager;
	
	String userId;
	GPSCoordinates location;
	String eventId;
	EventResponseDTO eventResponseDTO;
	
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

		// Retrieve the map and initial extent from XML layout
		mMapView = (MapView)findViewById(R.id.map);
		// Add dynamic layer to MapView
		mMapView.addLayer(new ArcGISTiledMapServiceLayer(getString(R.string.esri_map_url)));
		
		mLocationLayer = new GraphicsLayer();
		mMapView.addLayer(mLocationLayer);
		// set logo and enable wrap around
		mMapView.setEsriLogoVisible(true);
		mMapView.enableWrapAround(true);
		
		// Setup listener for map initialized
		mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onStatusChanged(Object source, STATUS status) {
				if (source == mMapView && status == STATUS.INITIALIZED) {

					if (mMapViewState != null) {
						mMapView.restoreState(mMapViewState);
					}
				}
			}
		});
		
		// Create a MapView Helper 
		mvHelper = new MapViewHelper(mMapView);
		
		//setHelperPointsOnMap();
		sendWillHelpYouRequest();
	}
	
	private void setHelperPointsOnMap(GPSCoordinates coords, boolean me) {
		mMapView = (MapView) findViewById(R.id.map); 
		// Create drawable icon
		int drawableId = R.drawable.you_marker;
		if (me) {
			drawableId = R.drawable.ic_launcher;
		}
		final Drawable icon = getResources().getDrawable(drawableId);
		// Make sure map has loaded before adding geometries 
		mMapView.setOnStatusChangedListener(new OnStatusChangedListener() { 
		  private static final long serialVersionUID = 1L; 
		  public void onStatusChanged(Object source, STATUS status) { 
		    // Add a graphic to represent ESRI Headquarters 
		    int loaded = mvHelper.addMarkerGraphic(34.056695, -117.195693, 
		                 "", "", null, icon, false, 0); 
			if (loaded < 0) {
				Log.d("TAG", "Marker Graphic not added to MapView"); 
			} 
		  } 
		});
		if (me) {
			mMapView.zoomToResolution(new Point(coords.getLongitude(), coords.getLatitude()), 2);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.pause();
		mMapViewState = mMapView.retainState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Start the MapView running again
		if (mMapView != null) {
			mMapView.unpause();
			if (mMapViewState != null) {
				mMapView.restoreState(mMapViewState);
			}
		}
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
				mvHelper.removeAllGraphics();
				setHelperPointsOnMap(location, false);
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
				mvHelper.removeAllGraphics();
				setHelperPointsOnMap(eventResponseDTO.getHelpeeCoords(), true);
				for(GPSCoordinates coords : eventResponseDTO.getHelpers()) {
					setHelperPointsOnMap(coords, false);
				}
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
	        	sendHelpYouGetEventRequest();
	        }
		});
	}
}