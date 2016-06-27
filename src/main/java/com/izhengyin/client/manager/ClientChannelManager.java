package com.izhengyin.client.manager;

import java.util.ArrayList;
import java.util.List;


import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;


public class ClientChannelManager {
	private static Group group = new Group();
	private static Sockets sockets = new Sockets();
	private static DefaultChannelGroup channelGroup = new DefaultChannelGroup(new DefaultEventExecutor());
	
	public void add(String key,SocketEntity socketEntity){
		
		sockets.add(key, socketEntity);
		
		group.add(socketEntity.getGroupId(), key);
		
		if(socketEntity.getChannel() != null){
			channelGroup.add(socketEntity.getChannel());
		}
		
	}
	
	
	public boolean remove(String key){
		
		SocketEntity socketEntity = sockets.get(key);
		
		if(socketEntity == null){
			return false;
		}
		
		if(!group.remove(socketEntity.getGroupId(),key)){
			return false;
		}
		
		return channelGroup.remove(socketEntity.getChannel());

	}
	
	public SocketEntity getSocket(String key){
		return sockets.get(key);
	}
	
	
	public List<SocketEntity> getSocketsByGroup(String groupId){
		List<SocketEntity>  result = new ArrayList<SocketEntity>();
		List<String> list = group.getList(groupId);
		for(int i=0;i<list.size();i++){
			String key = list.get(i);
			SocketEntity socketEntity = getSocket(key);
			if(socketEntity != null){
				result.add(socketEntity);
			}
		}
		return result;
	}
	
	
	public ChannelGroupFuture emit(SocketEntity socket,byte[] message){
		ChannelMatcher matcher = ChannelMatchers.is(socket.getChannel());
		return channelGroup.writeAndFlush(message,matcher);
	}
	
	public ChannelGroup broadcast(String groupId,byte[] message){
		
		List<SocketEntity> sockets = getSocketsByGroup(groupId);
		
		for(int i=0;i<sockets.size();i++){
			SocketEntity socket = sockets.get(i);
			if(socket != null){
				ChannelMatcher matcher = ChannelMatchers.is(socket.getChannel());
				channelGroup.write(message,matcher);
			}
		}
		return channelGroup.flush();
	}
}
