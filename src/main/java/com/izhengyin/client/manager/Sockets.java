package com.izhengyin.client.manager;

import java.util.HashMap;


/**
 * 客户端 Socket 连接对象管理
 * @author zhengyin <zhengyin.name@gmail.com>
 *
 */
public class Sockets {
	
	private static HashMap<String, SocketEntity> sockets;
	
	
	public void add(String key,SocketEntity socketEntity){
		sockets.put(key, socketEntity);
	}
	
	public SocketEntity get(String key){
		return sockets.get(key);
	}
	
	public void remove(String key){
		sockets.remove(key);
	}
}
