package com.github.yangwk.more.demo.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

public class ExampleRead {

	public static class MyMapper extends TableMapper<Text, LongWritable> {
		
		@Override
		public void map(ImmutableBytesWritable key, Result value, Context context)
				throws IOException, InterruptedException {
			// process data for the row from the Result instance.
			Cell cell = value.getColumnLatestCell(Bytes.toBytes("cf"), Bytes.toBytes("a"));
			if(cell != null){
				byte[] cellValue = CellUtil.cloneValue(cell);
				System.out.println("read cell value:" + Bytes.toString(cellValue) );
			}
		}

	}

	public static void main(String[] args) throws Exception {
		Configuration config = HBaseConfiguration.create();
		Job job = Job.getInstance(config, "ExampleRead");
		job.setJarByClass(ExampleRead.class); // class that contains mapper

		Scan scan = new Scan();
		scan.setCaching(500); // 1 is the default in Scan, which will be bad for MapReduce jobs
		scan.setCacheBlocks(false); // don't set to true for MR jobs
		// set other scan attrs

		TableMapReduceUtil.initTableMapperJob(
				"test", // input HBase table name
				scan, // Scan instance to control CF and attribute selection
				MyMapper.class, // mapper
				null, // mapper output key
				null, // mapper output value
				job);
		job.setOutputFormatClass(NullOutputFormat.class); // because we aren't emitting anything from mapper

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
