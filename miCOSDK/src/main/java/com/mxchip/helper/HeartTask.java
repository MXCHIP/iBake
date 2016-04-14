package com.mxchip.helper;

import java.util.TimerTask;

import com.mxchip.micosocket.SinLocalSocket;
import com.mxchip.micosocket.SinSocketListener;

public class HeartTask extends TimerTask {

	public static boolean sendTag = false;
	public static boolean recvTag = false;

	private SinLocalSocket ls;
	private SinSocketListener micol = null;

	private int heartcount = 0;

	public HeartTask(SinLocalSocket ls, SinSocketListener micol) {
		this.ls = ls;
		this.micol = micol;
	}

	@Override
	public void run() {
		ls.sendMessage(MiCOConstParam.HEARTBEATMSG);
		recvTag = false;
		sendTag = true;

		new Thread() {
			public void run() {
				try {
					Thread.sleep(MiCOConstParam.HEARTBEADSLEEP);
					if (!recvTag) {
						heartcount++;
						if (heartcount > MiCOConstParam.HEARTBEADTIMES)
							micol.onLost();
					} else {
						heartcount = 0;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}