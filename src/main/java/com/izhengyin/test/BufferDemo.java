package com.izhengyin.test;

import java.nio.ByteBuffer;

public class BufferDemo {
	public static void main(String[] args) {
		
		//创建一个 24 byte 的空间大小
		int capacity = 24;
		ByteBuffer buffer = ByteBuffer.allocate(capacity);
		BufferUnit.bufferInfo(buffer,"创建一个 24 byte 的空间大小");
		
		//写
		byte b1 = 72;
		byte b2 = 101;
		byte b3 = 108;
		byte b4 = 108;
		byte b5 = 111;
		buffer.put(b1);
		buffer.put(b2);
		buffer.put(b3);
		buffer.put(b4);
		buffer.put(b5);
		BufferUnit.bufferInfo(buffer,"写");
		
		// 切换读模式
		buffer.flip();
		BufferUnit.bufferInfo(buffer,"切换读模式");
		
		//读数据
		while(buffer.hasRemaining()){
			System.out.println(((char)buffer.get()));
		}
		BufferUnit.bufferInfo(buffer,"读数据");
		
		//清空缓冲区 ，注： clear 会将整个缓冲区清除，包括未读完的数据,如数据未读完，但需要先写入使用 compact 替代 clear
		buffer.clear();
		BufferUnit.bufferInfo(buffer,"清空缓冲区");
		
		//接着写
		buffer.put(b1);
		buffer.put(b2);
		buffer.put(b3);
		buffer.put(b4);
		buffer.put(b5);
		
		//切换到读
		buffer.flip();
		BufferUnit.bufferInfo(buffer,"切换到读");
		
		//只读取缓冲区 3 byte 余 2byte
		for(int i =0;i<3;i++){
			System.out.println(((char)buffer.get()));
		}
		BufferUnit.bufferInfo(buffer,"只读取缓冲区 3 byte 余 2byte");
		
		//清空已读缓冲区,此时 posttion 值为未读缓冲区数量 
		buffer.compact();
		BufferUnit.bufferInfo(buffer,"清空已读缓冲区,此时 posttion 值为未读缓冲区数量");
		
		//在接着写点数据
		buffer.put((byte) 33); //!
		buffer.put((byte) 35); //#
		
		//切换到读模式，并一次读完全部数据
		buffer.flip();
		while(buffer.hasRemaining()){
			System.out.println(((char)buffer.get()));
		}
		BufferUnit.bufferInfo(buffer,"切换到读模式，并一次读完全部数据");
	}
	
	
}	