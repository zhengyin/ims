package com.izhengyin.ims;

import java.util.List;

import com.izhengyin.http.HttpServerHandler;
import com.izhengyin.privateProtocol.MessageDecode;
import com.izhengyin.socket.SocketServerHandler;
import com.izhengyin.websocket.WebSocketServerHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.string.StringDecoder;
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
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		
		final int magic1 = in.getUnsignedByte(0);
		final int magic2 = in.getUnsignedByte(1);
		
		
		
		
		if(isSsl(in)){
			enabledSsl(ctx);
		}else if(isGizp(magic1, magic2)){
			enabledGzip(ctx);
		}else if(isHttp(magic1, magic2)){
			switchToHttp(ctx);
		}else{
			switchToSocket(ctx);
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
        p.addLast(new WebSocketServerCompressionHandler());
        p.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
	    p.addLast("handler1",new HttpServerHandler());
	    p.addLast("handler2", new WebSocketServerHandler());
		p.remove(this);
		System.out.println("============ switchToHttp ============");
	}
	
	private void switchToSocket(ChannelHandlerContext ctx){
		ChannelPipeline p = ctx.pipeline();
		//p.addLast(new LineBasedFrameDecoder(1024));
		p.addLast(new MessageDecode());
	    p.addLast(new SocketServerHandler());
		p.remove(this);
		System.out.println("============ switchToSocket ============");
	}
}
