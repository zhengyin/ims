package com.izhengyin.client.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Group{
	
	private static HashMap<String, List<String>> group;
	

	public void add(String groupId,String key){
		
		List<String> groupList = group.get(key);
		
		if(groupList.isEmpty()){
			groupList = new ArrayList<String>();
		}
		
		groupList.add(key);
		
		group.put(groupId,groupList);
	}
	
	public void remove(String groupId) {
		group.remove(groupId);
	}

	public boolean remove(String groupId,String key) {
		List<String> groupList = group.get(groupId);
		if(!groupList.isEmpty()){
			return groupList.remove(key);
		}
		return false;
	}

	public List<String> getList(String groupId){
		return group.get(groupId);
	}
}
