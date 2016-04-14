package com.mxchip.fogcloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class LocalDevice {
	
	PrintWriter writer;
	BufferedReader reader;
	
	private LocalDevice(){}
	private static final LocalDevice instance = new LocalDevice();
	public static LocalDevice getInstance(){
		return instance;
	}
	
	public void TcpClient(final String ip, final int port) {

		new Thread() {
			@Override
			public void run() {
				try {
					Log.d("---localDevice---", ip+" "+port);
					// 建立连接到远程服务器的socket
					Socket socket = new Socket(ip, port);
					
					writer = new PrintWriter(
							new OutputStreamWriter(
									socket.getOutputStream()));
					reader = new BufferedReader(
							new InputStreamReader(
									socket.getInputStream()));
					
					AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>(){

						@Override
						protected Void doInBackground(Void... arg0) {
							String line;
							try {
								while((line = reader.readLine()) != null){
									publishProgress(line);
								}
								writer.close();
								reader.close();
								writer = null;
								reader = null;
							} catch (IOException e) {
								e.printStackTrace();
							}
							return null;
						}
						
						@Override
						protected void onProgressUpdate(String... values) {
							Log.d("---localDevice---", "From server-->" + values[0]);
							super.onProgressUpdate(values);
						}
					};
					read.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void sendMessage(String message){
		if(writer != null){
			writer.write(message+"\n");
			writer.flush();
		}else{
			Log.d("---localDevice---", "连接已经断开");
		}
	}
	
}
