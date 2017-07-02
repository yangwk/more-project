package com.github.yangwk.more.demo.javase.swing.atm.dao;

import java.sql.Connection;

import com.github.yangwk.more.demo.javase.swing.atm.jdbc.MySqlDatabaseConnection;

public class DAOFactory {
	
	private static final IBankCardDAO singleBankCardDAO;
	static{
		singleBankCardDAO = getIBankCardDAOInstance();
	}
	
	private static IBankCardDAO getIBankCardDAOInstance(){
		IBankCardDAO bankCardDAO = null;
		try {
			MySqlDatabaseConnection mySqlDatabaseConnection = new MySqlDatabaseConnection();
			Connection connection = mySqlDatabaseConnection.getConnection();
			bankCardDAO = new BankCardDAOImpl(connection);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return bankCardDAO;
	}
	
	public static IBankCardDAO getIBankCardDAOSingleInstance(){
		return singleBankCardDAO;
	}
	
}
