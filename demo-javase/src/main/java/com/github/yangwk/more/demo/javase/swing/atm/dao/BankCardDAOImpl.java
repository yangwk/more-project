package com.github.yangwk.more.demo.javase.swing.atm.dao;

import java.sql.Connection;

public class BankCardDAOImpl implements IBankCardDAO{
	
	private Connection connection;
	
	public BankCardDAOImpl(Connection connection){
		this.connection = connection;
	}

	@Override
	public BankCard loginCheck(BankCard bankCard){
		return bankCard;
	}

	@Override
	public boolean getMoney(double money,String cardId){
		return connection != null;
	}

	@Override
	public boolean addMoney(double money,String cardId) {
		return true;
	}

	@Override
	public double lookMoney(String cardId) {
		return 0d;
	}

	@Override
	public boolean outputMoney(double money, BankCard srcBankCard,String desCardId) {
		return true;
	}

	@Override
	public BankCard alterPassword(BankCard bankCard, String newCardPassword) {
		return bankCard;
	}

	@Override
	public String lookMaster(String cardId) {
		return "cardMasterName";
	}

}
