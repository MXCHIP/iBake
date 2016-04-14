package com.mxchip.helper;

public class SinSocketParams {
	public String ip;
	public int port;
	public int overTime;
	public int heartBeatTime;
	public int autoConnectNo;
	
	public SinSocketParams(){
		this.ip = null;
		this.port = 8002;
		this.overTime = 60000;
		this.heartBeatTime = 20000;
		this.autoConnectNo = 1000;
	}
}
