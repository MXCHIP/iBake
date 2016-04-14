package com.mxchip.callbacks;

import org.json.JSONArray;

public interface SearchDeviceCallBack {
	public void onSuccess(String message);

	public void onFailure(int code, String message);

	public void onDevicesFind(JSONArray deviceStatus);
}
