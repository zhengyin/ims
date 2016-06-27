package com.izhengyin.app;

import java.util.List;

import com.izhengyin.protocol.http.HttpServerAuthHandler;
import com.izhengyin.protocol.http.HttpServerHandler;
import com.izhengyin.protocol.ims.ImsProtocolAuthHandler;
import com.izhengyin.protocol.ims.ImsProtocolDecode;
import com.izhengyin.protocol.ims.ImsProtocolEncode;
import com.izhengyin.protocol.websocket.WebSocketAuthHandler;
import com.izhengyin.protocol.websocket.WebSocketMessageDecode;
import com.izhengyin.protocol.websocket.WebSocketProtocolEncode;
import com.izhengyin.protocol.websocket.WebSocketServerHandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

public class MultiProtocolServerHandler extends ByteToMessageDecoder{
	private static final String WEBSOCKET_PATH = "/websocket";
	private SslContext sslCtx;
	private Boolean useSsl;
	private Boolean useGzip;
	
	public MultiProtocolServerHandler(SslContext sslCtx) {
		this(sslCtx, true, true);
	}
	
	/**
	 * 
	 * @param sslCtx	
	 * @param useHttps	使用  https 协议
	 * @param useGzip   使用  gzip 压缩 
	 */
	public MultiProtocolServerHandler(SslContext sslCtx,Boolean useSsl,Boolean useGzip) {
		this.sslCtx = sslCtx;
		this.useSsl = useSsl;
		this.useGzip = useGzip;
	}
	
	/**
	 * 使用输入流中前两个字节进行协议区分
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
		
		
		final int magic1 = in.getUnsignedByte(0);
		final int magic2 = in.getUnsignedByte(1);
		
		if(isSsl(in)){
			enabledSsl(ctx);
		}else if(isGizp(magic1, magic2)){
			enabledGzip(ctx);
		}else if(isHttp(magic1, magic2)){
			//继续验证是否为websocket协议连接
			ByteBuf buf = Unpooled.buffer(WEBSOCKET_PATH.length());
			in.getBytes(4, buf);
			byte[] bytes = new byte[buf.readableBytes()];
			buf.readBytes(bytes);
			String webSocketPath = new String(bytes);
			System.out.println(webSocketPath);
			if(webSocketPath.equals(WEBSOCKET_PATH)){
				switchToWebSocket(ctx);
			}else{
				switchToHttp(ctx);
			}
		}else if(isImSocket(magic1, magic2)){
			switchToSocket(ctx);
		}else{
			System.out.println("cannot be identified proticol ,  close connected! magic1:"+magic1+" magic2:"+magic2);
			ctx.close();
		}
	}
	
	
	private boolean isSsl(ByteBuf buf){
		if(useSsl){
			return SslHandler.isEncrypted(buf);
		}
		return false;
	}
	
	private boolean isGizp(int magic1,int magic2){
		if(useGzip){
			 return magic1 == 31 && magic2 == 139;
		}
		return false;
	}
	
	private boolean isHttp(int magic1,int magic2){
		return
	            magic1 == 'G' && magic2 == 'E' || // GET
	            magic1 == 'P' && magic2 == 'O' || // POST
	            magic1 == 'P' && magic2 == 'U' || // PUT
	            magic1 == 'H' && magic2 == 'E' || // HEAD
	            magic1 == 'O' && magic2 == 'P' || // OPTIONS
	            magic1 == 'P' && magic2 == 'A' || // PATCH
	            magic1 == 'D' && magic2 == 'E' || // DELETE
	            magic1 == 'T' && magic2 == 'R' || // TRACE
	            magic1 == 'C' && magic2 == 'O';   // CONNECT
	}
	
	private boolean isImSocket(int magic1,int magic2){
		return magic1 == 73 && magic2 == 77;
	}
	
	
	private void enabledSsl(ChannelHandlerContext ctx){
		ChannelPipeline p = ctx.pipeline();
        p.addLast("ssl", sslCtx.newHandler(ctx.alloc()));
        p.addLast("unificationA", new MultiProtocolServerHandler(sslCtx, false, useGzip));
        p.remove(this);
        System.out.println("============ enabledSsl ============");
	}
	
	private void enabledGzip(ChannelHandlerContext ctx){
		ChannelPipeline p = ctx.pipeline();
        p.addLast("gzipdeflater", ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
        p.addLast("gzipinflater", ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
        p.addLast("unificationB", new MultiProtocolServerHandler(sslCtx, useSsl, false));
        p.remove(this);
        System.out.println("============ enabledGzip ============");
	}
	
	private void switchToHttp(ChannelHandlerContext ctx){
		ChannelPipeline p = ctx.pipeline();
		p.addLast("decoder", new HttpRequestDecoder());
	    p.addLast("encoder", new HttpResponseEncoder());
	    p.addLast("deflater", new HttpContentCompressor());
	    p.addLast(new HttpObjectAggregator(65536));
	    p.addLast("HttpServerAuthHandler", new HttpServerAuthHandler());
	  //  p.addLast("HttpSpecificServerHandler", new HttpSpecificServerHandler());
	  //  p.addLast("HttpMessageHandler", new HttpMessageHandler());
	    p.addLast("HttpServerHandler", new HttpServerHandler());
		p.remove(this);
		System.out.println("============ switchToHttp ============");
	}
	
	private void switchToWebSocket(ChannelHandlerContext ctx){
		ChannelPipeline p = ctx.pipeline();
        p.addLast("HttpServerCodec",new HttpServerCodec());
        p.addLast("HttpObjectAggregator",new HttpObjectAggregator(65536));
        p.addLast("WebSocketServerCompressionHandler",new WebSocketServerCompressionHandler());
        p.addLast("WebSocketServerProtocolHandler",new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        p.addLast("WebSocketMessageDecode",new WebSocketMessageDecode());
        p.addLast("WebSocketProtocolEncode",new WebSocketProtocolEncode());
        p.addLast("WebSocketAuthHandler",new WebSocketAuthHandler());
		p.remove(this);
		System.out.println("============ switchToWebSocket ============");
	}
	
	private void switchToSocket(ChannelHandlerContext ctx){
		ChannelPipeline p = ctx.pipeline();
		p.addLast("ImsProtocolDecode",new ImsProtocolDecode(65535, 3, 4));
		p.addLast("ImsProtocolEncode",new ImsProtocolEncode());
		p.addLast("ImsProtocolAuthHandler",new ImsProtocolAuthHandler());
		p.remove(this);
		System.out.println("============ switchToSocket ============");
	}
}
