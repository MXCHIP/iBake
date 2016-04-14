package com.mico.micoapi;

import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;

import com.mxchip.callbacks.SearchDeviceCallBack;
import com.mxchip.helper.CommonFunc;
import com.mxchip.helper.MiCOConstParam;
import com.mxchip.utils.EasyLinkTXTRecordUtil;

public class MiCOmDNS {
	private JmDNS jmdns = null;
	public InetAddress intf = null;
	private WifiManager wm = null;
	private MulticastLock lock = null;
	private SampleListener sl = null;
	private ServiceInfo sName = null;
	private String mFindDeviceJsonString;
	private JSONArray array;
	private String arraytmp = "";
	private boolean isStarted = false;

	private CommonFunc comfunc = new CommonFunc();

	public void startMdnsService(final Context context, final String serviceName, final SearchDeviceCallBack searchdevcb) {
		if (!isStarted) {
			isStarted = true;
			array = new JSONArray();
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (isStarted) {
							if (intf != null && jmdns != null) {
								startDeviceSearch(context, serviceName,
										searchdevcb);
								Thread.sleep(1000 * 3);
							} else {
								if (intf == null) {
									intf = getLocalIpAddress(context);
								}

								if (jmdns == null) {
									jmdns = JmDNS.create(intf);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();
			comfunc.successCBmDNS(MiCOConstParam.SUCCESS, searchdevcb);
		}else{
			comfunc.failureCBmDNS(MiCOConstParam.ISWORKINGCODE, MiCOConstParam.ISWORKING, searchdevcb);
		}
	}

	public InetAddress getLocalIpAddress(Context context) throws Exception {
		if (wm == null)
			wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo = wm.getConnectionInfo();
		int intaddr = wifiinfo.getIpAddress();
		byte[] byteaddr = new byte[] { (byte) (intaddr & 0xff),
				(byte) (intaddr >> 8 & 0xff), (byte) (intaddr >> 16 & 0xff),
				(byte) (intaddr >> 24 & 0xff) };
		InetAddress addr = InetAddress.getByAddress(byteaddr);
		return addr;
	}

	private void startDeviceSearch(Context context, String serviceName, SearchDeviceCallBack searchdevcb) {
		try {
			if (wm == null)
				wm = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);

			lock = wm.createMulticastLock("mylock");
			lock.setReferenceCounted(true);
			lock.acquire();
			sl = new SampleListener(serviceName, searchdevcb);
			jmdns.addServiceListener(serviceName, sl);
			comfunc.onDevsFindmDNS(array, searchdevcb);
			array = new JSONArray();
			arraytmp = "";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class SampleListener implements ServiceListener, ServiceTypeListener {
		String serviceName;

		public SampleListener(String serviceName,
				SearchDeviceCallBack searchdevcb) {
			this.serviceName = serviceName;
		}

		/**
		 * Add four arguments FogProductId //product id IsEasylinkOK //60ms
		 * belong finish easy link action IsHaveSuperUser //is binded by
		 * administrator RemainingUserNumber //can be connected by user (max
		 * number)
		 */
		public void serviceAdded(ServiceEvent event) {
			sName = jmdns.getServiceInfo(serviceName, event.getName());
			if (null != sName) {
				// String Protocol = EasyLinkTXTRecordUtil.setProtocol(""+
				// sName.getTextString());
				String macbind = EasyLinkTXTRecordUtil.setBinding("" + sName.getTextString());
				String hardwareID = EasyLinkTXTRecordUtil.sethardwareID("" + sName.getTextString());
				String ipString = EasyLinkTXTRecordUtil.setDeviceIP("" + sName.getAddress());
				String macString = EasyLinkTXTRecordUtil.setDeviceMac("" + sName.getTextString());
				String allinfo = EasyLinkTXTRecordUtil.setAllInfo("" + sName.getTextString());

				String fogProductID = EasyLinkTXTRecordUtil.setFogProductID("" + sName.getTextString());
				String isEasyLinkOK = EasyLinkTXTRecordUtil.setIsEasylinkOK("" + sName.getTextString());
				String isHaveSuperUser = EasyLinkTXTRecordUtil.setIsHaveSuperUser("" + sName.getTextString());
				String remainingUserNumber = EasyLinkTXTRecordUtil.setRemainingUserNumber("" + sName.getTextString());

				mFindDeviceJsonString = "{\"deviceName\":\"" + sName.getName()
						+ "\",\"deviceMac\":\"" + macString
						+ "\",\"deviceIP\":\"" + ipString
						+ "\",\"deviceMacbind\":\"" + macbind
						+ "\",\"hardwareID\":\"" + hardwareID

						+ "\",\"fogProductID\":\"" + fogProductID
						+ "\",\"isEasyLinkOK\":\"" + isEasyLinkOK
						+ "\",\"isHaveSuperUser\":\"" + isHaveSuperUser
						+ "\",\"remainingUserNumber\":\"" + remainingUserNumber

						+ "\",\"allInfo\":\"" + allinfo
						+ "\",\"devicePort\":\"" + sName.getPort() + "\"}";
				try {
					if (-1 == arraytmp.indexOf(macString)) {
						JSONObject city = new JSONObject(mFindDeviceJsonString);
						array.put(city);
						arraytmp += macString + ",";
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		public void serviceRemoved(ServiceEvent event) {
		}

		public void serviceResolved(ServiceEvent event) {
		}

		public void serviceTypeAdded(ServiceEvent event) {
		}
	}

	public void stopMdnsService(SearchDeviceCallBack searchdevcb) {
		if (isStarted) {
			isStarted = false;
			comfunc.successCBmDNS(MiCOConstParam.STOP_SUCCESS, searchdevcb);
		}else{
			comfunc.failureCBmDNS(MiCOConstParam.ALREADYSTOPCODE, MiCOConstParam.ALREADYSTOP,searchdevcb);
		}
	}

}
