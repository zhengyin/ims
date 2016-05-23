package com.izhengyin.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AioServerDemo {
	
	private static final Logger logger = LoggerFactory.getLogger(AioServerDemo.class);
	
	public static void main(String[] args) throws IOException {
		AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(), 3);
		AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open(group);
		serverChannel.bind(new InetSocketAddress(3002));
		logger.debug("bind --- ");
		ListenerHandler listenerHandler = new ListenerHandler(serverChannel);
		listenerHandler.start();
	}
	
	public static class ListenerHandler extends Thread{
		private AsynchronousServerSocketChannel serverChannel;
		private boolean bStop = false;
		public ListenerHandler(AsynchronousServerSocketChannel serverChannel) {
				this.serverChannel = serverChannel;
		}
		@Override
		public void run() {
			logger.info("listener start ---");
			doAccept(this, new AcceptCompileHandler());
			
			try {
				//因为 doAccept 为非阻塞的，挂起线程，防止线程退出
				while (!bStop) {
					sleep(3000);
					logger.debug(" sleep ---");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if(this.serverChannel.isOpen()){
					try {
						this.serverChannel.close();
						logger.info("this server channel  closed ---");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			logger.info("listener end ---");
		}
		
		public void doAccept(ListenerHandler listenerHandler,AcceptCompileHandler acceptCompileHandler){
			this.serverChannel.accept(listenerHandler, acceptCompileHandler);	//异步非阻塞
		}
		/**
		 * @param bStop the bStop to set
		 */
		public void setbStop(boolean bStop) {
			this.bStop = bStop;
		}
		
		
	}
	
	
	private static class AcceptCompileHandler implements CompletionHandler<AsynchronousSocketChannel, ListenerHandler> {

		public void completed(AsynchronousSocketChannel socketChannel, ListenerHandler listenerHandler) {
			// TODO Auto-generated method stub
			try {
				logger.info("client connect , "+socketChannel.getRemoteAddress()); //新的客户端连接
				listenerHandler.doAccept(listenerHandler, this);	//继续监听客户端连接 
				//读数据
				ByteBuffer buffer = ByteBuffer.allocate(128);
				socketChannel.read(buffer, buffer, new ReadCompileHandler(socketChannel));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

		public void failed(Throwable exc, ListenerHandler listenerHandler) {
			// TODO Auto-generated method stub
			logger.debug("AcceptCompileHandler failed ---");
			exc.printStackTrace();
			listenerHandler.setbStop(true);
		}
		
	}
	
	
	private static class  ReadCompileHandler implements CompletionHandler<Integer, ByteBuffer> {
		private AsynchronousSocketChannel socketChannel;
		public ReadCompileHandler(AsynchronousSocketChannel socketChannel) {
			this.socketChannel = socketChannel;
		}
		
		public void completed(Integer readSize, ByteBuffer buffer) {
			
			logger.debug("read completed , readSize:"+readSize);
			if(readSize>0){
				String msg = read(readSize, buffer);
				if(msg.equals("Hi,Server")){	//客户端第一次讯问
					write("Hi,Client");
				}else if(msg.equals("I am here")){	//客户端 响应  Hi,Client 的应答消息
					write("ok");
					logger.info("应答完成");
				}else{
					logger.debug("未知的消息:"+msg);
				}
			}else if(readSize<0){
				try {
					socketChannel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.debug("对端链路关闭");
			}else{
				logger.debug("空数据包");
			}
			//接着读
			if(socketChannel.isOpen()){
				logger.debug("接着读");
				buffer.clear();
				socketChannel.read(buffer, buffer, new ReadCompileHandler(socketChannel));
			}
		}

		public void failed(Throwable exc, ByteBuffer attachment) {
			logger.debug("ReadCompileHandler failed ---");
			exc.printStackTrace();
			try {
				socketChannel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private String read(int readSize,ByteBuffer buffer){
			byte[] bytes = new byte[readSize];
			buffer.flip();
			buffer.get(bytes);
			String msg = new String(bytes);
			logger.info("received msg:"+msg);
			return msg;
		}
		
		private void write(String msg){
			byte[] bytes = msg.getBytes();
			ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
			buffer.put(bytes);
			buffer.flip();
			socketChannel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {

				public void completed(Integer result, ByteBuffer buffer) {
					//没有发送完，接着发
					if(buffer.hasRemaining()){
						socketChannel.write(buffer, buffer, this);	
					}
				}

				public void failed(Throwable exc, ByteBuffer attachment) {
					exc.printStackTrace();
					try {
						socketChannel.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			logger.info("send msg:"+msg);
		}
	}
	
	
	
}
