package com.example.mytest.dao;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mytest.R;
import com.example.mytest.dto.RequestDTO;
import com.example.mytest.dto.EventIdResponseDTO;
import com.example.mytest.dto.ResponseDTOListener;

public class HelpMeDAO extends NetworkDAO {
	
	public HelpMeDAO(Context context) {
		super(context, context.getString(R.string.help_me_base_url));
	}

	public void sendHelpMeRequest(RequestDTO body, final ResponseDTOListener listener) {
		sendPostRequest(new Response.Listener<JSONObject>() {
		    @Override
		    public void onResponse(JSONObject response) {
		    	Log.i("sendHelpMeRequest", response.toString());
		    	listener.successResponseRecieved(new EventIdResponseDTO(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	int status = error.networkResponse == null ? 0 : error.networkResponse.statusCode;
		    	listener.errorResponseRecieved(status, error.getMessage());
		    }
		}, body.toJSON(), context.getString(R.string.help_me_request_url));
	}
	
	public void sendUpdateHelpMeRequest(RequestDTO body, final ResponseDTOListener listener) {
		sendPutRequest(new Response.Listener<JSONObject>() {
		    @Override
		    public void onResponse(JSONObject response) {
		    	listener.successResponseRecieved(new EventIdResponseDTO(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	int status = error.networkResponse == null ? 0 : error.networkResponse.statusCode;
		    	listener.errorResponseRecieved(status, error.getMessage());
		    }
		}, body.toJSON(), context.getString(R.string.help_me_update_url));
	}
	
	public void sendCancelHelpMeRequest(RequestDTO body, final ResponseDTOListener listener) {
		sendDeleteRequest(new Response.Listener<JSONObject>() {
		    @Override
		    public void onResponse(JSONObject response) {
		    	listener.successResponseRecieved(new EventIdResponseDTO(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	int status = error.networkResponse == null ? 0 : error.networkResponse.statusCode;
		    	listener.errorResponseRecieved(status, error.getMessage());
		    }
		}, body.toJSON(), context.getString(R.string.help_me_cancel_url));
	}
}
