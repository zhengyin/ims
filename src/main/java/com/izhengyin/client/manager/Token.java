package com.izhengyin.client.manager;

import java.util.HashMap;
import java.util.Iterator;

public class Token {
	
	private static HashMap<String, Integer> tokens;
	private static final int GC_PROBABILITY  = 1;	
	private static final int GC_DIVISOR  = 100;
	
	/**
	 * 
	 * @param token				认证令牌	8个字节byte数组 [客户端ip+当前分，当前秒，随机数1，随机数2]
	 * @param authExpireTime	认证失效时间
	 * @return
	 */
	public static boolean register(String token , int authExpireTime){
		
		if(tokens.get(token) != null){
			return false;
		}
		
		tokens.put(token, authExpireTime);
		
		randomGc();
		
		return true;
	}
	
	public static boolean isRegister(String token){
		int authExpireTime = tokens.get(token);
		if(authExpireTime<=0){
			return false;
		}
		
		int curTime = (int) (System.currentTimeMillis() / 1000);
		
		if(authExpireTime < curTime){
			tokens.remove(token);
			return false;
		}
		return true;
	}
	
	/**
	 * 随机GC
	 */
	private static void randomGc(){
		//触发GC,循环已注册的token列表,移除过期的token
		if(Math.random()*GC_DIVISOR<GC_PROBABILITY){
			int curTime = (int) (System.currentTimeMillis() / 1000);
			Iterator iter = tokens.entrySet().iterator();
			while (iter.hasNext()) {
				int t = (Integer) iter.next();
				if(t<curTime){
					iter.remove();
				}
			}
		}
	}
}
