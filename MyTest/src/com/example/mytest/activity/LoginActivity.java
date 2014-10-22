package com.example.mytest.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.example.mytest.R;

public class LoginActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setupLoginAskHelpButton();
		setupLoginAssistButton();
	}
	
	private void setupLoginAskHelpButton() {
		Button helpMeButton = (Button)findViewById(R.id.LoginButton);
		helpMeButton.setOnTouchListener(loginAskForHelpButtonTouchListener);
	}
	
	private void setupLoginAssistButton() {
		Button helpYouButton = (Button)findViewById(R.id.RegisterButton);
		helpYouButton.setOnTouchListener(loginAssistButtonTouchListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void login(String id) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(getString(R.string.extra_user_id), id);
		startActivity(intent);
	}
	
	OnTouchListener loginAskForHelpButtonTouchListener = new OnTouchListener() {
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	    	Rect buttonBounds = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
	    	
	    	switch(event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		v.setBackgroundResource(R.color.login_down_button);
	    		return false;
	    	case MotionEvent.ACTION_UP:
	    		if (isTouchInButtonBounds(buttonBounds, v, event.getX(), event.getY())) {
	    			login(getString(R.string.login_ask_for_help_user_id));
	    			return true;
	    		}
	    		v.setBackgroundResource(R.color.login_up_button);
	    		return false;
	    	}
	        return false;
	    }
	    
	    private boolean isTouchInButtonBounds(Rect buttonBounds, View v, float x, float y) {
	    	return buttonBounds.contains(v.getLeft() + (int)x, v.getTop() + (int)y);
	    }
	};
	
	OnTouchListener loginAssistButtonTouchListener = new OnTouchListener() {
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	    	Rect buttonBounds = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
	    	
	    	switch(event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		v.setBackgroundResource(R.color.login_down_button);
	    		return false;
	    	case MotionEvent.ACTION_UP:
	    		if (isTouchInButtonBounds(buttonBounds, v, event.getX(), event.getY())) {
	    			login(getString(R.string.login_assist_user_id));
	    			return true;
	    		}
	    		v.setBackgroundResource(R.color.login_up_button);
	    		return false;
	    	}
	        return false;
	    }
	    
	    private boolean isTouchInButtonBounds(Rect buttonBounds, View v, float x, float y) {
	    	return buttonBounds.contains(v.getLeft() + (int)x, v.getTop() + (int)y);
	    }
	};
	
	@Override
	public void onBackPressed() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.exit_app_title);
	    builder.setMessage(R.string.exit_app_message);
	    builder.setPositiveButton(R.string.exit_app_yes_button, new OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            finish();
	        }
	    });
	    builder.setNegativeButton(R.string.exit_app_no_button, new OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {

	        }
	    });
	    builder.show();
	}
}
