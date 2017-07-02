package com.github.yangwk.more.demo.javase.swing.atm.dao;

public interface IBankCardDAO {
	//登录
	public BankCard loginCheck(BankCard bankCard);
	//取款
	public boolean getMoney(double money, String cardId);
	//存款
	public boolean addMoney(double money, String cardId);
	//查余额
	public double lookMoney(String cardId);
	//转账
	public boolean outputMoney(double money,BankCard srcBankCard,String desCardId);
	//修改密码
	public BankCard alterPassword(BankCard bankCard, String newCardPassword);
	//查看户主
	public String lookMaster(String cardId);
}
