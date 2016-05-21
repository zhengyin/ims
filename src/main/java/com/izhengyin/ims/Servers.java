package com.izhengyin.ims;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class Servers {
	
	
	
	
	public void run(int port) throws InterruptedException, SSLException, CertificateException{
		
		// SSL 证书配置
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        final SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
            .build();
		NioEventLoopGroup bossGroup = null;
		NioEventLoopGroup workerGroup = null;
		try {
			bossGroup = new NioEventLoopGroup(1);
			workerGroup = new NioEventLoopGroup();
			ServerBootstrap b = new ServerBootstrap();
			
			b.group(bossGroup,workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChildChannelHandle( sslCtx));
			
			ChannelFuture cFuture =  b.bind(port).sync();
			System.out.println("The NIO Server is started! listen port:"+port);
			cFuture.channel().closeFuture().sync();
		} finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	private class ChildChannelHandle extends ChannelInitializer<SocketChannel>{
		
		private SslContext sslCtx;
		
		public ChildChannelHandle(SslContext sslCtx) {
			this.sslCtx = sslCtx;
		}
		
		@Override
		protected void initChannel(SocketChannel sChannel) throws Exception {
			ChannelPipeline cPipeline = sChannel.pipeline();
			cPipeline.addLast(new MultiProtocolServerHandler(sslCtx,false,false));
		}
	}
}
