package com.mxchip.callbacks;

public interface EasyLinkCallBack {
	public void onSuccess(String message);

	public void onFailure(int code, String message);
}
