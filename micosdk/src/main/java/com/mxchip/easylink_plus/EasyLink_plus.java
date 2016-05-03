/**
 * 
 */
package com.mxchip.easylink_plus;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

import com.mxchip.easylink_minus.EasyLink_minus;
import com.mxchip.easylink_v2.EasyLink_v2;
import com.mxchip.easylink_v3.EasyLink_v3;
import com.mxchip.helper.Helper;

/**
 * @author Perry
 * 
 * @date 2014-10-21
 */
public class EasyLink_plus {
	private static EasyLink_v2 e2;
	private static EasyLink_v3 e3;
	// private static EasyLink_minus minus;
	private static EasyLink_plus me;
	boolean sending = true;
	ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

	private EasyLink_plus(Context ctx) {
		try {
			e2 = EasyLink_v2.getInstence();
			e3 = EasyLink_v3.getInstence();
			// minus = new EasyLink_minus(ctx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static EasyLink_plus getInstence(Context ctx) {
		if (me == null) {
			me = new EasyLink_plus(ctx);
		}
		return me;
	}

	public void setSmallMtu(boolean onoff) {
		e3.SetSmallMTU(onoff);
	}

	public void transmitSettings(final String ssid, final String key, final int ipAddress, final int sleeptime, String extraData) {
		try {
			//TODO 不包含额外参数
//			final byte[] ssid_byte = ssid.getBytes("UTF-8");
//			final byte[] key_byte = key.getBytes("UTF-8");
////			int userinfoLen = 6 + extraData.getBytes().length;
//
////			final byte[] userinfo = new byte[userinfoLen];
//			final byte[] byteip = new byte[5];
//			byteip[0] = 0x23; // #
//			String strIP = String.format("%08x", ipAddress);
//			System.arraycopy(Helper.hexStringToBytes(strIP), 0, byteip, 1, 4);
////			if (!"".equals(extraData) || (null != extraData)) {
////				userinfo[5] = 0x23; // #
////				System.arraycopy(extraData.getBytes(), 0, userinfo, 6, extraData.getBytes().length);
////			}

			//TODO 包含额外参数
			final byte[] ssid_byte = ssid.getBytes("UTF-8");
			final byte[] key_byte = key.getBytes("UTF-8");
			int userinfoLen = 5 + extraData.getBytes().length;

			final byte[] userinfo = new byte[userinfoLen];
			userinfo[0] = 0x23; // #
			String strIP = String.format("%08x", ipAddress);
			System.arraycopy(Helper.hexStringToBytes(strIP), 0, userinfo, 1, 4);
			if (!"".equals(extraData) || (null != extraData)) {
				System.arraycopy(extraData.getBytes(), 0, userinfo, 0, extraData.getBytes().length);
				userinfo[extraData.getBytes().length] = 0x23; // #
				System.arraycopy(Helper.hexStringToBytes(strIP), 0, userinfo, extraData.getBytes().length+1, 4);
			}

			singleThreadExecutor = Executors.newSingleThreadExecutor();
			sending = true;
			singleThreadExecutor.execute(new Runnable() {
				@Override
				public void run() {
					while (sending) {
						try {
							// minus.transmitSettings(ssid, key, ipAddress);
							// Log.e("easylink", "START!!!!");
							e2.transmitSettings(ssid_byte, key_byte, userinfo, sleeptime);
							e3.transmitSettings(ssid_byte, key_byte, userinfo, sleeptime);
							try {
								Thread.sleep(10 * 1000);
								e2.stopTransmitting();
								e3.stopTransmitting();
								// minus.stopTransmitting();
								// Log.e("easylink", "STOP!!!!");
								Thread.sleep(10 * 1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void stopTransmitting() {
		// Log.e("easylink", "STOP!!!!");
		sending = false;
		singleThreadExecutor.shutdown();
		e2.stopTransmitting();
		e3.stopTransmitting();
		// minus.stopTransmitting();
	}
}
