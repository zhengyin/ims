package com.izhengyin.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(3002);
			Socket socket =  serverSocket.accept();
			InputStream in = socket.getInputStream();
		    OutputStream out = socket.getOutputStream();
		    char c ;
		    while(true){
		    		while((c = (char) in.read()) != -1){
		    			System.out.println("read:"+c);
		    			out.write((byte)c);
		    		}
		    }
		}catch(Exception e){
			
		}
		
	}

}
