package com.github.yangwk.more.demo.javase.swing.atm.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlDatabaseConnection {
	
	private Connection connection = null;
	
	public MySqlDatabaseConnection() throws ClassNotFoundException, SQLException{
	}
	
	public Connection getConnection() {
		return this.connection;
	}

	public void close() throws SQLException {
		if(this.connection != null)
			this.connection.close();
	}

}
