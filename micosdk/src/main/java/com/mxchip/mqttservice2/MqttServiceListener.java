package com.mxchip.mqttservice2;

public interface MqttServiceListener {
	public void onMqttReceiver(String msgType, String messages);
}
