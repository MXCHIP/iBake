package com.mico.micoapi;

import com.mxchip.mqttservice2.MqttService;
import com.mxchip.mqttservice2.MqttServiceListener;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

/**
 * 
 * @author Rocke by 2015-08-24
 * 
 */
public class MiCOMQTT implements ServiceConnection {
	private Intent serviceIntent;
//	private Context mContext;
	private MqttService.ServiceBinder binder;
	private MqttServiceListener msl;

	private boolean mqtttag = false;
	private boolean msgtag = false;

	private Handler tcHandler;

	/**
	 * init context
	 * 
	 * @param context
	 */
	// public MiCOMQTT(Context context) {
	// ctx = context;
	// }

	/**
	 * start mqtt service
	 * 
	 * @param host
	 * @param port
	 * @param userName
	 * @param passWord
	 * @param clientID
	 * @param topic
	 */
	public void startMqttService(Context context, String host, String port,
			String userName, String passWord, String clientID, String topic,
			MqttServiceListener mqttServiceListener) {
			msl = mqttServiceListener;
			if (host.equals("") || port.equals("") || userName.equals("")
					|| passWord.equals("") || clientID.equals("")
					|| topic.equals("")) {
				msl.onMqttReceiver("status", "Parameters can not be empty.");
			} else {
				if (!mqtttag) {
					serviceIntent = new Intent(context, MqttService.class);
					serviceIntent.putExtra("com.mxchip.host", host);
					serviceIntent.putExtra("com.mxchip.port", port);
					serviceIntent.putExtra("com.mxchip.userName", userName);
					serviceIntent.putExtra("com.mxchip.passWord", passWord);
					serviceIntent.putExtra("com.mxchip.clientID", clientID);
					serviceIntent.putExtra("com.mxchip.topic", topic);
					context.bindService(serviceIntent, this,
							Context.BIND_AUTO_CREATE);
					mqtttag = true;
				}
			}

			tcHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (1 == msg.what) {
						msl.onMqttReceiver("status", (String) msg.obj);
					} else if (2 == msg.what) {
						msl.onMqttReceiver("payload", (String) msg.obj);
					}
				}
			};
	}

	/**
	 * stop mqtt service
	 */
	public void stopMqttService(Context context) {
		if (mqtttag) {
			context.unbindService(this);
			mqtttag = false;
			msgtag = false;
		}
	}

	/**
	 * send command to device
	 * 
	 * @param topic
	 * @param command
	 * @param qos
	 * @param retained
	 */
	public void publishCommand(String topic, String command, int qos,
			boolean retained) {
		if (mqtttag) {
			binder.publishAPI(topic, command, qos, retained);
		}
	}

	public void stopRecvMessage() {
		if (msgtag && mqtttag) {
			binder.stopRecvMsgAPI();
			msgtag = false;
		}
	}

	public void recvMessage() {
		if (!msgtag && mqtttag) {
			binder.recvMsgAPI();
			msgtag = true;
		}
	}

	public void subscribe(String topic, int qos) {
		if (mqtttag) {
			binder.addSubscribeAPI(topic, qos);
		}
	}

	public void unsubscribe(String topic) {
		if (mqtttag) {
			binder.unSubscribeAPI(topic);
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// Log.d("---api---", "onServiceConnected");
		binder = (MqttService.ServiceBinder) service;
		if (!msgtag) {
			binder.bindListenerAPI(new MqttServiceListener() {

				@Override
				public void onMqttReceiver(String msgType, String messages) {
					// msl.onMqttReceiver(msgType, messages);
					Message msg = new Message();
					msg.obj = messages;
					if (msgType.equals("status")) {
						msg.what = 1;
					} else if (msgType.equals("payload")) {
						msg.what = 2;
					}
					tcHandler.sendMessage(msg);
				}
			});
			msgtag = true;
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		// Log.d("---api---", "onServiceDisconnected");
	}
}
