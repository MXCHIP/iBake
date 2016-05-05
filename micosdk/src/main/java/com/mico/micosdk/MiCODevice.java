package com.mico.micosdk;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.mico.micoapi.AliMQTT;
import com.mico.micoapi.MiCOEasyLink;
import com.mico.micoapi.MiCOMQTT;
import com.mico.micoapi.MiCOSocket;
import com.mico.micoapi.MiCOmDNS;
import com.mxchip.callbacks.ControlDeviceCallBack;
import com.mxchip.callbacks.EasyLinkCallBack;
import com.mxchip.callbacks.ManageDeviceCallBack;
import com.mxchip.callbacks.SearchDeviceCallBack;
import com.mxchip.callbacks.SinSocketCallBack;
import com.mxchip.callbacks.UserCallBack;
import com.mxchip.fogcloud.HttpSendParam;
import com.mxchip.helper.CommonFunc;
import com.mxchip.helper.Configuration;
import com.mxchip.helper.MiCOConstParam;
import com.mxchip.helper.CreateQRCode;
import com.mxchip.helper.ListenDeviceParams;
import com.mxchip.helper.ScheduleTaskParam;
import com.mxchip.helper.ShareDeviceParams;
import com.mxchip.helper.SinSocketParams;
import com.mxchip.mqttservice2.MqttServiceListener;

/**
 * Manage my devices,
 * Easy Link, mDNS, MQTT, bindDevice unBindDevice, listenDevice
 * Project:MiCOSDK
 * Author:Sin  
 * Create time:
 * @version 1.0
 */
public class MiCODevice {
	private MiCOEasyLink micoeasylink;
	private MiCOmDNS micomdns;
	private MiCOMQTT micomqtt;
	private MiCOSocket micosocket;
	private CommonFunc comfunc;
	private Context mContext = null;
	
	private HttpSendParam hsp = null;

	public MiCODevice(Context context) {
		micoeasylink = new MiCOEasyLink();
		micomdns = new MiCOmDNS();
		micomqtt = new MiCOMQTT();
		comfunc = new CommonFunc();
		hsp = new HttpSendParam();
		mContext = context;
	}

	/**
	 * Get ssid.
	 * @return
	 */
	public String getSSID() {
		if (null != mContext) {
			return micoeasylink.getSSID(mContext);
		}
		return null;
	}

	/**
	 * Start Easy Link.
	 * @param ssid
	 * @param password
	 * @param runSecond
	 * @param easylinkcb
	 */
	public void startEasyLink(String ssid, String password, int runSecond, int sleeptime, EasyLinkCallBack easylinkcb, String extraData) {
		if(0 == runSecond)
			runSecond = 60000 * 10;
		if (comfunc.checkPara(ssid, password) && (runSecond > 0)) {
			if(null != mContext)
				micoeasylink.startEasyLink(mContext, ssid, password, runSecond, sleeptime, easylinkcb, extraData);
			else{
				comfunc.failureCBEasyLink(MiCOConstParam.EMPTYCODE, MiCOConstParam.CONTEXTISNULL,easylinkcb);
			}
		} else {
			if(!(runSecond > 0)){
				comfunc.failureCBEasyLink(MiCOConstParam.EMPTYCODE, MiCOConstParam.RunSecondMax,easylinkcb);
			}else{
				comfunc.failureCBEasyLink(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY,easylinkcb);
			}
		}
	}
	
	/**
	 * Stop Easy Link.
	 * @param easylinkcb
	 */
	public void stopEasyLink(EasyLinkCallBack easylinkcb){
		micoeasylink.stopEasyLink(easylinkcb);
	}
	
	/**
	 * Start mDNS to search my devices.
	 * @param serviceName
	 * @param searchdevcb
	 */
	public void startSearchDevices(String serviceName, SearchDeviceCallBack searchdevcb) {
		if (comfunc.checkPara(serviceName)) {
			if (null != mContext)
				micomdns.startMdnsService(mContext, serviceName, searchdevcb);
			else {
				comfunc.failureCBmDNS(MiCOConstParam.EMPTYCODE, MiCOConstParam.CONTEXTISNULL, searchdevcb);
			}
		} else {
			comfunc.failureCBmDNS(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, searchdevcb);
		}
	}
	
	/**
	 * Stop search devices.
	 * @param searchdevcb
	 */
	public void stopSearchDevices(SearchDeviceCallBack searchdevcb) {
		micomdns.stopMdnsService(searchdevcb);
	}
	
