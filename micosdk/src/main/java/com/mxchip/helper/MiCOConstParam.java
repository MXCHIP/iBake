package com.mxchip.helper;

public class MiCOConstParam {
	
	public static int EXCEPTIONCODE = 9500;
	public static int EMPTYCODE = 9400;
	public static int ISWORKINGCODE = 9403;
	public static int ALREADYSTOPCODE = 9403;
	public static int QOSERRCODE = 9403;
	public static int _TIMEOUT = 15000;
	public static int HEARTBEADSLEEP = 2000;
	public static int HEARTBEADTIMES = 4;
	public static int SOCKETEXCEPTION = 0;
	public static int CLOSEDCODE = 1001;
	
	public static String LOCALDEVICEPORT = "8002";
	
	public static String SUCCESS = "success";
	public static String STOP_SUCCESS = "stop success";
	public static String EMPTY = "Parameters can not be empty.";
	public static String RunSecondMax = "RunSecond should greater than 1.";
	public static String ROLENUM = "Role must within (1,2,3).";
	public static String QOSERR = "Qos must within (0,1,2).";
	public static String CONTEXTISNULL = "Context can not be null.";
	public static String ISWORKING = "It is working.";
	public static String ALREADYSTOP = "It is closed.";
	public static String CLOSED = "Socket is closed.";
	public static String HEARTBEATMSG = "{\"heartbeat\":\"\"}";
	
//	public static int _ILLEGAL_ARGUMENT_CODE = 4000;
//	public String _ILLEGAL_ARGUMENT_MSG = "Illegal argument.";
//	public JSONObject _ILLEGAL_ARGUMENT_JSON;
//
//	public MiCOConstParam() {
//		try {
//			_ILLEGAL_ARGUMENT_JSON = new JSONObject("{\"code\":"+_ILLEGAL_ARGUMENT_CODE+",\"message\":\""+_ILLEGAL_ARGUMENT_MSG+"\"}");
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
}
