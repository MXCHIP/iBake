package com.mico.micoapi;

import java.net.NetworkInterface;
import java.net.SocketException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.mxchip.callbacks.EasyLinkCallBack;
import com.mxchip.easylink_plus.EasyLink_plus;
import com.mxchip.helper.CommonFunc;
import com.mxchip.helper.MiCOConstParam;

/**
 * Created by wangchao on 4/20/15.
 */
public class MiCOEasyLink {

	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	private EasyLink_plus mEasylinkPlus;
	private boolean eltag = false;
	private Thread workThread = null;
	
	private CommonFunc comfunc = new CommonFunc();

	/**
	 * get ssid
	 * @param context
	 * @return
	 */
	public String getSSID(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
		return mWifiInfo.getSSID().replaceAll("\"", "");
	}

	/**
	 * Start easylink
	 * @param context
	 * @param ssid
	 * @param password
	 * @param runSecond
	 * @param easylinkcb
	 */
	public void startEasyLink(final Context context, String ssid, String password,
							  final int runSecond,
							  final int sleeptime,
							  final EasyLinkCallBack easylinkcb,
							  final String extraData) {
		if (!eltag) {
			if (null == workThread) {
				workThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(runSecond);
							if(eltag)
								stopEasyLink(easylinkcb);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				workThread.start();
			}

			try {
				startEasylink(context, ssid, password, sleeptime, easylinkcb, extraData);
				eltag = true;
				comfunc.successCBEasyLink(MiCOConstParam.SUCCESS, easylinkcb);
			} catch (Exception e) {
				comfunc.failureCBEasyLink(MiCOConstParam.EXCEPTIONCODE, e.getMessage(),easylinkcb);
			}
		} else {
			comfunc.failureCBEasyLink(MiCOConstParam.ISWORKINGCODE, MiCOConstParam.ISWORKING,easylinkcb);
		}
	}

	/**
	 * Stop EasyLink
	 * @param easylinkcb
	 */
	public void stopEasyLink(EasyLinkCallBack easylinkcb) {
		if (null != mEasylinkPlus && eltag) {
			if (null != workThread) {
				workThread = null;
			}
			mEasylinkPlus.stopTransmitting();
			eltag = false;
			comfunc.successCBEasyLink(MiCOConstParam.STOP_SUCCESS,easylinkcb);
		} else {
			comfunc.failureCBEasyLink(MiCOConstParam.ALREADYSTOPCODE, MiCOConstParam.ALREADYSTOP,easylinkcb);
		}
	}

	protected void startEasylink(Context context, String ssid, String password, int sleeptime, EasyLinkCallBack easylinkcb, String extraData) {
		// v2 and v3
		int ip = getNormalIP(context);
		mEasylinkPlus = EasyLink_plus.getInstence(context);
		try {
			NetworkInterface intf = NetworkInterface.getByName("wlan0");
			if (intf.getMTU() < 1500) {
				mEasylinkPlus.setSmallMtu(true);
			}
		} catch (SocketException e) {
			e.printStackTrace();
			comfunc.failureCBEasyLink(MiCOConstParam.EXCEPTIONCODE, e.getMessage(),easylinkcb);
		}
		try {
			mEasylinkPlus.transmitSettings(ssid, password, ip, sleeptime, extraData);
		} catch (Exception e) {
			e.printStackTrace();
			comfunc.failureCBEasyLink(MiCOConstParam.EXCEPTIONCODE, e.getMessage(),easylinkcb);
		}
	}

	private int getNormalIP(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
		return mWifiInfo.getIpAddress();
	}
}
