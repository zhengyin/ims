package com.izhengyin.protocol.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.izhengyin.protocol.ims.ImsMessagType;
import com.izhengyin.protocol.ims.ImsMessage;
import com.izhengyin.protocol.ims.ImsProtocolHeartBeatHandler;
import com.izhengyin.protocol.ims.ImsProtocolMessageHandler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class WebSocketAuthHandler extends ChannelInboundHandlerAdapter{
	
	
	private static final Logger logger = LoggerFactory.getLogger("WebSocketProtocol");
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		WebSocketMessage wsMessage  = (WebSocketMessage)msg;
		
		System.out.println(wsMessage);
		
		if(wsMessage.getType() == WebSocketMessagType.AUTH){
			
			byte[] token = wsMessage.getToken();
			WebSocketMessage message = new WebSocketMessage();
			message.setType(WebSocketMessagType.AUTH_SUCCESS);
			ChannelFuture cFuture = ctx.writeAndFlush(message);

			cFuture.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess()){
						ChannelPipeline p = future.channel().pipeline();
						//认证通过后添加消息处理handler
						p.addLast("ReadTimeoutHandler",new ReadTimeoutHandler(50));
						p.addLast("ImsProtocolHeartBeatHandler",new WebSocketProtocolHeartBeatHandler());
						p.addLast("ImsProtocolMessageHandler",new WebSocketServerMessageHandler());
					}
					if(future.isCancelled()){
						logger.info("isCancelled");
					}
					if(future.isDone()){
						logger.info("isDone");
					}
					if(future.isCancellable()){
						logger.info("isCancellable");
					}
					if(future.isVoid()){
						logger.info("isVoid");
					}
				}
			});
			
			ctx.pipeline().remove(this);
			
			logger.info("auth success , "+wsMessage.toString());
			
		}else{
			//如果不是认证消息关闭连接
			ctx.close();
		}
	}
	
}
