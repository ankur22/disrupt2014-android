package com.example.mytest.dao;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mytest.R;

import android.content.Context;

public class HelpYouDAO extends NetworkDAO {

	public HelpYouDAO(Context context) {
		super(context, context.getString(R.string.help_you_base_url));
	}

	public void sendDoesAnyoneNeedHelpRequest() {
		this.sendGetRequest(new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) {
		    	// TODO: Do something with success
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	// TODO: Do something with error
		    }
		}, context.getString(R.string.help_you_anyone_need_help_url));
	}
	
	public void sendWillHelpYouRequest() {
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
		}, context.getString(R.string.help_you_will_help_url));
	}
	
	public void sendHelpingYouUpdateRequest() {
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
		}, context.getString(R.string.help_you_update_url));
	}
}
