package com.example.mytest.dto;

public interface ResponseDTOListener {
	public void successResponseRecieved(ResponseDTO responseDTO);
	public void errorResponseRecieved(int status, String message);
}
