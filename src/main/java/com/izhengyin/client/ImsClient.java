package com.izhengyin.client;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.izhengyin.protocol.ims.ImsProtocolDecode;
import com.izhengyin.protocol.ims.ImsProtocolEncode;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class ImsClient {
	
	public static void main(String[] args) throws InterruptedException {
		new ImsClient("127.0.0.1", 8080).run();
	}
	
	private  String hostname;
	private  int port; 
	
	private static final Logger logger = LoggerFactory.getLogger(ImsClient.class);
	
	public ImsClient(String hostname,int  port) {
		this.hostname = hostname;
		this.port = port;
	}
	public void run() throws InterruptedException{
		
		InetSocketAddress remoteAddress = new InetSocketAddress(hostname, port);
		
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		
		bootstrap.group(group);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.handler(new LoggingHandler());
		bootstrap.handler(new MyHandlerInit());
		
		try{
			
			ChannelFuture channelFuture = bootstrap.connect(remoteAddress);
			
			logger.info("client connected to "+remoteAddress.toString());
			
			channelFuture.channel().closeFuture().sync();
			
		}finally{
			group.shutdownGracefully();
		}
	}
	
	private static class MyHandlerInit extends ChannelInitializer<SocketChannel>{

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new ImsProtocolEncode());
			ch.pipeline().addLast(new ImsProtocolDecode(65535, 3, 4));
			ch.pipeline().addLast(new ImsClientHandler(logger));
		}
		
	}
}
