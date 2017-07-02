package com.github.yangwk.more.demo.zookeeper.zkclient;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class ChangeListener {

	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181");
		String path = "/test";
		
		final CountDownLatch waitCallback = new CountDownLatch(1);
		
		zkClient.subscribeDataChanges(path, new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("handleDataDeleted");
				System.out.println(dataPath + " data deleted");
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println("handleDataChange");
				System.out.println(dataPath + " data changed " + data);
			}
		});
		
		
//		if(! zkClient.exists(path)){
//			zkClient.createPersistent(path);
//		}
//		Object s = zkClient.readData(path);
//		System.out.println("orig data : "+ (s == null ? s : s.toString()) );
//		zkClient.writeData(path, "abcdefg");
		
		
//		zkClient.subscribeChildChanges(path, new IZkChildListener() {
//			
//			@Override
//			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
//				System.out.println("parentPath :" + parentPath);
//				if(currentChilds == null){
//					System.out.println("no children");
//				}else{
//					for(String child : currentChilds){
//						System.out.println("child :"+child);
//					}
//				}
//			}
//		});
		
		
		try {
			waitCallback.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
