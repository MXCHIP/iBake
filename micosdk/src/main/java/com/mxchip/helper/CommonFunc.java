package com.mxchip.helper;

import org.json.JSONArray;

import com.mxchip.callbacks.ControlDeviceCallBack;
import com.mxchip.callbacks.EasyLinkCallBack;
import com.mxchip.callbacks.ManageDeviceCallBack;
import com.mxchip.callbacks.SearchDeviceCallBack;
import com.mxchip.callbacks.SinSocketCallBack;
import com.mxchip.callbacks.UserCallBack;

/**
 * Many javas will use this function Project：MiCOSDK Author：Sin Creat time
 * 2016-01-20
 * 
 * @version 1.0
 */
public class CommonFunc {
	
	/**
	 * Check argument, whether it is null or blank
	 * 
	 * @param param
	 * @return
	 */
	public boolean checkPara(String... param) {
		if (null == param || param.equals("")) {
			return false;
		} else if (param.length > 0) {
			for (String str : param) {
				if (null == str || str.equals("")) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * This field may not be blank
	 * 
	 * @param usercb
	 */
	public void illegalCallBack(UserCallBack usercb) {
		failureCBFilterUser(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, usercb);
	}

	/**
	 * Success for user,
	 * Check if we will call back to the developer.
	 *
	 * @param message
	 * @param usercb
	 */
	public void succeesCBFilterUser(String message, UserCallBack usercb) {
		if (null == usercb)
			return;
		usercb.onSuccess(message);
	}
	public void failureCBFilterUser(int code, String message, UserCallBack usercb) {
		if (null == usercb)
			return;
		usercb.onFailure(code, message);
	}

	/**
	 * EasyLink.
	 * Check if we will call back to the developer.
	 * @param message
	 * @param easylinkcb
	 */
	public void successCBEasyLink(String message, EasyLinkCallBack easylinkcb) {
		if (null == easylinkcb)
			return;
		easylinkcb.onSuccess(message);
	}
	public void failureCBEasyLink(int code, String message, EasyLinkCallBack easylinkcb) {
		if (null == easylinkcb)
			return;
		easylinkcb.onFailure(code, message);
	}
	
	/**
	 * About mDNS, we will call back to the developer. 
	 * @param message
	 * @param searchdevcb
	 */
	public void successCBmDNS(String message, SearchDeviceCallBack searchdevcb) {
		if (null == searchdevcb)
			return;
		searchdevcb.onSuccess(message);
	}
	public void failureCBmDNS(int code, String message,SearchDeviceCallBack searchdevcb) {
		if (null == searchdevcb)
			return;
		searchdevcb.onFailure(code, message);
	}
	public void onDevsFindmDNS(JSONArray deviceStatus, SearchDeviceCallBack searchdevcb) {
		if (null == searchdevcb)
			return;
		searchdevcb.onDevicesFind(deviceStatus);
	}

	/**
	 * Share my QRcode to developer.
	 * @param message
	 * @param managedevcb
	 */
	public void successCBShareQrCode(String message, ManageDeviceCallBack managedevcb){
		if (null == managedevcb)
			return;
		managedevcb.onSuccess(message);
	}
	public void failureCBShareQrCode(int code, String message, ManageDeviceCallBack managedevcb) {
		if (null == managedevcb)
			return;
		managedevcb.onFailure(code, message);
	}
	
	/**
	 * Share my QRcode to developer.
	 * @param message
	 * @param managedevcb
	 */
	public void successCBBindDev(String message, ManageDeviceCallBack managedevcb){
		if (null == managedevcb)
			return;
		managedevcb.onSuccess(message);
	}
	public void failureCBBindDev(int code, String message, ManageDeviceCallBack managedevcb) {
		if (null == managedevcb)
			return;
		managedevcb.onFailure(code, message);
	}
	
	/**
	 * Listen to my device.
	 * @param message
	 * @param managedevcb
	 */
	public void successCBCtrlDev(String message, ControlDeviceCallBack ctrldevcb){
		if (null == ctrldevcb)
			return;
		ctrldevcb.onSuccess(message);
	}
	public void failureCBCtrlDev(int code, String message, ControlDeviceCallBack ctrldevcb) {
		if (null == ctrldevcb)
			return;
		ctrldevcb.onFailure(code, message);
	}
	public void onDevStatusReceived(String msgType, String messages, ControlDeviceCallBack ctrldevcb){
		if (null == ctrldevcb)
			return;
		ctrldevcb.onDeviceStatusReceived(msgType, messages);
	}
	
	/**
	 * Local control device.
	 * @param message
	 * @param sscb
	 */
	public void successCBLocalCtrl(String message, SinSocketCallBack sscb){
		if (null == sscb)
			return;
		sscb.onSuccess(message);
	}
	
	public void failureCBLocalCtrl(int code, String message, SinSocketCallBack sscb){
		if (null == sscb)
			return;
		sscb.onFailure(code, message);
	}
	
	public void lostCBLocalCtrl(SinSocketCallBack sscb){
		if (null == sscb)
			return;
		sscb.onLost();
	}
	
	public void msgReadCBLocalCtrl(String message, SinSocketCallBack sscb){
		if (null == sscb)
			return;
		sscb.onMessageRead(message);
	}
}
