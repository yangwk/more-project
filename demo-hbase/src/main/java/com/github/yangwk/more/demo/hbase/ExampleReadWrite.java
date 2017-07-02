package com.github.yangwk.more.demo.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.mapreduce.Job;

public class ExampleReadWrite {

	public static class MyMapper extends TableMapper<ImmutableBytesWritable, Put>  {

		@Override
		public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
			// this example is just copying the data from the source table...
	   		context.write(row, resultToPut(row,value));
	   	}
	        
	  	private static Put resultToPut(ImmutableBytesWritable key, Result result) throws IOException {
	  		Put put = new Put(key.get());
	 		for (Cell cell : result.rawCells()) {
				put.add(cell);
			}
			return put;
	   	}
	}

	public static void main(String[] args) throws Exception {
		Configuration config = HBaseConfiguration.create();
		Job job = Job.getInstance(config, "ExampleReadWrite");
		job.setJarByClass(ExampleReadWrite.class); // class that contains mapper

		Scan scan = new Scan();
		scan.setCaching(500); // 1 is the default in Scan, which will be bad for MapReduce jobs
		scan.setCacheBlocks(false); // don't set to true for MR jobs
		// set other scan attrs

		TableMapReduceUtil.initTableMapperJob(
				args[0], // input HBase table name
				scan, // Scan instance to control CF and attribute selection
				MyMapper.class, // mapper
				null, // mapper output key
				null, // mapper output value
				job);
		
		TableMapReduceUtil.initTableReducerJob(
				args[1],      // output table
				null,             // reducer class
				job);
		
		job.setNumReduceTasks(0);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
