package com.izhengyin.unit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validdate {
	
	private final static String IP_PATTREN = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."  
	          +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
	          +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
	          +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	
	
	public static boolean ip(String ipAddress){
	    Pattern r = Pattern.compile(IP_PATTREN);
	    return matcher(r,ipAddress);
	}
	
	public static boolean matcher(Pattern r,String in){
		Matcher m = r.matcher(in);
		if (m.find()) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
}
