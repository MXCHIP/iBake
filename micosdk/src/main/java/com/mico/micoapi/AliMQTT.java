package com.mico.micoapi;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mxchip.helper.MacSignature;
import com.mxchip.mqttservice2.MqttServiceListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Rocke on 2016/05/05.
 */
public class AliMQTT {

    private String TAG = "---AliMqttHelper---";
    MqttClient sampleClient;
    private Handler tcHandler =  new MyHandler();
    private Runnable mRunnable;
    private MqttServiceListener mMqttServiceListener = null;

    public void startAliMqtt(final String broker, final String acessKey, final String secretKey,
                             final String clientId,
                             final String[] topicFilters,
                             MqttServiceListener mqttServiceListener) {

        mMqttServiceListener = mqttServiceListener;
        mRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    MemoryPersistence persistence = new MemoryPersistence();
                    sampleClient = new MqttClient(broker, clientId, persistence);
                    final MqttConnectOptions connOpts = new MqttConnectOptions();
                    Log.d(TAG, "Connecting to broker: " + broker);
                    String sign = MacSignature.macSignature(clientId.split("@@@")[0], secretKey);
                    Log.d(TAG, "sign: " + sign);
                    connOpts.setUserName(acessKey);
                    connOpts.setServerURIs(new String[]{broker});
                    connOpts.setPassword(sign.toCharArray());
                    connOpts.setCleanSession(true);
                    // connOpts.setCleanSession(false);
                    /** HeartBeat service will send heart beat once 1.5*20 but it doesn't reconnect*/
                    // connOpts.setKeepAliveInterval(100);
                    sampleClient.setCallback(new MqttCallback() {
                        public void connectionLost(Throwable throwable) {
                            while (true) {
                                try {
                                    sendMsgToClient("status", "Lost");
                                    throwable.printStackTrace();
                                    Thread.sleep(1000L);

                                    sampleClient.connect(connOpts);
                                    sendMsgToClient("status", "Reconnected");

                                    sampleClient.subscribe(topicFilters);
                                    sendMsgToClient("status", "Connected");

                                    break;
                                } catch (MqttSecurityException e) {
                                    // TODO Auto-generated catch block
                                    sendMsgToClient("status", "Connect Exception");

                                    e.printStackTrace();
                                } catch (MqttException e) {
                                    // TODO Auto-generated catch block
                                    sendMsgToClient("status", "Connect Exception");

                                    e.printStackTrace();

                                    if (e.getReasonCode() == MqttException.REASON_CODE_CLIENT_CONNECTED) {
                                        break;
                                    }
                                } catch (Exception e) {
                                    sendMsgToClient("status", "Connect Exception");
                                    e.printStackTrace();
                                }
                            }
                        }

                        public void messageArrived(String topic, MqttMessage payload) throws Exception {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = "{\"topic\":\"" + topic + "\",\"payload\":" + payload.toString() + "}";
                            tcHandler.sendMessage(msg);
                        }

                        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                            // Log.d(TAG, "deliveryComplete:" + iMqttDeliveryToken.getMessageId());
                            sendMsgToClient("status", "Publish success");
                        }
                    });

                    sampleClient.connect(connOpts);
                    sampleClient.subscribe(topicFilters);
                    sendMsgToClient("status", "Connected");
                } catch (Exception me) {
                    me.printStackTrace();
                }
            }
        };

        mRunnable.run();
    }

    public void stopAliMqtt(){
        try {
            sampleClient.disconnect();
            tcHandler.removeCallbacks(mRunnable);
            sendMsgToClient("status", "Stop");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void sendMsgToClient(String name, String message) {
        if (null != mMqttServiceListener) {
            mMqttServiceListener.onMqttReceiver(name, message);
        }
    }

    class MyHandler extends Handler{

        @Override
        public void handleMessage(Message msg)
        {
            // TODO Auto-generated method stub
            if (1 == msg.what) {
                mMqttServiceListener.onMqttReceiver("status", (String) msg.obj);
            } else if (2 == msg.what) {
                mMqttServiceListener.onMqttReceiver("payload", (String) msg.obj);
            }
        }
    }
}
