package com.mxchip.callbacks;

/**
 * Developer'd client.
 * @Project WSocketClient
 * @Author Sin
 * @Createtime 
 * @version 1.0
 */
public abstract class SinSocketCallBack {
	public void onLost() {}
	public void onSuccess(String message) {}
	public void onFailure(int code, String message) {}
	public void onMessageRead(String message) {}
}
