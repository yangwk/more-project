package com.github.yangwk.more.demo.hadoop.hdfs;

import org.apache.hadoop.fs.FileSystem;

public interface Action<T> {
	T doAction(FileSystem fs) throws Exception;
}
