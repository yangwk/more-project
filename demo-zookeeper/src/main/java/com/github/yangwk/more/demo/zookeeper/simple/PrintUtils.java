package com.github.yangwk.more.demo.zookeeper.simple;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class PrintUtils {
	
	public static void print(WatchedEvent event){
		//输出事件详情
		String path = event.getPath();
		KeeperState ks = event.getState();
		String ksName = ks.name();
		int ksIntValue = ks.getIntValue();
		EventType et = event.getType();
		String etName = et.name();
		int etIntValue = et.getIntValue();
		
		System.out.println("path:"+path+", KeeperState.name:"+ksName
				+", KeeperState.intValue:"+ksIntValue+", EventType.name:"
				+etName+", EventType.intValue:"+etIntValue);
	}
	
	
}
