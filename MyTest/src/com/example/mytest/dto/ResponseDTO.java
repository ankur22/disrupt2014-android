package com.example.mytest.dto;

import com.android.volley.VolleyError;

public class ResponseDTO {
	private String body;
	private VolleyError error;
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public void setError(VolleyError error) {
		this.error = error;
	}
	
	public String getBody() {
		return this.body;
	}
	
	public VolleyError getError() {
		return this.error;
	}
}
