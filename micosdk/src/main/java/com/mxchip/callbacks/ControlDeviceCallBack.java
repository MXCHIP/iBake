package com.mxchip.callbacks;

//TODO abstract class
public abstract class ControlDeviceCallBack {
	public void onSuccess(String message){}
	public void onFailure(int code, String message){}
	public void onDeviceStatusReceived(String msgType, String messages){}
}
