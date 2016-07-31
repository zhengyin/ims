package com.izhengyin.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author zhengyin < http://izhengyin.com >
 */
public class NioServerDemo {	
	private static final Logger logger = LoggerFactory.getLogger(NioServerDemo.class);
	public static void main(String[] args) throws IOException {
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		
		try{
			serverSocket.socket().bind(new InetSocketAddress(3002));
		    while(true){
		    	SocketChannel socketChannel =  serverSocket.accept();
		    	new ServerSocketChannelHandler(socketChannel).start();
		    }
		}finally{
			serverSocket.close();
		}
	}
	
	
	private static class ServerSocketChannelHandler extends Thread{
		SocketChannel socketChannel;
		SocketAddress remoteSocketAddress ;
		public ServerSocketChannelHandler(SocketChannel socketChannel) throws IOException {
			
			this.socketChannel = socketChannel;
			this.remoteSocketAddress = socketChannel.socket().getRemoteSocketAddress();
			logger.info("client connected ,"+remoteSocketAddress);
		}
		@Override
		public void run() {
			ByteBuffer buffer = ByteBuffer.allocate(12);
    		//从 channel 读数据到  写到  缓冲区
			try {
				int readSize = socketChannel.read(buffer);	//阻塞读 ~
				while(readSize != -1){
	    			buffer.flip();
	    			byte[] bytes = new byte[readSize];
	    			buffer.get(bytes);
	    			String msg = new String(bytes);
	    			logger.info("receiver msg:"+msg);
	    			if(msg.equals("Hi,Server")){	//客户端第一次讯问
	    				buffer.clear();
	    				//响应客户端消息
	    				buffer.put("Hi,Client".getBytes());	
	    				buffer.flip();
	    				if(buffer.hasRemaining()){
							socketChannel.write(buffer);
						}
	    				logger.info("send msg:Hi,Client");
	    			}else if(msg.equals("I am here")){	//客户端 响应  Hi,Client 的应答消息
	    				
	    				buffer.clear();
	    				//响应客户端消息
	    				buffer.put("ok".getBytes());	
	    				buffer.flip();
	    				if(buffer.hasRemaining()){
							socketChannel.write(buffer);
						}
	    				logger.info("send msg:ok");
	    				logger.info("应答完成");
	    			}else{
	    				logger.debug("未知的消息:"+msg);
	    			}
	    			
	    			buffer.clear();
	    			readSize = socketChannel.read(buffer);	//阻塞读 ~
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					socketChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			logger.info("client colsed , "+remoteSocketAddress);
		}
	}
	
}
