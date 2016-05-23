package com.izhengyin.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class AioClientDemo {

	private static final Logger logger = LoggerFactory.getLogger(AioClientDemo.class);
	private static AsynchronousSocketChannel socketChannel;
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		socketChannel = AsynchronousSocketChannel.open();
		InetSocketAddress remote = new InetSocketAddress("127.0.0.1", 3002);
		Future<Void> connectResult =  socketChannel.connect(remote);
		if(connectResult.get() != null){
			logger.info("connect fail!");	
			return;
		}
		logger.info("connect ok!");	
		
		ByteBuffer buffer = ByteBuffer.allocate(128);
		
		buffer.put("Hi,Server".getBytes());
		buffer.flip();
		socketChannel.write(buffer);
		buffer.clear();
		
		Future<Integer> readResult = socketChannel.read(buffer);
		int readSize = readResult.get();	//阻塞
		while (socketChannel.isOpen()) {
			if(readSize > 0){	//读到数据
				String msg = read(readSize, buffer);
				if(msg.equals("Hi,Client")){		//服务端响应  Hi,Server 的应答消息
					write("I am here");
				}else if(msg.equals("ok")){
					logger.info("应答完成,主动关闭连接");
					socketChannel.close();
				}else{
					logger.debug("未知的消息："+msg);
				}
			}else if(readSize < 0){	//对端链路关闭
				socketChannel.close();
				logger.info("对端链路关闭");
			}else{	//空数据
				logger.info("空数据");
			}
			//接着读
			if(socketChannel.isOpen()){
				logger.debug("接着读");
				buffer.clear();
				readResult = socketChannel.read(buffer);//异步
				
				// ... 可以干点别的
				
				readSize = readResult.get();		//阻塞
			}
			
		}
		logger.debug("---- 程序终止  ----");
	}
	
	private static String read(int readSize,ByteBuffer buffer){
		byte[] bytes = new byte[readSize];
		buffer.flip();
		buffer.get(bytes);
		String msg = new String(bytes);
		logger.info("received msg:"+msg);
		return msg;
	}
	
	private static void write(String msg){
		byte[] bytes = msg.getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		socketChannel.write(buffer);
		buffer.clear();
		logger.info("send msg:"+msg);
	}
}
