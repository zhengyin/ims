package com.izhengyin.client.manager;

public class SocketEntity {
	private String channel;
	private String socketId;
	private int authExpireTime;
	private int connectedTime;
	private byte[] ip;
	private byte[] userAgent;
	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * @return the socketId
	 */
	public String getSocketId() {
		return socketId;
	}
	/**
	 * @param socketId the socketId to set
	 */
	public void setSocketId(String socketId) {
		this.socketId = socketId;
	}
	/**
	 * @return the authExpireTime
	 */
	public int getAuthExpireTime() {
		return authExpireTime;
	}
	/**
	 * @param authExpireTime the authExpireTime to set
	 */
	public void setAuthExpireTime(int authExpireTime) {
		this.authExpireTime = authExpireTime;
	}
	/**
	 * @return the connectedTime
	 */
	public int getConnectedTime() {
		return connectedTime;
	}
	/**
	 * @param connectedTime the connectedTime to set
	 */
	public void setConnectedTime(int connectedTime) {
		this.connectedTime = connectedTime;
	}
	/**
	 * @return the ip
	 */
	public byte[] getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(byte[] ip) {
		this.ip = ip;
	}
	/**
	 * @return the userAgent
	 */
	public byte[] getUserAgent() {
		return userAgent;
	}
	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(byte[] userAgent) {
		this.userAgent = userAgent;
	}
	
	
	
}
