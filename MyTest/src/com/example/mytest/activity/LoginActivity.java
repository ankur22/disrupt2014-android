package com.example.mytest.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.mytest.R;
import com.example.mytest.dao.UserDAO;
import com.example.mytest.dto.LoginDTO;
import com.example.mytest.dto.LoginResponseDTO;
import com.example.mytest.dto.RegisterDTO;
import com.example.mytest.dto.RegisterResponseDTO;
import com.example.mytest.dto.ResponseDTO;
import com.example.mytest.dto.ResponseDTOListener;

public class LoginActivity extends Activity {
	UserDAO userDAO;
	
	ProgressDialog progressDialog;
	
	boolean sentLoginRequest;
	boolean sentRegisterRequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setupLoginAskHelpButton();
		setupLoginAssistButton();
		
		userDAO = new UserDAO(this.getApplicationContext());
	}
	
	private void setupLoginAskHelpButton() {
		Button helpMeButton = (Button)findViewById(R.id.LoginButton);
		helpMeButton.setOnTouchListener(loginButtonTouchListener);
	}
	
	private void setupLoginAssistButton() {
		Button helpYouButton = (Button)findViewById(R.id.RegisterButton);
		helpYouButton.setOnTouchListener(registerButtonTouchListener);
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
	
	private void displayProgressDialog() {
		if (progressDialog == null || !progressDialog.isShowing()) {
			progressDialog = ProgressDialog.show(this,
        			getString(R.string.progress_dialog_default_title),
        			getString(R.string.progress_dialog_default_message));
		}
	}
	
	private void dismissProgressDialog() {
		runOnUiThread(new Runnable() {
	        public void run() {
	        	if (progressDialog != null && progressDialog.isShowing()) {
	        		progressDialog.dismiss();
	        	}
	        }
		});
	}
	
	private String getTextFromEditText(int id) {
		EditText helpYouButton = (EditText)findViewById(id);
		return helpYouButton.getText().toString();
	}
	
	private boolean isLoginInputValid(String email, String password) {
		return email != null && email.length() > 0 &&
				password != null && password.length() > 0;
	}
	
	private void sendLoginRequest() {
		if (!sentLoginRequest) {
			sentLoginRequest = true;
			
			String email = getTextFromEditText(R.id.EmailTextField);
			String password = getTextFromEditText(R.id.PasswordTextField);
			
			if (!isLoginInputValid(email, password)) {
				dismissProgressDialog();
				return;
			}
			
			LoginDTO requestBody = new LoginDTO(email, password);
			
			userDAO.sendLoginRequest(requestBody, new ResponseDTOListener() {
				
				@Override
				public void successResponseRecieved(ResponseDTO responseDTO) {
					login(((LoginResponseDTO)responseDTO).getUserId());
					sentLoginRequest = false;
					dismissProgressDialog();
				}
				
				@Override
				public void errorResponseRecieved(int status, String message) {
					Log.w("sendLoginRequest", "Error response recieved while trying to login: " + message);
					sentLoginRequest = false;
					dismissProgressDialog();
				}
			});
		}
	}
	
	OnTouchListener loginButtonTouchListener = new OnTouchListener() {
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	    	Rect buttonBounds = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
	    	
	    	switch(event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		v.setBackgroundResource(R.color.login_down_button);
	    		return false;
	    	case MotionEvent.ACTION_UP:
	    		if (isTouchInButtonBounds(buttonBounds, v, event.getX(), event.getY())) {
	    			sendLoginRequest();
	    			displayProgressDialog();
	    			return v.performClick();
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
	
	private boolean isRegisterInputValid(String email, String password, String confirmPassword) {
		return email != null && email.length() > 0 &&
				password != null && password.length() > 0 &&
				confirmPassword != null && confirmPassword.length() > 0 &&
				confirmPassword.equals(password);
	}
	
	private void sendRegisterRequest() {
		if (!sentRegisterRequest) {
			sentRegisterRequest = true;
			
			String email = getTextFromEditText(R.id.RegisterEmailTextField);
			String password = getTextFromEditText(R.id.RegisterPasswordTextField);
			String confirmPassword = getTextFromEditText(R.id.RegisterConfirmPasswordTextField);
			
			if (!isRegisterInputValid(email, password, confirmPassword)) {
				dismissProgressDialog();
				return;
			}
			
			RegisterDTO requestBody = new RegisterDTO(email, password);
			
			userDAO.sendRegisterRequest(requestBody, new ResponseDTOListener() {
				
				@Override
				public void successResponseRecieved(ResponseDTO responseDTO) {
					login(((RegisterResponseDTO)responseDTO).getUserId());
					sentRegisterRequest = false;
					dismissProgressDialog();
				}
				
				@Override
				public void errorResponseRecieved(int status, String message) {
					Log.w("sendRegisterRequest", "Error response recieved while trying to register: " + message);
					sentRegisterRequest = false;
					dismissProgressDialog();
				}
			});
		}
	}
	
	OnTouchListener registerButtonTouchListener = new OnTouchListener() {
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	    	Rect buttonBounds = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
	    	
	    	switch(event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		v.setBackgroundResource(R.color.login_down_button);
	    		return false;
	    	case MotionEvent.ACTION_UP:
	    		if (isTouchInButtonBounds(buttonBounds, v, event.getX(), event.getY())) {
	    			sendRegisterRequest();
	    			displayProgressDialog();
	    			return v.performClick();
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
