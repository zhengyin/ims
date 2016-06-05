package com.izhengyin.client;

import java.util.concurrent.TimeUnit;

import org.omg.PortableServer.ThreadPolicyOperations;
import org.slf4j.Logger;

import com.izhengyin.protocol.ims.ImsMessagType;
import com.izhengyin.protocol.ims.ImsMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class ImsClientHandler extends ChannelInboundHandlerAdapter{
	static final byte MESSAGE_TYPE_AUTH = 1;						//认证
	static final byte MESSAGE_TYPE_AUTH_SUCCESS = 2;				//认证成功
	static final byte MESSAGE_TYPE_SEND_HEARTBEAT = 3;				//发送心跳
	static final byte MESSAGE_TYPE_SERVER_RESPONSE_HEARTBEAT = 4;	//服务端响应心跳
	private Logger logger;
	public ImsClientHandler(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelActive ");
		ImsMessage imsMessage = new ImsMessage();
		imsMessage.setType(ImsMessagType.AUTH);
		byte[] token = new byte[8];
		token[0] = 123;
		token[1] = 123;
		token[2] = 123;
		token[3] = 123;
		token[4] = 25;
		token[5] = 54;
		token[6] = 12;
		token[7] = 112;
		imsMessage.setToken(token);
		ctx.write(imsMessage);
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ImsMessage imsMessage = (ImsMessage)msg;
		
		logger.debug(msg.toString());
		
		if(imsMessage.getType() == ImsMessagType.AUTH_SUCCESS){
			ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5, TimeUnit.SECONDS);
		}else if(imsMessage.getType() == ImsMessagType.RESPONSE_HEARTBEAT){
			logger.debug("receiver server heartbeat message -> "+imsMessage.toString());
		}
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		cause.printStackTrace();
	}
	
	private class HeartBeatTask implements Runnable{
		private ChannelHandlerContext ctx;
		public HeartBeatTask(ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}
		public void run() {
			ImsMessage imsMessage = buildHeartMessage();
			ctx.writeAndFlush(imsMessage);
		}
		private ImsMessage buildHeartMessage(){
			ImsMessage imsMessage = new ImsMessage();
			imsMessage.setType(ImsMessagType.SEND_HEARTBEAT);
			return imsMessage;
		}
	}
}
