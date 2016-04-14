package com.mxchip.callbacks;

public interface UserCallBack {
	public void onSuccess(String message);

	public void onFailure(int code, String message);
}
