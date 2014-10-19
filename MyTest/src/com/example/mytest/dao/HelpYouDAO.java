package com.example.mytest.dao;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mytest.R;
import com.example.mytest.dto.EventResponseDTO;
import com.example.mytest.dto.RequestDTO;
import com.example.mytest.dto.ResponseDTO;
import com.example.mytest.dto.ResponseDTOListener;

public class HelpYouDAO extends NetworkDAO {

	public HelpYouDAO(Context context) {
		super(context, context.getString(R.string.help_you_base_url));
	}

	public void sendDoesAnyoneNeedHelpRequest(RequestDTO body, final ResponseDTOListener listener) {
		this.sendPostRequest(new Response.Listener<JSONObject>() {
		    @Override
		    public void onResponse(JSONObject response) {
		    	listener.successResponseRecieved(new ResponseDTO(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	int status = error.networkResponse == null ? 0 : error.networkResponse.statusCode;
		    	listener.errorResponseRecieved(status, error.getMessage());
		    }
		}, body.toJSON(), context.getString(R.string.help_you_anyone_need_help_url));
	}
	
	public void sendWillHelpYouRequest(RequestDTO body, final ResponseDTOListener listener) {
		this.sendPostRequest(new Response.Listener<JSONObject>() {
		    @Override
		    public void onResponse(JSONObject response) {
		    	listener.successResponseRecieved(new ResponseDTO(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	int status = error.networkResponse == null ? 0 : error.networkResponse.statusCode;
		    	listener.errorResponseRecieved(status, error.getMessage());
		    }
		}, body.toJSON(), context.getString(R.string.help_you_will_help_url));
	}
	
	public void sendHelpingYouUpdateRequest(RequestDTO body, final ResponseDTOListener listener) {
		this.sendPostRequest(new Response.Listener<JSONObject>() {
		    @Override
		    public void onResponse(JSONObject response) {
		    	listener.successResponseRecieved(new ResponseDTO(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	int status = error.networkResponse == null ? 0 : error.networkResponse.statusCode;
		    	listener.errorResponseRecieved(status, error.getMessage());
		    }
		}, body.toJSON(), context.getString(R.string.help_you_update_url));
	}
	
	public void sendHelpYouGetEventRequest(String eventId, final ResponseDTOListener listener) {
		this.sendGetRequest(new Response.Listener<JSONObject>() {
		    @Override
		    public void onResponse(JSONObject response) {
		    	listener.successResponseRecieved(new EventResponseDTO(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	int status = error.networkResponse == null ? 0 : error.networkResponse.statusCode;
		    	listener.errorResponseRecieved(status, error.getMessage());
		    }
		}, new JSONObject(), context.getString(R.string.help_you_get_event_url) + eventId);
	}
}
