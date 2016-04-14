package com.mxchip.callbacks;

public interface ManageDeviceCallBack {
	public void onSuccess(String message);
	public void onFailure(int code, String message);
}
