package com.example.mytest.dao;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public abstract class NetworkDAO {
	
	protected Context context;
	protected String baseURL;
	
	public NetworkDAO(Context context, String baseURL) {
		this.context = context;
		this.baseURL = baseURL;
	}
	
	protected void sendGetRequest(Listener<JSONObject> successListener, ErrorListener errorListener, JSONObject body, String url) {
		sendRequest(successListener, errorListener, body, this.baseURL + url, Method.GET);
	}
	
	protected void sendRequest(Listener<JSONObject> successListener, ErrorListener errorListener,
			JSONObject body, String url, int requestType) {
		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this.context);

		Log.d("sendRequest", body.toString());
		
		// Request a string response from the provided URL.
		JsonObjectRequest jsonRequest = new JsonObjectRequest(requestType, url, body, successListener, errorListener);
		
		// Add the request to the RequestQueue.
		queue.add(jsonRequest);
	}
	
	protected void sendPutRequest(Listener<JSONObject> successListener, ErrorListener errorListener, JSONObject body, String url) {
		sendRequest(successListener, errorListener, body, this.baseURL + url, Method.PUT);
	}
	
	protected void sendPostRequest(Listener<JSONObject> successListener, ErrorListener errorListener, JSONObject body, String url) {
		sendRequest(successListener, errorListener, body, this.baseURL + url, Method.POST);
	}
	
	protected void sendDeleteRequest(Listener<JSONObject> successListener, ErrorListener errorListener, JSONObject body, String url) {
		sendRequest(successListener, errorListener, body, this.baseURL + url, Method.DELETE);
	}
}
