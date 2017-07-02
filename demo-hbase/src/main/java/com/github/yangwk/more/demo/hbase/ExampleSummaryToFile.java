package com.github.yangwk.more.demo.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ExampleSummaryToFile {

	public static class MyMapper extends TableMapper<Text, IntWritable>  {
		
		private final IntWritable ONE = new IntWritable(1);
	   	private Text text = new Text();

		@Override
		public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
			String val = new String(value.getValue(Bytes.toBytes("cf"), Bytes.toBytes("a")));
          	text.set(val);     // we can only emit Writables...

        	context.write(text, ONE);
	   	}
	}
	
	
	public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable>  {
        
		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int i = 0;
			for (IntWritable val : values) {
				i += val.get();
			}	
			context.write(key, new IntWritable(i));
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		Configuration config = HBaseConfiguration.create();
		Job job = Job.getInstance(config, "ExampleSummaryToFile");
		job.setJarByClass(ExampleSummaryToFile.class); // class that contains mapper

		Scan scan = new Scan();
		scan.setCaching(500); // 1 is the default in Scan, which will be bad for MapReduce jobs
		scan.setCacheBlocks(false); // don't set to true for MR jobs
		// set other scan attrs

		TableMapReduceUtil.initTableMapperJob(
				args[0], // input HBase table name
				scan, // Scan instance to control CF and attribute selection
				MyMapper.class, // mapper
				Text.class, // mapper output key
				IntWritable.class, // mapper output value
				job);
		
		job.setReducerClass(MyReducer.class);    // reducer class
		job.setNumReduceTasks(1);    // at least one, adjust as required
		FileOutputFormat.setOutputPath(job, new Path(args[1]));  // adjust directories as required

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
