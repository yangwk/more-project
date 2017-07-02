package com.github.yangwk.more.demo.hadoop;

import org.apache.hadoop.fs.FileStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.github.yangwk.more.demo.hadoop.hdfs.FileSystemOperation;

public class FileSystemOperationTest {
	
	static void printLocalImplTest(){
		FileSystemOperation fso = new FileSystemOperation();
		fso.printLocalImpl();
	}
	
	static void printDistributedImplTest(){
		FileSystemOperation fso = new FileSystemOperation();
		fso.printDistributedImpl();
	}
	
	static void listStatusTest(){
		FileSystemOperation fso = new FileSystemOperation();
		//不安全
		// hdfs://192.168.10.101:9000/user
		FileStatus[] fileStatusArr =  fso.listStatus("/user/ywk");
		for(final FileStatus status : fileStatusArr){
			
			String json = JSON.toJSONString(status, new PropertyPreFilter() {
				
				@Override
				public boolean apply(JSONSerializer serializer, Object object, String name) {
					if(name.equals("symlink")){
						if(! status.isSymlink()){
							return false;
						}
					}
					return true;
				}
			});
			
			System.out.println(json);
		}
	}

	public static void main(String[] args) {
//		printLocalImplTest();
//		printDistributedImplTest();
		listStatusTest();
	}

}
