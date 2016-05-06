package com.izhengyin.privateProtocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Client {
	
	public static void main(String[] args) throws InterruptedException {
		String host = "127.0.0.1";
		int port = 8080;
		new Client().run(host, port);
	}
	
	
	public void run(String host,int port) throws InterruptedException{
		
		NioEventLoopGroup group = null;
		
		try {
			group = new NioEventLoopGroup();
			Bootstrap bootstrap  = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ClientHandler());
			
			ChannelFuture cFuture = bootstrap.connect(host,port).sync();
			System.out.println("connect to "+host+" "+port+" ok");
			
			cFuture.channel().closeFuture().sync();
			System.out.println("connect is close");
			
		} finally {
			group.shutdownGracefully();
		}
	}
	
	
	private static class ClientHandler extends ChannelInitializer<SocketChannel>{
		
		protected void initChannel(SocketChannel ch) throws Exception {
			
			ChannelPipeline cPipeline = ch.pipeline();
			cPipeline.addLast(new MessageEncode());
			cPipeline.addLast(new MessageBinaryDecode());
		//	cPipeline.addLast(new StringEncoder());
			cPipeline.addLast(new ClientChannelHandler());
			
		}
	}

}
