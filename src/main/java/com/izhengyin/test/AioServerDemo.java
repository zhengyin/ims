package com.izhengyin.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http2.Http2Connection.Listener;

public class AioServerDemo {
	
	private static final Logger logger = LoggerFactory.getLogger(AioServerDemo.class);
	
	public static void main(String[] args) throws IOException {
		AioServerDemo oAioServerDemo = new AioServerDemo();
		oAioServerDemo.listener();
	}
	
	
	private void listener() throws IOException{
		AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
		serverChannel.bind(new InetSocketAddress(3002));
		serverChannel.accept(this, new AcceptCompileHandler());
		
		logger.debug("accept ---");
	}
	
	
	private class AcceptCompileHandler implements CompletionHandler<AsynchronousSocketChannel, AioServerDemo> {

		public void completed(AsynchronousSocketChannel result, AioServerDemo attachment) {
			// TODO Auto-generated method stub
			logger.debug("completed ---");
			
			
		}

		public void failed(Throwable exc, AioServerDemo attachment) {
			// TODO Auto-generated method stub
			logger.debug("failed ---");
			
		}
		
	}
	
	
	private class  CompileHandler implements CompletionHandler<AsynchronousSocketChannel, AcceptCompileHandler> {

		public void completed(AsynchronousSocketChannel result, AcceptCompileHandler attachment) {
			// TODO Auto-generated method stub
			
		}

		public void failed(Throwable exc, AcceptCompileHandler attachment) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
}
