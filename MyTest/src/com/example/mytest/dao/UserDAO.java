package com.example.mytest.dao;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mytest.R;
import com.example.mytest.dto.LoginDTO;
import com.example.mytest.dto.LoginResponseDTO;
import com.example.mytest.dto.RegisterDTO;
import com.example.mytest.dto.RegisterResponseDTO;
import com.example.mytest.dto.ResponseDTOListener;

public class UserDAO extends NetworkDAO {

	public UserDAO(Context context) {
		super(context, context.getString(R.string.login_base_url));
	}

	public void sendLoginRequest(LoginDTO body, final ResponseDTOListener listener) {
		this.sendPostRequest(new Response.Listener<JSONObject>() {
		    @Override
		    public void onResponse(JSONObject response) {
		    	listener.successResponseRecieved(new LoginResponseDTO(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	int status = error.networkResponse == null ? 0 : error.networkResponse.statusCode;
		    	listener.errorResponseRecieved(status, error.getMessage());
		    }
		}, body.toJSON(), context.getString(R.string.login_url));
	}
	
	public void sendRegisterRequest(RegisterDTO body, final ResponseDTOListener listener) {
		this.sendPostRequest(new Response.Listener<JSONObject>() {
		    @Override
		    public void onResponse(JSONObject response) {
		    	listener.successResponseRecieved(new RegisterResponseDTO(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	int status = error.networkResponse == null ? 0 : error.networkResponse.statusCode;
		    	listener.errorResponseRecieved(status, error.getMessage());
		    }
		}, body.toJSON(), context.getString(R.string.login_url));
	}
}
