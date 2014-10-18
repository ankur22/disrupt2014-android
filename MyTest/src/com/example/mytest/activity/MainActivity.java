package com.example.mytest.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.mytest.R;

public class MainActivity extends Activity {
	String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupHelpMeButton();
		setupHelpYouButton();
		
		Intent intent = getIntent();
		this.userId = intent.getStringExtra(getString(R.string.login_to_main_extra_id));
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
        	Toast.makeText(getApplicationContext(), "Current Id is " + userId, Toast.LENGTH_SHORT).show();
	        return true;
        }
		return false;
	}
	
	OnTouchListener helpMeButtonTouchListener = new OnTouchListener() {
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	    	Rect buttonBounds = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
	    	
	    	switch(event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		v.setBackgroundResource(R.color.help_me_button_down_color);
	    		return false;
	    	case MotionEvent.ACTION_UP:
	    		if (isTouchInButtonBounds(buttonBounds, v, event.getX(), event.getY())) {
	    			// TODO: Send POST to /help/me
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
	    			// TODO: Send POST to /help/me
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

}
