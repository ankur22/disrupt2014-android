package com.example.mytest.dao;

import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytest.dto.ResponseDTO;

public abstract class NetworkDAO {
	
	protected Context context;
	protected String baseURL;
	
	public NetworkDAO(Context context, String baseURL) {
		this.context = context;
		this.baseURL = baseURL;
	}
	
	protected void sendGetRequest(Listener<String> successListener, ErrorListener errorListener, String url) {
		this.sendRequest(successListener, errorListener, this.baseURL + url, Method.GET);
	}
	
	protected void sendRequest(Listener<String> successListener, ErrorListener errorListener, String url,
			int requestType) {
		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this.context);

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(requestType, url, successListener, errorListener);
		
		// Add the request to the RequestQueue.
		queue.add(stringRequest);
	}
	
	protected void sendPutRequest(Listener<String> successListener, ErrorListener errorListener, String url) {
		this.sendRequest(successListener, errorListener, this.baseURL + url, Method.PUT);
	}
	
	protected void sendPostRequest(Listener<String> successListener, ErrorListener errorListener, String url) {
		this.sendRequest(successListener, errorListener, this.baseURL + url, Method.POST);
	}
	
	protected void sendDeleteRequest(Listener<String> successListener, ErrorListener errorListener, String url) {
		this.sendRequest(successListener, errorListener, this.baseURL + url, Method.DELETE);
	}
}
