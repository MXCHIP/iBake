package com.mxchip.micosocket;

public abstract class SinSocketListener {
	public void onSuccess() {}
	public void onFailure(int code, String message) {}
	public void onLost() {}
	public void onHeartBeat(String message) {}
	public void onMessageRead(String message) {}
}
