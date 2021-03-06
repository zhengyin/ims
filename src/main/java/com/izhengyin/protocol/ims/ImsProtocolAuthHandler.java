package com.izhengyin.protocol.ims;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.izhengyin.client.manager.ClientChannelManager;
import com.izhengyin.client.manager.SocketEntity;
import com.izhengyin.client.manager.Token;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ImsProtocolAuthHandler extends ChannelInboundHandlerAdapter{
	
	private static final Logger logger = LoggerFactory.getLogger("ImsProtocol");
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ImsMessage imsMessage = (ImsMessage)msg;
		
		if(imsMessage.getType() == ImsMessagType.AUTH){
			
			String token = new String(imsMessage.getToken());
			ImsMessage message;
			if(!Token.isRegister(token)){
				message = new ImsMessage();
				message.setType(ImsMessagType.AUTH_FAIL);
			}else{
				message = new ImsMessage();
				message.setType(ImsMessagType.AUTH_SUCCESS);
				
				ClientChannelManager channelManager = new ClientChannelManager();
				SocketEntity sokcet = channelManager.getSocket(token);
				sokcet.setChannel(ctx.channel());
				channelManager.add(token, sokcet);
				
			}
			
			
			
			//DefaultChannelGroup
			
		//	ChannelMatcher
			ChannelFuture cFuture = ctx.writeAndFlush(message);

			cFuture.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess()){
						ChannelPipeline p = future.channel().pipeline();
						//认证通过后添加消息处理handler
						p.addLast("ReadTimeoutHandler",new ReadTimeoutHandler(50));
						p.addLast("ImsProtocolHeartBeatHandler",new ImsProtocolHeartBeatHandler());
						p.addLast("ImsProtocolMessageHandler",new ImsProtocolMessageHandler());
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
			
			logger.info("auth "+imsMessage.toString());
		
			
		}else{
			//如果不是认证消息关闭连接
			ctx.close();
		}
	}
}
