package com.github.yangwk.more.demo.hbase.filter;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class FilterListDemo {

	public static void main(String[] args){
		Configuration conf = HBaseConfiguration.create();

		Connection connection = null;
		Table table = null;
		try {
			connection = ConnectionFactory.createConnection(conf);
			table = connection.getTable(TableName.valueOf("test"));
			
			FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
			SingleColumnValueFilter filter1 = new SingleColumnValueFilter(
					Bytes.toBytes(args[0]), 
					Bytes.toBytes(args[1]),
					CompareOp.EQUAL, 
					Bytes.toBytes(args[2]));

			Scan scan = new Scan();
			scan.setCaching(500);
			scan.setCacheBlocks(false); // don't set to true for MR jobs
			
			filterList.addFilter(filter1);
			scan.setFilter(filterList);
			
			ResultScanner rs = table.getScanner(scan);
			try {
				for (Result result = rs.next(); result != null; result = rs.next()) {
					byte[] row = result.getRow();
					System.out.println("row:");
					System.out.println(Bytes.toString(row));
					for (Cell cell : result.rawCells()) {
						byte[] cf = CellUtil.cloneFamily(cell);
						byte[] cq = CellUtil.cloneQualifier(cell);
						byte[] cv = CellUtil.cloneValue(cell);
						System.out.println(Bytes.toString(cf)+":"+Bytes.toString(cq));
						System.out.println(Bytes.toString(cv));
					}
				}
			} finally {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(table != null)
				try {table.close();} catch (IOException e) {}
			if(connection != null)
				try {connection.close();} catch (IOException e) {}
		}
	}
}
