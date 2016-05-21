package com.izhengyin.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSocketChannelDemo {	
	private static final Logger logger = LoggerFactory.getLogger(ServerSocketChannelDemo.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			@SuppressWarnings("resource")
			ServerSocketChannel serverSocket = ServerSocketChannel.open();
			serverSocket.socket().bind(new InetSocketAddress(3002));
		    while(true){
		    		SocketChannel socketChannel =  serverSocket.accept();
		    		System.out.println("clent connected ");
		    		ByteBuffer buffer = ByteBuffer.allocate(12);
		    		//从 channel 读数据到  写到  缓冲区
		    		int readSize = socketChannel.read(buffer);
		    		logger.debug("readSize:"+readSize);
		    		BufferUnit.bufferInfo(buffer);
		    		
		    		//却换到读模式
		    		buffer.flip();
		    		byte[] bytes = new byte[readSize];
		    		buffer.get(bytes);
		    		System.out.println(new String(bytes));
		    		BufferUnit.bufferInfo(buffer);
		    		
		    		//切换到写模式
		    		buffer.clear();
		    		buffer.put("Hi,client!".getBytes());
		    		buffer.flip();
		    		socketChannel.write(buffer);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
