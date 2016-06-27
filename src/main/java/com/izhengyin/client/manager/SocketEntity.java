package com.izhengyin.client.manager;

import io.netty.channel.Channel;

public class SocketEntity {
	private String groupId;
	private Channel channel;
	private int connectedTime;
	private byte[] ip;
	private byte[] userAgent;
	/**
	 * @return the getGroupId
	 */
	public String getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}
	/**
	 * @param socketId the socketId to set
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
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
