package com.izhengyin.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author zhengyin < http://izhengyin.com >
 */
public class NioClientDemo {
	private static final Logger logger = LoggerFactory.getLogger(NioClientDemo.class);
	private static SocketChannel socketChannel;
	
	public static void main(String[] args) throws IOException {

		
		InetSocketAddress remote = new InetSocketAddress("127.0.0.1", 3002);
		try {
			socketChannel = SocketChannel.open();
			socketChannel.connect(remote);
			
			//1=>数据写到缓冲区 	
			ByteBuffer buffer = ByteBuffer.allocate(12);
			buffer.put("Hi,Server".getBytes());
			BufferUnit.bufferInfo(buffer,"1=>开辟一个12Byte的缓冲区，并将  Hello 写到缓冲区");
			
			//2=>切换到读模式,通过 channel 发送 buffer  ,注： 此时 posttion 被定位到了 数据写入位置，  socketChannel.write 并不会自动的把 position 置为可读数据的初始位置
			buffer.flip();
			while(buffer.hasRemaining()) {
				socketChannel.write(buffer);
			}
			BufferUnit.bufferInfo(buffer,"2=>切换到读模式,通过 channel 发送 buffer");
			
			
			//3=>清空缓冲区,从 channel 读数据到 buffer 注：此时buffer 可写limit大小写为，buffer的写入大小,此例中被至为:5 ,故调用 clear 而不是  flip
			buffer.clear();
			int readSize = socketChannel.read(buffer);
			BufferUnit.bufferInfo(buffer,"3=>清空缓冲区,从 channel 读数据到 buffer,readSize:"+readSize);
			
			
			
			//一直读 ... 
			while(readSize != -1){
				
				
				
				//4=>切换到读模式	，从buffer 读到 bytes 数组
				buffer.flip();
				byte[] bytes = new byte[readSize];
				buffer.get(bytes);
				String msg = new String(bytes);
				BufferUnit.bufferInfo(buffer,"4=>切换到读模式，从buffer 读到 bytes 数组");
				
				
				logger.info("receiver msg:"+msg);
				if(msg.equals("Hi,Client")){		//服务端响应  Hi,Server 的应答消息
					//5=>清空缓冲区
					buffer.clear();
					buffer.put("I am here".getBytes());
					BufferUnit.bufferInfo(buffer,"5=>清空缓冲区，将 bytes 写到Buffer");
					
					//6=>切换到读模式,通过 channel 发送 buffer
					buffer.flip();
					if(buffer.hasRemaining()){
						socketChannel.write(buffer);
					}
					logger.info("send msg:I am here");
					BufferUnit.bufferInfo(buffer,"6=>切换到读模式,通过 channel 发送 buffer");
				}else if(msg.equals("ok")){
					logger.info("应答完成,主动关闭连接");
					break;
				}else{
					logger.debug("未知的消息："+msg);
				}
				
				//7=>清空缓冲区,从 channel 读数据到 buffer
				buffer.clear();
				readSize = socketChannel.read(buffer);
				BufferUnit.bufferInfo(buffer,"7=>清空缓冲区,从 channel 读数据到 buffer,readSize:"+readSize);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			socketChannel.close();
		}
		
	}

	
}
