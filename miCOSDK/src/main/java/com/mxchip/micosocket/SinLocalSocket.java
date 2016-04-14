package com.mxchip.micosocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.os.AsyncTask;
import android.util.Log;

public class SinLocalSocket {

	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private Socket socket = null;
	private SinSocketListener slssslisten = null;
	
	private int exceptioncode = 0;
	private int closedcode = 0;
	private String heartbeatmsg = null;

	public SinLocalSocket(int exceptioncode, int closedcode,String heartbeatmsg){
		this.exceptioncode = exceptioncode;
		this.closedcode = closedcode;
		this.heartbeatmsg = heartbeatmsg;
	}
	
	public void openSocket(final String host, final int port, final int overTime,
			final SinSocketListener sslisten) {
		slssslisten = sslisten;

		new Thread() {
			@Override
			public void run() {
				if (null == socket) {
					try {
						// 建立连接到远程服务器的socket
						socket = new Socket();
						SocketAddress address = new InetSocketAddress(host, port);
						socket.connect(address, overTime);
						
						writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
						reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
						sslisten.onSuccess();

						AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {

							@Override
							protected Void doInBackground(Void... arg0) {
								try {
									if(reader != null){
										String line;
										while ((reader != null)&&(line = reader.readLine()) != null) {
											publishProgress(line);
										}
										reader.close();
										reader = null;
									}
									if(writer != null){
										writer.close();
										writer = null;
									}
								} catch (IOException e) {
//									e.printStackTrace();
								}
								return null;
							}

							@Override
							protected void onProgressUpdate(String... values) {
								if (values[0].equals(heartbeatmsg)) {
									sslisten.onHeartBeat(values[0]);
								} else {
									sslisten.onMessageRead(values[0]);
								}
							}
							
							protected void onPostExecute(Void result) {
								if(null != socket)
									sslisten.onLost();
							};
						};
						read.execute();
					} catch (Exception e) {
						sslisten.onFailure(exceptioncode, e.getMessage());
						closeSocket();
//						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void sendMessage(String message) {
		if (writer != null) {
			writer.write(message);
			writer.flush();
		} else {
			if(null != slssslisten)
				slssslisten.onFailure(closedcode, "");
		}
	}

	public void closeSocket() {
		if (null != socket) {
			try {
				socket.close();
				socket = null;
//				if(writer != null){
//					writer.close();
//					writer = null;
//				}
//				if(reader != null){
//					reader.close();
//					reader = null;
//				}
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}
	}
}
