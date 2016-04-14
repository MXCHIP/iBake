package com.mxchip.easylink_minus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.mxchip.helper.ProbeReqData;

/**
 * Created by wangchao on 6/9/15.
 */
public class EasyLink_minus {
	private static final String TAG = "EasyLink_minus";
	private Thread mCallbackThread; // call start ap after wifi enabled
	private Context mContext;
	boolean stopSending = false;
	private IntentFilter mIntentFilter = null;
	private boolean mScanning;
	private int mErrorId = 0;

	public boolean isScanning() {
		return mScanning;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context paramAnonymousContext, Intent intent) {
			EasyLink_minus.this.mScanning = false;
			mContext.unregisterReceiver(this);
			Log.d(TAG, "action:" + intent.getAction());
			if (intent.getAction().equals("android.net.wifi.SCAN_RESULTS")) {
				System.out.println("SCAN_RESULTS_AVAILABLE");
				EasyLink_minus.this.mScanning = false;
			}
			if (intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
				try {
					Parcelable localParcelable = intent
							.getParcelableExtra("networkInfo");
					if ((localParcelable != null)
							&& (!((NetworkInfo) localParcelable).isAvailable())) {
						EasyLink_minus.this.mErrorId = 102;
						EasyLink_minus.this.mScanning = false;
						EasyLink_minus.this.clearNetList();
						return;
					}
				} catch (Exception localException2) {
					localException2.printStackTrace();
					return;
				}
			}
			try {
				switch (intent.getIntExtra("wifi_state", 0)) {
				case 0:
					EasyLink_minus.this.clearNetList();
					return;
				}
			} catch (Exception localException1) {
				localException1.printStackTrace();
				return;
			}
			EasyLink_minus.this.mErrorId = 101;
			EasyLink_minus.this.mScanning = false;
			return;
		}
	};

	private List<Integer> mNetId = new ArrayList<Integer>();

	public EasyLink_minus(Context ctx, Thread t) {
		this(ctx);
		mCallbackThread = t;
	}

	public EasyLink_minus(Context ctx) {
		mContext = ctx;
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction("android.net.wifi.SCAN_RESULTS");
		mIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
		mIntentFilter.addAction("android.net.wifi.STATE_CHANGE");
		mContext.registerReceiver(this.mReceiver, this.mIntentFilter);
	}

	// private void clearNetList() {
	// WifiManager localWifiManager = (WifiManager) mContext
	// .getSystemService(Context.WIFI_SERVICE);
	// if (localWifiManager == null
	// || localWifiManager.getConfiguredNetworks() == null) {
	// return;
	// }
	// for (WifiConfiguration localWifiConfiguration : localWifiManager
	// .getConfiguredNetworks()) {
	// String ssid = localWifiConfiguration.SSID.replaceAll("\"", "");
	// if (!ssid.contains("#?1234"))
	// // if (!ssid.matches("^#[0x00-0xff]*@[0x00-0xff]*"))
	// // if (!ssid.matches("^#.*@.*"))
	// continue;
	// localWifiManager.removeNetwork(localWifiConfiguration.networkId);
	// localWifiManager.saveConfiguration();
	// }
	// }

	public void transmitSettings(final String Ssid, final String Key,
			final int ipAddress) {
		new Thread() {
			@Override
			public void run() {
				startTransmit(Ssid, Key, ipAddress);
			}
		}.start();
	}

	public boolean startTransmit(String Ssid, String Key, int ipAddress) {
		Log.d(TAG, new String(Ssid));
		// clearNetList();
		stopSending = false;
		String param[] = null;
		try {
			param = new ProbeReqData().bgProtocol(Ssid, Key, ipAddress);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// if (null == param) {
		// return false;
		// }

		WifiManager localWifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		// if (localWifiManager == null)
		// return false;
		// if (!localWifiManager.isWifiEnabled()) {
		// if (!localWifiManager.setWifiEnabled(true)) {
		// return false;
		// }
		// }
		// WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
		// if (localWifiInfo == null)
		// return false;
		WifiConfiguration config = new WifiConfiguration();
		config.BSSID = null;
		config.preSharedKey = null;
		config.wepKeys = new String[4];
		config.wepTxKeyIndex = 0;
		config.priority = 0;
		config.hiddenSSID = true;
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		config.allowedPairwiseCiphers
				.set(WifiConfiguration.PairwiseCipher.TKIP);
		config.allowedPairwiseCiphers
				.set(WifiConfiguration.PairwiseCipher.CCMP);
		config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

		while (!stopSending) {
			for (int pi = 1; pi < param.length; pi++) {
				config.SSID = String.format("\"%s\"",
						new Object[] { param[pi] });

				localWifiManager.addNetwork(config);
				localWifiManager.saveConfiguration();

				for (WifiConfiguration cfg : localWifiManager
						.getConfiguredNetworks()) {
					if (cfg.SSID.equals(config.SSID)) {
						mNetId.add(cfg.networkId);
					}
				}

				try {
					for (int netId : mNetId) {
						localWifiManager.disableNetwork(netId);
						localWifiManager.enableNetwork(netId, false);
						localWifiManager.startScan();
						Thread.sleep(50L);
					}
				} catch (Exception e) {
				} finally {
					mNetId = new ArrayList<Integer>();
					// clearNetList();
				}
			}
			// sendProbeRequest(localWifiManager, mNetId);
		}
		return true;
	}

	private void sendProbeRequest(WifiManager localWifiManager,
			List<Integer> netIds) {
		try {
			while (!stopSending) {
				for (int netId : netIds) {
					localWifiManager.disableNetwork(netId);
					localWifiManager.enableNetwork(netId, false);
					localWifiManager.startScan();
					Thread.sleep(50L);
				}
			}
		} catch (Exception e) {
		} finally {
			mNetId = new ArrayList<Integer>();
			// clearNetList();
		}
	}

	// If the head of bag is 0, remove it.
	private void clearNetList() {
		WifiManager localWifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		if (localWifiManager == null
				|| localWifiManager.getConfiguredNetworks() == null) {
			return;
		}
		for (WifiConfiguration localWifiConfiguration : localWifiManager
				.getConfiguredNetworks()) {
			String ssid = localWifiConfiguration.SSID.replaceAll("\"", "");

			for (byte bt : ssid.getBytes()) {
				if (bt == 1) {
					localWifiManager
							.removeNetwork(localWifiConfiguration.networkId);
					localWifiManager.saveConfiguration();
				}
			}
		}
	}

	public void stopTransmitting() {
		stopSending = true;
		clearNetList();
	}
}
