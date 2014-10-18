package com.example.mytest.dao;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mytest.R;

import android.content.Context;

public class HelpMeDAO extends NetworkDAO {
	
	public HelpMeDAO(Context context) {
		super(context, context.getString(R.string.help_me_base_url));
	}

	public void sendHelpMeRequest() {
		this.sendPostRequest(new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) {
		    	// TODO: Do something with success
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	// TODO: Do something with error
		    }
		}, context.getString(R.string.help_me_request_url));
	}
	
	public void sendUpdateHelpMeRequest() {
		this.sendPutRequest(new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) {
		    	// TODO: Do something with success
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	// TODO: Do something with error
		    }
		}, context.getString(R.string.help_me_update_url));
	}
	
	public void sendCancelHelpMeRequest() {
		this.sendDeleteRequest(new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) {
		    	// TODO: Do something with success
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	// TODO: Do something with error
		    }
		}, context.getString(R.string.help_me_cancel_url));
	}
}
