package com.izhengyin.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketChannelDemo {
	private static final Logger logger = LoggerFactory.getLogger(BufferDemo.class);
	public static void main(String[] args) {
		InetSocketAddress remote = new InetSocketAddress("127.0.0.1", 3002);
		try {
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.connect(remote);
			ByteBuffer buffer = ByteBuffer.allocate(12);
			
			//数据写到缓冲区
			buffer.put("Hello".getBytes());
			BufferUnit.bufferInfo(buffer,"数据写到缓冲区");
			
			//切换到读模式,通过 channel 发送 buffer
		//	buffer.flip();
			//while(buffer.hasRemaining()) {
				//socketChannel.write(buffer);
			//}
			socketChannel.write(buffer);
			BufferUnit.bufferInfo(buffer,"切换到读模式,通过 channel 发送 buffer");
			
			
			//切换到写模式 
			buffer.flip();
			BufferUnit.bufferInfo(buffer,"切换到写模式");
			
			//从 channel 读数据到 buffer
			int readSize = socketChannel.read(buffer);
			BufferUnit.bufferInfo(buffer,"从 channel 读数据到 buffer");
			
			//一直读 ... 
			while(readSize != -1){
				logger.debug("readSize:"+readSize);
				
				//切换到读模式
				buffer.flip();
				BufferUnit.bufferInfo(buffer,"切换到读模式");
				
				//从buffer 读到 bytes 数组
				byte[] bytes = new byte[readSize];
				while(buffer.hasRemaining()){	/** 此处可能会出现 半包 问题  **/
					buffer.get(bytes);
					System.out.println(new String(bytes));
				}
				
				//清空缓冲区
				buffer.clear();
				BufferUnit.bufferInfo(buffer,"清空缓冲区");
				
				//将 bytes 写到Buffer
				buffer.put(bytes);
				BufferUnit.bufferInfo(buffer,"将 bytes 写到Buffer");
				
				//切换到读模式,通过 channel 发送 buffer
				buffer.flip();
				if(buffer.hasRemaining()){
					socketChannel.write(buffer);
				}
				BufferUnit.bufferInfo(buffer,"切换到读模式,通过 channel 发送 buffer");
				
				
				
				buffer.flip();
				readSize = socketChannel.read(buffer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
