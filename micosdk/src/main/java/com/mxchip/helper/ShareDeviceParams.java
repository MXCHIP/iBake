package com.mxchip.helper;

public class ShareDeviceParams {
	public String bindvercode;
	public String deviceid;
	public String devicepw;
	public int role;
	public String bindingtype;
	public boolean iscallback;
	
	public ShareDeviceParams(){
		this.bindvercode = null;
		this.deviceid = null;
		this.devicepw = null;
		this.role = 0;
		this.bindingtype = null;
		this.iscallback = false;
	}
}
