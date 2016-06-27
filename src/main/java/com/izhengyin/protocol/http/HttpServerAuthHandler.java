package com.izhengyin.protocol.http;

import static io.netty.handler.codec.http.HttpVersion.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

public class HttpServerAuthHandler extends ChannelInboundHandlerAdapter{
	
	private String allowIpPattern = "^192.168.[0-255]{1}.[0-255]{1}";
	
	public HttpServerAuthHandler() {
		
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
		if(!isAllowIp(address.getAddress().getHostAddress())){
			 FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN);
			 ctx.writeAndFlush(response);
		}else{
			 ctx.fireChannelRead(msg);
		}
	}
	
	private boolean isAllowIp(String ip){
        Pattern r = Pattern.compile(allowIpPattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(ip);
        if (m.find()) {
    	   return true;
        } else {
           return false;
        }
	}
	
}























