package com.github.yangwk.more.demo.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcClient {
	static final String driverClass = "org.apache.hive.jdbc.HiveDriver";
	static final String host$port = "192.168.10.101:10000";
	static final String connectUrl = "jdbc:hive2://" + host$port;
	static final String user = "ywk";
	// The <password> field value is ignored in non-secure mode
	static final String password = "";
	
	Connection getConnection(){
		Connection cnct = null;
		try {
			Class.forName(driverClass);
			
			cnct = DriverManager.getConnection("jdbc:hive2://" + host$port, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnct;
	}

	void executeQuery(String sql){
		Connection cnct = null;
		try {
			cnct = getConnection();
			Statement stmt = cnct.createStatement();
			ResultSet rset = stmt.executeQuery(sql);
			try{
				int cnt = 0;
				if(rset.next()){
					int colCnt = rset.getMetaData().getColumnCount();
					do{
						cnt ++;
						for(int r=0; r<colCnt; r++){
							Object obj = rset.getObject(r+1);
							
							System.out.println(obj==null ? "null" : obj.toString());
						}
					}while(rset.next());
				}
				System.out.println("all count:"+cnt);
			}finally{
				if(rset != null)
					try {rset.close();} catch (SQLException e) {}
				if(stmt != null)
					try {stmt.close();} catch (SQLException e) {}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(cnct != null)
				try {cnct.close();} catch (SQLException e) {}
			
		}
	}

	void test(){
		executeQuery("select * from pokes_groupby");
	}

	public static void main(String[] args) {
		HiveJdbcClient client = new HiveJdbcClient();
		client.test();
	}
	
}