	/**
	 * Share my device to another.
	 * @param deviceid
	 * @param managedevcb
	 * @param token
	 */
	public void getShareVerCode(String deviceid, final ManageDeviceCallBack managedevcb, String token) {
		
		if (comfunc.checkPara(deviceid, token)) {
			try {
				JSONObject postParam = new JSONObject();
				postParam.put("deviceid", deviceid);
				hsp.doHttpPost(Configuration.GETSHAREVERCODE, postParam, new UserCallBack() {

							@Override
							public void onSuccess(String message) {
								try {
									String sharcode = new JSONObject(message).getString("data");
									sharcode = new JSONObject(sharcode).getString("vercode");
									comfunc.successCBShareQrCode(sharcode, managedevcb);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(int code, String message) {
								comfunc.failureCBShareQrCode(code, message.toString(), managedevcb);
							}
						}, token);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} else {
			comfunc.failureCBShareQrCode(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, managedevcb);
		}
	}
	
	public Bitmap creatQrCode(String message, int height, int width) {
		
		if (comfunc.checkPara(message) && (height > 0) && (width > 0)) {
			
			CreateQRCode cqr = new CreateQRCode();
			return cqr.createQRImage(message, height, width);
		} else {
			Log.e("CreatQrCode", MiCOConstParam.EMPTY);
			return null;
		}
	}
	
	public void addDeviceByVerCode(ShareDeviceParams sdevp, final ManageDeviceCallBack managedevcb,String token){
		
		if (comfunc.checkPara(sdevp.bindvercode, sdevp.deviceid, sdevp.bindingtype, token)) {
			
			if (0 < sdevp.role && (sdevp.role < 4)) {
	            try {
	            	JSONObject postParam = new JSONObject();
	                postParam.put("vercode", sdevp.bindvercode);
	                postParam.put("role", sdevp.role);
	                postParam.put("bindingtype", sdevp.bindingtype);
	                postParam.put("iscallback", (true == sdevp.iscallback) ? true : false);
	                postParam.put("deviceid", sdevp.deviceid);
	                postParam.put("devicepw", sdevp.devicepw);
	                
	                hsp.doHttpPost(Configuration.ADDDEVBYVERCODE, postParam,
	                        new UserCallBack() {
								
								@Override
								public void onSuccess(String message) {
									comfunc.successCBShareQrCode(message, managedevcb);
								}
								
								@Override
								public void onFailure(int code, String message) {
									comfunc.failureCBShareQrCode(code, message, managedevcb);
								}
							}, token);
	            } catch (JSONException e1) {
	                e1.printStackTrace();
	            }
			}else{
				comfunc.failureCBShareQrCode(MiCOConstParam.EMPTYCODE, MiCOConstParam.ROLENUM, managedevcb);
			}
		}else{
			comfunc.failureCBShareQrCode(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, managedevcb);
		}
	}
	
	/**
	 * Binding my device from device.
	 */
	public void bindDevice(String ip, final ManageDeviceCallBack managedevcb, String token){
		
		if (comfunc.checkPara(ip, token)){
			getBindVerCodeFromDevice(ip, managedevcb, token);
		}else{
			comfunc.failureCBBindDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, managedevcb);
		}
	}
	
	private void getBindVerCodeFromDevice(String ip, final ManageDeviceCallBack managedevcb, final String token) {
		try {
			JSONObject postParam = new JSONObject();
			postParam.put("getvercode", "");
			hsp.doHttpPost("http://" + ip + ":" + MiCOConstParam.LOCALDEVICEPORT,postParam, new UserCallBack() {

						@Override
						public void onSuccess(String message) {
//							Log.d("---POST---", message);
							if (null != message && !message.equals("")) {
								try {
									JSONObject msgobj = new JSONObject(message);
									String vercode = msgobj.getString("vercode");
									String deviceid = msgobj.getString("deviceid");
									String devicepw = msgobj.getString("devicepw");
									toBindingFogCloud(deviceid, devicepw,vercode, managedevcb, token);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int code, String message) {
//							Log.d("---POST---", code + " " + message);
							comfunc.failureCBBindDev(code, message, managedevcb);
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	//##public --> private
	
	private void toBindingFogCloud(String deviceid, String devicepw,String vercode, 
			final ManageDeviceCallBack managedevcb, String token) {
		
		try {
			JSONObject postParam = new JSONObject();
			postParam.put("deviceid", deviceid);
			postParam.put("devicepw", devicepw);
			postParam.put("vercode", vercode);

			hsp.doHttpPost(Configuration.BINDDEVICE, postParam,
					new UserCallBack() {

						@Override
						public void onSuccess(String message) {
							comfunc.successCBBindDev(message, managedevcb);
						}

						@Override
						public void onFailure(int code, String message) {
							comfunc.failureCBBindDev(code, message, managedevcb);
						}
					}, token);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	public void unBindDevice(String deviceid, final ManageDeviceCallBack managedevcb, String token){
		
		if (comfunc.checkPara(deviceid, token)){
			try {
				JSONObject postParam = new JSONObject();
				postParam.put("deviceid", deviceid);
				hsp.doHttpPut(Configuration.UNBINDDEVICE, postParam, new UserCallBack() {
					
					@Override
					public void onSuccess(String message) {
						comfunc.successCBBindDev(message, managedevcb);
					}
					
					@Override
					public void onFailure(int code, String message) {
						comfunc.failureCBBindDev(code, message, managedevcb);
					}
				}, token);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			comfunc.failureCBBindDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, managedevcb);
		}
	}

	public void updateDeviceAlias(String deviceid, String alias, final ManageDeviceCallBack managedevcb, String token) {
		if (comfunc.checkPara(deviceid, alias, token)) {
			try {
				JSONObject postParam = new JSONObject();
				postParam.put("deviceid", deviceid);
				postParam.put("alias", alias);
				hsp.doHttpPut(Configuration.UPDATEDEVALIAS, postParam,
						new UserCallBack() {

							@Override
							public void onSuccess(String message) {
								comfunc.successCBBindDev(message, managedevcb);
							}

							@Override
							public void onFailure(int code, String message) {
								comfunc.failureCBBindDev(code, message, managedevcb);
							}
						}, token);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			comfunc.failureCBBindDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, managedevcb);
		}
	}
	
	
	/**
	 * Listen to my device, use MQTT service.
	 * @param listendevparams
	 * @param ctrldevcb
	 */
	public void startListenDevice(ListenDeviceParams listendevparams, final ControlDeviceCallBack ctrldevcb) {
		String deviceid = listendevparams.deviceid;
		String host = listendevparams.host;
		String userName = listendevparams.userName;
		String passWord = listendevparams.passWord;
		String clientID = listendevparams.clientID;
		int mqtttype = listendevparams.mqtttype;

		if (comfunc.checkPara(deviceid, host, userName, passWord, clientID)) {
			//使用Fogcloud2.0的MQTT
			if (0 == mqtttype) {
				String port = listendevparams.port;
				if (comfunc.checkPara(port)) {
					String topic = Configuration.getTopic(deviceid);
					if (null != mContext) {
						micomqtt.startMqttService(mContext, host, port, userName, passWord, clientID, topic,
								new MqttServiceListener() {
									@Override
									public void onMqttReceiver(String msgType, String messages) {
										comfunc.onDevStatusReceived(msgType, messages, ctrldevcb);
									}
								});

						comfunc.successCBCtrlDev(MiCOConstParam.SUCCESS, ctrldevcb);
					} else {
						comfunc.failureCBCtrlDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.CONTEXTISNULL, ctrldevcb);
					}
				} else {
					comfunc.failureCBCtrlDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, ctrldevcb);
				}
			}
			//使用阿里云的MQTT
			else if (1 == mqtttype) {
				AliMQTT aliMQTT = new AliMQTT();
				String[] topicFilters = listendevparams.topicFilters;

				if (comfunc.checkPara(topicFilters)) {
					aliMQTT.startAliMqtt(host, userName, passWord, clientID, topicFilters, new MqttServiceListener() {
						@Override
						public void onMqttReceiver(String msgType, String messages) {
							comfunc.onDevStatusReceived(msgType, messages, ctrldevcb);
						}
					});
				}
			}
		} else {
			comfunc.failureCBCtrlDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, ctrldevcb);
		}
	}

	/**
	 *
	 * @param deviceid
	 * @param devicepw
	 * @param command
	 * @param commandType
	 * @param ctrldevcb
	 * @param token
	 */
	public void sendCommand(String deviceid, String devicepw, String command, String commandType, 
			final ControlDeviceCallBack ctrldevcb, String token){
		
		if (comfunc.checkPara(deviceid, devicepw, command, commandType, token)) {
            try {
            	JSONObject postParam = new JSONObject();
                postParam.put("deviceid", deviceid);
                postParam.put("devicepw", devicepw);
                postParam.put("payload", command);
                postParam.put("format", commandType);
                
                hsp.doHttpPost(Configuration.SENDCOMMAND, postParam, new UserCallBack() {
//				hsp.doHttpPost("http://172.28.241.6:444", postParam, new UserCallBack() {

					@Override
					public void onSuccess(String message) {
						comfunc.successCBCtrlDev(message, ctrldevcb);
					}
					
					@Override
					public void onFailure(int code, String message) {
						comfunc.failureCBCtrlDev(code, message, ctrldevcb);
					}
				}, token);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
        	comfunc.failureCBCtrlDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, ctrldevcb);
        }
	}

	public void addDeviceListener(String topic, int qos, ControlDeviceCallBack ctrldevcb) {
		if (comfunc.checkPara(topic)) {
			if (qos < 0 || qos > 2){
				comfunc.failureCBCtrlDev(MiCOConstParam.QOSERRCODE, MiCOConstParam.QOSERR, ctrldevcb);
				return;
			}else{
				micomqtt.subscribe(topic, qos);
				comfunc.successCBCtrlDev(MiCOConstParam.SUCCESS, ctrldevcb);
			}
		}else{
			comfunc.failureCBCtrlDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, ctrldevcb);
		}
	}
	
	public void removeDeviceListener(String topic, ControlDeviceCallBack ctrldevcb) {
		if (comfunc.checkPara(topic)) {
			micomqtt.unsubscribe(topic);
			comfunc.successCBCtrlDev(MiCOConstParam.SUCCESS, ctrldevcb);
		} else {
			comfunc.failureCBCtrlDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, ctrldevcb);
		}
	}
	
	public void stopListenDevice(ControlDeviceCallBack ctrldevcb){
		if(null != mContext){
			micomqtt.stopMqttService(mContext);
			comfunc.successCBCtrlDev(MiCOConstParam.SUCCESS, ctrldevcb);
		}else{
			comfunc.failureCBCtrlDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.CONTEXTISNULL, ctrldevcb);
		}
	}
	
	/**
	 * Control local device.
	 * @param sspara
	 * @param sscb
	 */
	public void connectLocalDevice(SinSocketParams sspara, SinSocketCallBack sscb){
		if(comfunc.checkPara(sspara.ip) && sspara.port>0){
			micosocket = new MiCOSocket(sscb);
			micosocket.connect(sspara);
		}else{
			comfunc.failureCBLocalCtrl(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, sscb);
		}
	}
	
	public void disconnectLocalDevice(SinSocketCallBack sscb){
		micosocket.close();
		comfunc.successCBLocalCtrl(MiCOConstParam.SUCCESS, sscb);
	}
	
	public void sendLocalCommand(String command, SinSocketCallBack sscb){
		if(comfunc.checkPara(command)){
			micosocket.sendMessage(command);
			comfunc.successCBLocalCtrl(MiCOConstParam.SUCCESS, sscb);
		}else{
			comfunc.failureCBLocalCtrl(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, sscb);
		}
	}

	/**
	 * Create schedule take
	 * @param stp
	 * @param ctrldevcb
	 * @param token
	 */
	public void createScheduleTask(ScheduleTaskParam stp, final ControlDeviceCallBack ctrldevcb, String token){

		if (comfunc.checkPara(stp.device_id, stp.order, token)) {
			try {
				JSONObject postParam = new JSONObject();
				postParam.put("task_type", 0);
				postParam.put("device_id", stp.device_id);
				postParam.put("order", stp.order);
				postParam.put("enable", stp.enable);

				postParam.put("month", stp.month);
				postParam.put("day_of_month", stp.day_of_month);
				postParam.put("day_of_week", stp.day_of_week);
				postParam.put("hour", stp.hour);
				postParam.put("minute", stp.minute);

				hsp.doHttpPost(Configuration._SCHEDULETASK, postParam, new UserCallBack() {

					@Override
					public void onSuccess(String message) {
						comfunc.successCBCtrlDev(message, ctrldevcb);
					}

					@Override
					public void onFailure(int code, String message) {
						comfunc.failureCBCtrlDev(code, message, ctrldevcb);
					}
				}, token);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} else {
			comfunc.failureCBCtrlDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, ctrldevcb);
		}
	}

	/**
	 * Create delay take
	 * @param stp
	 * @param ctrldevcb
	 * @param token
	 */
	public void creatDelayTask(ScheduleTaskParam stp, final ControlDeviceCallBack ctrldevcb, String token){

		if (comfunc.checkPara(stp.device_id, stp.order, token)) {
			try {
				JSONObject postParam = new JSONObject();
				postParam.put("task_type", 1);
				postParam.put("device_id", stp.device_id);
				postParam.put("order", stp.order);
				postParam.put("enable", stp.enable);

				postParam.put("second", stp.second);

				hsp.doHttpPost(Configuration._SCHEDULETASK, postParam, new UserCallBack() {

					@Override
					public void onSuccess(String message) {
						comfunc.successCBCtrlDev(message, ctrldevcb);
					}

					@Override
					public void onFailure(int code, String message) {
						comfunc.failureCBCtrlDev(code, message, ctrldevcb);
					}
				}, token);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} else {
			comfunc.failureCBCtrlDev(MiCOConstParam.EMPTYCODE, MiCOConstParam.EMPTY, ctrldevcb);
		}
	}
}
