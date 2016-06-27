package com.izhengyin.client.manager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import com.izhengyin.unit.Validdate;

public class Token {
	
	private static HashMap<String, Integer> tokens;
	private static final int GC_PROBABILITY  = 1;	
	private static final int GC_DIVISOR  = 100;
	
	
	/**
	 * token 生成规则, [客户端IP+天+时+秒+毫秒]
	 * 	注： 在同一客户端进行注册时， 同一秒并发请求有62分之1的概率重复
	 * @param ip
	 * @return	String
	 */
	public static String generate(String ip){
		
		byte[] tokenBytes = new byte[]{
				48,49,50,51,52,53,54,55,56,57,	//0~9
				65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90, //A~Z
				97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122 //a~z
		};
		
		if(!Validdate.ip(ip)){
			return null;
		}
		
		String [] ipb= ip.split("\\.");
		
		int[] arr = new int[8];
		arr[0] = Integer.parseInt(ipb[0]);
		arr[1] = Integer.parseInt(ipb[1]);
		arr[2] = Integer.parseInt(ipb[2]);
		arr[3] = Integer.parseInt(ipb[3]);
		Calendar calendar = Calendar.getInstance();
		arr[4] = calendar.get(Calendar.DAY_OF_YEAR);
		arr[5] = calendar.get(calendar.HOUR_OF_DAY);
		arr[6] = calendar.get(calendar.SECOND);
		arr[7] = calendar.get(calendar.MILLISECOND);
		
		StringBuilder sBuilder = new StringBuilder();
		int baseLen = tokenBytes.length;
		for(int i=0;i<arr.length;i++){
			byte b = tokenBytes[arr[i]%baseLen];
			sBuilder.append((char)b);
		}
		return sBuilder.toString();
	}
	
	
	/**
	 * @param token				认证令牌	8个字节byte数组 
	 * @param authExpireTime	认证失效时间 [秒]
	 * @return
	 */
	public static boolean register(String token , int authExpireTime){
		
		if(tokens.get(token) != null){
			return false;
		}
		
		Calendar calendar = Calendar.getInstance();
		
		authExpireTime =  (int)calendar.getTimeInMillis()/1000+authExpireTime;
		
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
