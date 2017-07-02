package com.github.yangwk.more.demo.hadoop.hdfs;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class FileSystemOperation {
	
	public static final String HDFS_URI = "hdfs://192.168.10.101:9000";
	
	/**
	 * 打印本地文件系统的实现，这也是默认的实现
	 * @author yangwk
	 *
	 */
	public void printLocalImpl(){
		FileSystem fs = null;
		try {
			Configuration conf = new Configuration();
			URI uri = FileSystem.getDefaultUri( conf );
			fs = FileSystem.get(uri, conf);
			
			System.out.println(uri.toString());	// file:///
			System.out.println( fs.getClass().getName() );	// org.apache.hadoop.fs.LocalFileSystem
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			IOUtils.closeQuietly(fs);
		}
	}
	
	/**
	 * 打印分布式系统的实现，需要导入hdfs
	 * @author yangwk
	 *
	 */
	public void printDistributedImpl(){
		FileSystem fs = null;
		try {
			Configuration conf = new Configuration();
			URI uri = URI.create( HDFS_URI );
			fs = FileSystem.get(uri, conf);
			
			System.out.println(uri.toString());	// hdfs://192.168.10.101:9000
			System.out.println( fs.getClass().getName() );	// org.apache.hadoop.hdfs.DistributedFileSystem
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(fs);
		}
	}
	
	
	private FileSystem getFileSystem(){
		FileSystem fs = null;
		try {
			Configuration conf = new Configuration();
			fs = FileSystem.get( URI.create(HDFS_URI), conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fs;
	}
	
	
	<T> T execute(Action<T> action){
		FileSystem fs = null;
		try{
			fs = getFileSystem();
			T result = action.doAction(fs);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeQuietly(fs);
		}
	}
	
	
	public FileStatus[] listStatus(final String path){
		return execute(new Action<FileStatus[]>() {

			@Override
			public FileStatus[] doAction(FileSystem fs) throws Exception {
				Path p = new Path(path);
				return fs.listStatus(p);
			}

		});
	}


}
