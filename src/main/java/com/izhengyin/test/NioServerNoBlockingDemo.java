package com.izhengyin.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author zhengyin < http://izhengyin.com >
 */
public class NioServerNoBlockingDemo {
	
	private static final int PORT = 3002;
	private static final Logger logger = LoggerFactory.getLogger(NioServerNoBlockingDemo.class);
	public static void main(String[] args) throws IOException {
		new Handler(PORT).start();
	}
	
	private static class Handler extends Thread{
		
		private  ServerSocketChannel serverSocketChannel;
		private  Selector selector;
		private  Boolean bShop = false;
		public Handler(int port) throws IOException {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			this.serverSocketChannel.configureBlocking(false);
			/**
			    SelectionKey.OP_CONNECT		连接就绪
				SelectionKey.OP_ACCEPT		接收就绪
				SelectionKey.OP_READ		读就绪
				SelectionKey.OP_WRITE		写就绪
			 */
			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		}
		
		public void run() {
			while (!bShop) {
				try {
					int num = selector.select(3000);
					logger.debug("register key:"+selector.keys().size()+" , select channel num:"+num);
					@SuppressWarnings("rawtypes")
					Set selectionKeys = selector.selectedKeys();	// 返回准备就绪的 selectionKey 
					@SuppressWarnings("rawtypes")
					Iterator keyIterator = selectionKeys.iterator();
					while (keyIterator.hasNext()) {
						
						SelectionKey key = (SelectionKey)keyIterator.next();
						
						try {
							handelInput(key);
						} catch (Exception e) {
							e.printStackTrace();
							if (key != null) {
				                key.cancel();
				                key.channel().close();
				            }
						}
						
						keyIterator.remove();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
			}
			
			// TODO: handle finally clause
			if(selector != null){
				try {
					selector.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		private void handelInput(SelectionKey key) throws IOException{
			
			if(key.isAcceptable()){	//由 	serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); 注册监听
				ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
				SocketChannel socketChannel = serverSocketChannel.accept();	//产生一个新的 socketChannel，处理客户端的数据
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_READ);
				logger.info("isAcceptable key:"+key.toString()+" remoteAddress:"+socketChannel.getRemoteAddress());
				
			}else if(key.isConnectable()){	
				logger.info("isConnectable key:"+key.toString());
			}else if(key.isReadable()){
				
				SocketChannel socketChannel = (SocketChannel)key.channel();
				ByteBuffer buffer = ByteBuffer.allocate(128);
				int readSize = socketChannel.read(buffer);
				
				logger.info("isReadable key:"+key.toString()+" remoteAddress:"+socketChannel.getRemoteAddress()+" readSize:"+readSize);
				if(readSize>0){	//读到有效数据
					buffer.flip();
					byte[] bytes = new byte[readSize];
					buffer.get(bytes);
					String msg = new String(bytes);
					logger.info("receive msg:"+msg);
					if(msg.equals("Hi,Server")){
						send(socketChannel, "Hi,Client");
					}else if(msg.equals("I am here")){	//客户端 响应  Hi,Client 的应答消息
						send(socketChannel, "ok");
	    				logger.info("应答完成");
	    			}else{
	    				logger.debug("未知的消息:"+msg);
	    			}
					buffer.clear();
				}else if(readSize < 0){	//对端链路关闭,需要关闭socket
					key.cancel();		//将key从  selector 上清除 ,通过 selector.keys() 查看注册在 selector上的 key
					socketChannel.close();
					logger.info("对端链路关闭");
				}else{	//正常产生的空数据包 ,丢弃
					
				}
			}else if(key.isWritable()){
				logger.info("isWritable key:"+key.toString());
			}
		}
		
		private void send(SocketChannel socketChannel,String msg) throws IOException{
			byte[] bytes = msg.getBytes();
			ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
			buffer.put(bytes);
			buffer.flip();
			socketChannel.write(buffer);
			logger.info("send msg:"+msg);
		}
		
		
		@SuppressWarnings("unused")
		public void setBStop(boolean state){
			this.bShop = state;
		}
	}
}
