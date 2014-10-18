package com.example.mytest.controller;

import android.content.BroadcastReceiver;
import android.content.Context;

import com.example.mytest.activity.MainActivity;

public class Alarm {
	AlarmListener listener;
	Thread alarmThread;
	boolean shouldStop;
	Object lock;
	
	public Alarm(Context context, AlarmListener listener) {
		lock = new Object();
		shouldStop = false;
		setupAlarm(context);
		this.listener = listener;
	}
	
	private void setupAlarm(Context context) {
		new Thread(new Runnable() {
			public void run() {
				while(!shouldStop) {
					try {
						synchronized (this) {
							wait(7000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
					listener.alarm();
				}
			}
		}).start();
	}

	public void clearAlarm() {
		shouldStop = true;
	}
}
