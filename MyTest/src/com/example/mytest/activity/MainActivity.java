package com.example.mytest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.mytest.R;
import com.example.mytest.controller.Alarm;
import com.example.mytest.controller.AlarmListener;
import com.example.mytest.dao.HelpMeDAO;
import com.example.mytest.dao.HelpYouDAO;
import com.example.mytest.dto.RequestDTO;
import com.example.mytest.dto.ResponseDTO;
import com.example.mytest.dto.ResponseDTOListener;
import com.example.mytest.model.GPSCoordinates;

public class MainActivity extends Activity implements LocationListener, AlarmListener {
	HelpMeDAO helpMeDAO;
	HelpYouDAO helpYouDAO;
	LocationManager locationManager;
	Alarm alarm;
	
	boolean helpMeHelpStateOn;
	boolean sentHelpYouRequest;
	
	String userId;
	GPSCoordinates location;
	String eventId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupHelpMeButton();
		setupHelpYouButton();
		setupLocationManager();
		
		helpMeHelpStateOn = false;
		sentHelpYouRequest = false;
		helpMeDAO = new HelpMeDAO(this.getApplicationContext());
		helpYouDAO = new HelpYouDAO(this.getApplicationContext());
		alarm = new Alarm(getApplicationContext(), this);
		
		Intent intent = getIntent();
		this.userId = intent.getStringExtra(getString(R.string.extra_user_id));
		location = new GPSCoordinates();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		alarm = new Alarm(getApplicationContext(), this);
		location = new GPSCoordinates();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearPollingUpdates();
	}
	
	private void clearPollingUpdates() {
		alarm.clearAlarm();
		locationManager.removeUpdates(this);
	}
	
	private void setupLocationManager() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				7000, // 7 secs
				10, this);
	}
	
	private void setupHelpMeButton() {
		Button helpMeButton = (Button)findViewById(R.id.helpMeButton);
		helpMeButton.setOnTouchListener(helpMeButtonTouchListener);
	}
	
	private void setupHelpYouButton() {
		Button helpYouButton = (Button)findViewById(R.id.helpYouButton);
		helpYouButton.setOnTouchListener(helpYouButtonTouchListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings:
        	Toast.makeText(getApplicationContext(), "Current Id is " + userId + ". " +
        			location.getLatitude() + ":" + location.getLongitude(), Toast.LENGTH_SHORT).show();
	        return true;
        }
		return false;
	}
	
	OnTouchListener helpMeButtonTouchListener = new OnTouchListener() {
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	    	Rect buttonBounds = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
	    	Button button = (Button)v;
	    	
	    	switch(event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		v.setBackgroundResource(R.color.help_me_button_down_color);
	    		return false;
	    	case MotionEvent.ACTION_UP:
	    		if (isTouchInButtonBounds(buttonBounds, v, event.getX(), event.getY())) {
	    			if (helpMeHelpStateOn) {
	    				helpMeHelpStateOn = false;
	    				button.setText(getString(R.string.help_me_button_text));
	    				sendCancelHelpMeRequest();
	    			} else {
	    				helpMeHelpStateOn = true;
	    				button.setText(getString(R.string.help_me_button_cancel_text));
	    				sendHelpMeRequest();
	    			}
	    		}
	    		v.setBackgroundResource(R.color.help_me_button_up_color);
	    		return false;
	    	}
	        return false;
	    }
	    
	    private boolean isTouchInButtonBounds(Rect buttonBounds, View v, float x, float y) {
	    	return buttonBounds.contains(v.getLeft() + (int)x, v.getTop() + (int)y);
	    }
	};
	
	private void sendHelpMeRequest() {
		RequestDTO requestBody = new RequestDTO(userId, location);
		
		helpMeDAO.sendHelpMeRequest(requestBody, new ResponseDTOListener() {
			
			@Override
			public void successResponseRecieved(ResponseDTO responseDTO) {
				eventId = responseDTO.getEventId();
			}
			
			@Override
			public void errorResponseRecieved(int status, String message) {
				Log.w("sendHelpMeRequest", "Error response recieved while trying to request help me: " + message);
			}
		});
	}
	
	private void sendCancelHelpMeRequest() {
		RequestDTO requestBody = new RequestDTO(userId, location, eventId);
		
		helpMeDAO.sendCancelHelpMeRequest(requestBody, new ResponseDTOListener() {
			
			@Override
			public void successResponseRecieved(ResponseDTO responseDTO) {
				eventId = responseDTO.getEventId();
			}
			
			@Override
			public void errorResponseRecieved(int status, String message) {
				Log.w("sendHelpMeRequest", "Error response recieved while trying to request help me: " + message);
			}
		});
	}
	
	private void sendHelpMeUpdateRequest() {
		RequestDTO requestBody = new RequestDTO(userId, location, eventId);
		
		helpMeDAO.sendUpdateHelpMeRequest(requestBody, new ResponseDTOListener() {
			
			@Override
			public void successResponseRecieved(ResponseDTO responseDTO) {
			}
			
			@Override
			public void errorResponseRecieved(int status, String message) {
				Log.w("sendHelpMeRequest", "Error response recieved while trying to send update request for help me: " + message);
			}
		});
	}
	
	OnTouchListener helpYouButtonTouchListener = new OnTouchListener() {
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	    	Rect buttonBounds = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
	    	
	    	switch(event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		v.setBackgroundResource(R.color.help_you_button_down_color);
	    		return false;
	    	case MotionEvent.ACTION_UP:
	    		if (isTouchInButtonBounds(buttonBounds, v, event.getX(), event.getY())) {
	    			sendHelpYouRequest(false);
	    		}
	    		v.setBackgroundResource(R.color.help_you_button_up_color);
	    		return false;
	    	}
	        return false;
	    }
	    
	    private boolean isTouchInButtonBounds(Rect buttonBounds, View v, float x, float y) {
	    	return buttonBounds.contains(v.getLeft() + (int)x, v.getTop() + (int)y);
	    }
	};
	
	private void sendHelpYouRequest(final boolean background) {
		if (!sentHelpYouRequest) {
			sentHelpYouRequest = true;
			RequestDTO requestBody = new RequestDTO(userId, location);
			
			helpYouDAO.sendDoesAnyoneNeedHelpRequest(requestBody, new ResponseDTOListener() {
				
				@Override
				public void successResponseRecieved(ResponseDTO responseDTO) {
					eventId = responseDTO.getEventId();
					if (eventId != null && eventId.equals("-1")) {
						eventId = null;
					}
					if (eventId == null && !background) {
						Toast.makeText(getApplicationContext(), "All is well in your neighbourhood.", Toast.LENGTH_SHORT).show();
					} else if (eventId != null && background) {
						Toast.makeText(getApplicationContext(), "Someone has requested help.", Toast.LENGTH_SHORT).show();
					} else if (eventId != null && !background) {
						startMapActivity();
						clearPollingUpdates();
					}
					sentHelpYouRequest = false;
				}
				
				@Override
				public void errorResponseRecieved(int status, String message) {
					Log.w("sendHelpMeRequest", "Error response recieved while trying to request help me: " + message);
					sentHelpYouRequest = false;
				}
			});
		}
	}
	
	private void startMapActivity() {
		Intent intent = new Intent(this, MapActivity.class);
		intent.putExtra(getString(R.string.extra_user_id), userId);
		intent.putExtra(getString(R.string.extra_event_id), eventId);
		startActivity(intent);
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
	        	if (helpMeHelpStateOn) {
	        		sendHelpMeUpdateRequest();
	        	} else {
	        		sendHelpYouRequest(true);
	        	}
	        }
		});
	}
}
