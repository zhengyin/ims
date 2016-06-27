package com.izhengyin.protocol.http;


import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.izhengyin.client.manager.ClientChannelManager;
import com.izhengyin.client.manager.SocketEntity;
import com.izhengyin.client.manager.Sockets;
import com.izhengyin.client.manager.Token;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.AsciiString;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
	
	private static final AsciiString CONNECTION = new AsciiString("Connection");
	
	private static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");
	
    private FullHttpRequest request;
    
    private String clientIp;
    
    private String clientAgent;
    
	private Map<String, List<String>> requestParams ;
	
    
    
	public HttpServerHandler() {}
	
	 /** Buffer that stores the response content */
    private final StringBuilder buf = new StringBuilder();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		
		this.request = request;
		
		this.requestParams = new QueryStringDecoder(request.uri()).parameters();
		
		InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
		
		this.clientIp = address.getAddress().getHostAddress();
		
		this.clientAgent = request.headers().get("User-Agent");
		
		route(ctx, request);
	}
	
	private void route(ChannelHandlerContext ctx, FullHttpRequest request){
	
		String uri = request.uri();
		
		int i = uri.indexOf("?");
		System.out.println(i);
		if(i>=0){
			uri = uri.substring(0,i);
		}
		
		System.out.println(uri);
		
		if (!requestParams.isEmpty()) {
            for (Entry<String, List<String>> p: requestParams.entrySet()) {
                String key = p.getKey();
                List<String> vals = p.getValue();
                for (String val : vals) {
                    buf.append("PARAM: ").append(key).append(" = ").append(val).append("\r\n");
                }
            }
            buf.append("\r\n");
            System.out.println(buf.toString());
        }
		
		System.out.println(ctx.channel().id().asShortText());
		System.out.println(ctx.channel().id().asLongText());
		
		DefaultFullHttpResponse response = null;
		
		if(uri.toLowerCase().equals("/token/register/")){
			response = toKenRegister();
		}else if(uri.toLowerCase().equals("/token/destory/")){
			
		}
		
		sendHttpResponse(ctx, response);
	}
	
	/**
	 * 注册 token
	 * @return DefaultFullHttpResponse
	 */
	private DefaultFullHttpResponse toKenRegister(){
		String ip = requestParams.get("ip").get(0);
		String groupId = requestParams.get("groupId").get(0);
		int authExpireTime = Integer.parseInt(requestParams.get("authExpireTime").get(0));
		System.out.println(ip);
		System.out.println(authExpireTime);
		String token = Token.generate(ip);
		DefaultFullHttpResponse response ;
		if(token == null){
			response= new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST, Unpooled.EMPTY_BUFFER);
		}else{
			
			int maxTryRegisterNum = 100;	//最大尝试的注册次数
			
			boolean state ;
			while ((state = Token.register(token, authExpireTime))) {
				if(state || maxTryRegisterNum<=0){
					break;
				}
				maxTryRegisterNum --;
			};
			
			if(!state){
				response= new DefaultFullHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR, Unpooled.EMPTY_BUFFER);
			}
			
			SocketEntity socketEntity = new SocketEntity();
			socketEntity.setGroupId(groupId);
			
			Sockets sockets = new Sockets();
			sockets.add(token, socketEntity);
			
			
			response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.copiedBuffer(token.getBytes()));
		}
		return response;
	}
	
	
	
	
	private void sendHttpResponse(ChannelHandlerContext ctx , DefaultFullHttpResponse response) {
		if(HttpUtil.is100ContinueExpected(request)){
			ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
		}else if(HttpUtil.isKeepAlive(request)){
			response.headers().set(CONNECTION, KEEP_ALIVE);
			ctx.write(response);
		}
        ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}























