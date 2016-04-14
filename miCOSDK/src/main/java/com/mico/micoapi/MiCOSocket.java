package com.mico.micoapi;

import java.util.Timer;

import android.util.Log;

import com.mxchip.callbacks.SinSocketCallBack;
import com.mxchip.helper.CommonFunc;
import com.mxchip.helper.MiCOConstParam;
import com.mxchip.helper.HeartTask;
import com.mxchip.helper.SinSocketParams;
import com.mxchip.micosocket.SinLocalSocket;
import com.mxchip.micosocket.SinSocketListener;

public class MiCOSocket {
	private SinSocketCallBack sscb = null;
	private SinSocketListener micosslisten = null;
	private SinLocalSocket ls = null;
	private Timer t = null;
	private boolean isConnect = false;
	private boolean iflost = false;
	private static int autoconnect = 0;
	
	private CommonFunc comfunc = new CommonFunc();

	private SinSocketParams sspara = null;

	public MiCOSocket(SinSocketCallBack sslisten) {
		this.sscb = sslisten;
		ls = new SinLocalSocket(MiCOConstParam.SOCKETEXCEPTION, MiCOConstParam.CLOSEDCODE, MiCOConstParam.HEARTBEATMSG);
		micosslisten = sda;
	}

	private SinSocketListener sda = new SinSocketListener() {
		public void onHeartBeat(String message) {
			HeartTask.recvTag = true;
			Log.d("---localDevice---", "Heart beat.");
		};

		public void onMessageRead(String message) {
			comfunc.msgReadCBLocalCtrl(message, sscb);
		};

		public void onSuccess() {
			isConnect = true;
			iflost = false;
			autoconnect = sspara.autoConnectNo;
			startHeartBeat();
			comfunc.successCBLocalCtrl(MiCOConstParam.SUCCESS, sscb);
		};
		
		public void onFailure(int code, String message) {
			if(iflost)
				comfunc.lostCBLocalCtrl(sscb);
			else{
				comfunc.failureCBLocalCtrl(code, message, sscb);
				if(message.indexOf("failed to connect to")>-1)
					close();
			}
		};

		public void onLost() {
			comfunc.lostCBLocalCtrl(sscb);
			
			iflost = true;
			stopHeartBeat();

			if(isConnect){
				ls.closeSocket();
				isConnect = false;
			}
			
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(!isConnect && (autoconnect > 0)){
						Log.d("---miCOSocket---", autoconnect+"");
						ls.closeSocket();
						ls.openSocket(sspara.ip, sspara.port, sspara.overTime, micosslisten);
						autoconnect --;
						try {
							Thread.sleep(sspara.overTime + 2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		};
	};

	public void initSocket(){
		if(null != t){
			t.cancel();
			t = null;
		}
		if(isConnect){
			ls.closeSocket();
			isConnect = false;
		}
		iflost = false;
		autoconnect = 0;
	}

	public void connect(SinSocketParams sspara) {
		initSocket();
		if(!isConnect){
			ls.openSocket(sspara.ip, sspara.port, sspara.overTime, micosslisten);
			isConnect = true;
			this.sspara = sspara;
		}
	}
	
	public void close(){
		initSocket();
	}

	public void sendMessage(String message) {
		if(isConnect)
			ls.sendMessage(message);
		else
			comfunc.failureCBLocalCtrl(MiCOConstParam.CLOSEDCODE, MiCOConstParam.CLOSED, sscb);
	}

	private void stopHeartBeat(){
		if(null != t){
			t.cancel();
			t = null;
		}
	}

	private void startHeartBeat() {
		if (null == t) {
			t = new Timer();
			HeartTask mytask = new HeartTask(ls, micosslisten);
			t.schedule(mytask, 5000, sspara.heartBeatTime);
		}
	}
}
