package com.mxchip.mqttservice2;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MqttService extends Service {
	private ServiceBinder serviceBinder = new ServiceBinder();
	private String LOG_TAG = "---service---";

	private MqttClient client;
	private MqttConnectOptions options;
	private Handler handler;
	private ScheduledExecutorService scheduler;
	private MqttServiceListener mMqttServiceListener = null;
	private Boolean recvTag = false;
	private Boolean connectTag = false;

	private String[] topicList = null;

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(LOG_TAG, "onBind");

		String host = intent.getStringExtra("com.mxchip.host");
		String port = intent.getStringExtra("com.mxchip.port");
		String userName = intent.getStringExtra("com.mxchip.userName");
		String passWord = intent.getStringExtra("com.mxchip.passWord");
		String clientID = intent.getStringExtra("com.mxchip.clientID");
		String topic = intent.getStringExtra("com.mxchip.topic");

		startMqttService(host, port, userName, passWord, clientID, topic);

		return serviceBinder;
	}

	@Override
	public void onCreate() {
		Log.d(LOG_TAG, "onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "onDestroy");
		sendMsgToClient("status", "Stop");
		stopMqttService();

		super.onDestroy();
	}

	/*
	 * ---------------------------------------begin mqtt
	 */

	/**
	 * start mqtt service
	 * @param host
	 * @param port
	 * @param userName
	 * @param password
	 * @param clientID
	 * @param topic
	 */
	public void startMqttService(String host, String port, String userName,
			String password, String clientID, final String topic) {

		mqttInit("tcp://" + host + ":" + port, userName, password, clientID);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {// message arrived
					// Log.d("---ok---", (String) msg.obj);
					sendMsgToClient("payload", (String) msg.obj);
				} else if (msg.what == 2) {// connection
					connectTag = true;
					reSubscribe(topic);
					Log.d(LOG_TAG, "Connected");
					sendMsgToClient("status", "Connected");
				} else if (msg.what == 3) {// connect exception
					Log.d(LOG_TAG, "Connect Exception");
					sendMsgToClient("status", "Connect Exception");
				}
			}
		};
		startReconnect();
	}

	private void reSubscribe(String topic) {
		if (!topic.equals("") && (null == topicList)) {
			try {
				client.subscribe(topic, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			topicList = new String[1];
			topicList[0] = topic;
		} else {
			for (String tp : topicList) {
				try {
					client.subscribe(tp, 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		sendMsgToClient("status", "ReSubscribe success");
	}

	/**
	 * init mqtt
	 * 
	 * @param host
	 * @param userName
	 * @param password
	 * @param clientID
	 */
	private void mqttInit(String host, String userName, String password,
			String clientID) {
		try {
			/*
			 * host client id must different MemoryPersistence is default
			 */
			client = new MqttClient(host, clientID, new MemoryPersistence());
			// MQTT setting
			options = new MqttConnectOptions();
			/*
			 * if clean session false, the service will remember the id of
			 * client true, it will create a new id when it connected
			 */
			options.setCleanSession(true);
			/*
			 * user name
			 */
			options.setUserName(userName);
			/*
			 * password
			 */
			options.setPassword(password.toCharArray());
			/*
			 * overtime 10ms
			 */
			options.setConnectionTimeout(10);
			/*
			 * HeartBeat service will send heart beat once 1.5*20 but it doesn't
			 * reconnect
			 */
			// options.setKeepAliveInterval(20);

			// call back
			client.setCallback(new MqttCallback() {

				@Override
				public void connectionLost(Throwable cause) {
					/**
					 * if it lost, we wil reconnect
					 */
					connectTag = false;
					Log.d(LOG_TAG, "Lost");
					sendMsgToClient("status", "Lost");
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					/*
					 * after publish we will use it
					 */
					sendMsgToClient("status", "Publish success");
				}

				@Override
				public void messageArrived(String topicName, MqttMessage payload)
						throws Exception {
					// after subscribe we will receive
					if (recvTag) {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = "{\"topic\":\"" + topicName
								+ "\",\"payload\":" + payload.toString() + "}";
						handler.sendMessage(msg);
					}
				}
			});
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * subscribe topic
	 * 
	 * @param topic
	 * @param qos
	 */
	public void subscribeService(String topic, int qos) {
		if (topic.equals("")) {
			sendMsgToClient("status", "Topic is empty.");
		} else if (!connectTag) {
			sendMsgToClient("status", "MQTT is disconnect.");
		} else {
			try {
				client.subscribe(topic, qos);

				int ssize = topicList.length;
				String[] tmp = new String[ssize + 1];
				int i = 0;
				for (; i < ssize; i++) {
					tmp[i] = topicList[i];
				}
				tmp[i] = topic;

				topicList = tmp;
			} catch (Exception e) {
				e.printStackTrace();
			}
			sendMsgToClient("status", "Subscribe success");
		}
	}

	/**
	 * cancel subscribe topic
	 * 
	 * @param topic
	 */
	public void unSubscribeService(String topic) {
		if (topic.equals("")) {
			sendMsgToClient("status", "Topic is empty.");
		} else if (!connectTag) {
			sendMsgToClient("status", "MQTT is disconnect.");
		} else {
			try {
				client.unsubscribe(topic);

				int ssize = topicList.length;
				int i = 0, j = 0;
				for (; i < ssize; i++) {
					if (topic != topicList[i]) {
						j++;
					}
				}
				String[] tmp = new String[j];
				i = 0;
				j = 0;
				for (; i < ssize; i++) {
					if (topic != topicList[i]) {
						tmp[j++] = topicList[i];
					}
				}
				topicList = tmp;

			} catch (Exception e) {
				e.printStackTrace();
			}
			sendMsgToClient("status", "UnSubscribe success");
		}
	}

	/**
	 * if the connect is lost, it will reconnect
	 */
	private void startReconnect() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (!client.isConnected()) {
					connect();
				}
			}
		}, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
	}

	/**
	 * connect the mqtt service
	 */
	private void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					client.connect(options);
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} catch (Exception e) {
					// e.printStackTrace();
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	/**
	 * start receive messages
	 * 
	 * @param mqttServiceListener
	 */
	public void initMessageSend(MqttServiceListener mqttServiceListener) {
		mMqttServiceListener = mqttServiceListener;
		recvTag = true;
	}

	public void recvMsgService() {
		recvTag = true;
	}

	/**
	 * stop receive messages
	 */
	public void stopRecvMsgService() {
		recvTag = false;
	}

	/**
	 * publish command to service
	 * 
	 * @param topic
	 * @param command
	 * @param qos
	 * @param retained
	 */
	public void publishService(String topic, String command, int qos,
			boolean retained) {
		try {
			client.publish(topic, command.getBytes(), qos, retained);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * stop mqtt service
	 */
	public void stopMqttService() {
		try {
			scheduler.shutdown();
			client.disconnect();
			connectTag = false;
			recvTag = false;
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	private void sendMsgToClient(String name, String message) {
		if (null != mMqttServiceListener) {
			mMqttServiceListener.onMqttReceiver(name, message);
		}
	}

	/*
	 * ---------------------------------------end mqtt
	 */

	/*
	 * ---------------------------------------begin binder
	 */
	public class ServiceBinder extends android.os.Binder {
		public MqttService getService() {
			return MqttService.this;
		}

		/**
		 * bind listener then we can send message to api
		 * 
		 * @param mqttServiceListener
		 */
		public void bindListenerAPI(MqttServiceListener mqttServiceListener) {
			initMessageSend(mqttServiceListener);
		}

		/**
		 * stop sending message to api
		 */
		public void stopRecvMsgAPI() {
			stopRecvMsgService();
		}

		/**
		 * publish from api
		 * 
		 * @param topic
		 * @param command
		 * @param qos
		 * @param retained
		 */
		public void publishAPI(String topic, String command, int qos,
				boolean retained) {
			publishService(topic, command, qos, retained);
		}

		/**
		 * add subscribe from api
		 * 
		 * @param topic
		 * @param qos
		 */
		public void addSubscribeAPI(String topic, int qos) {
			subscribeService(topic, qos);
		}

		/**
		 * remove one topic of topics by api
		 * 
		 * @param topic
		 */
		public void unSubscribeAPI(String topic) {
			unSubscribeService(topic);
		}

		/***
		 * want receive message again
		 */
		public void recvMsgAPI() {
			recvMsgService();
		}
	}
	/*
	 * --------------------------------------end binder
	 */
}
