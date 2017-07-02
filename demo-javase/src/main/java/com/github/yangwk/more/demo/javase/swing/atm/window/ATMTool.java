package com.github.yangwk.more.demo.javase.swing.atm.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ATMTool {
	//glasspane一定要显示的时间
	public static final long GlassPaneShowTime = 1000;	//1000毫秒,不应超过GlassPane的超时时间
	//子功能窗体的统一宽度,高度
	public static final int JDialogWidth = 450;
	public static final int JDialogHeight = 450;
	//子功能窗体统一背景色
	public static final Color JDialogBackground = Color.yellow;
	//取款和存款的金额限定
	public static final double RatedGetAddMoney = 100.0D;
	//确认对话框选项
	public static final int YES = JOptionPane.YES_OPTION;
	public static final int NO = JOptionPane.NO_OPTION;
	//账号长度
	public static final int AccountLength = 20;
	//密码长度
	public static final int PasswordLength = 6;
	//金钱可输入长度
	public static final int MoneyLength = 15;
	//金钱正则表达式
	public static final String MoneyPattern = "^([0-9]|([1-9]+[0-9]+))\\.[0-9]{0,2}$";
	//账号正则表达式
	public static final String AccountPattern = "^[0-9]{20}$";
	//密码正则表达式
	public static final String PasswordPattern = "^[0-9]{6}$";
	//登录失败
	public static final String LoginFailHint = "登录失败";
	//登录达到3次
	public static final String LoginOverHint = "登录失败达到3次,退卡";
	//安全提醒
	public static final String SafeHint = "请确认周边环境是否安全";
	//账号提示
	public static final String AccountErrorHint = "银行卡号必须为"+ATMTool.AccountLength+"个数字";
	//密码提示
	public static final String PasswordErrorHint = "密码必须为"+ATMTool.PasswordLength+"个数字";
	//取款和存款的金额提示
	public static final String GetAddMoneyErrorHint = "金钱必须是100的倍数,格式如下:\n100.0\n200.00";
	//转账,通用的金额提示
	public static final String AllMoneyErrorHint = "金钱必须保留小数,格式如下:\n0.35\n9.60\n100.0\n1000.00";
	//取款成功
	public static final String GetMoneySuccessHint = "取款成功";
	//取款失败
	public static final String GetMoneyFailHint = "余额不足,取款失败";
	//取消存款提示
	public static final String CancelAddMoneyHint = "取消存款,\n不会对您的钱财造成任何损失,\n确定取消存款吗?";
	//决定取消存款
	public static final String CancelAddMoneyOKHint = "放入的金钱,请从接收口取出";
	//存款成功
	public static final String AddMoneySuccessHint = "存款成功";
	//存款失败
	public static final String AddMoneyFailHint = "操作意外中断,存款失败\n若对您的钱财造成损失,\n请拨打客服热线:95516";
	//查询失败
	public static final String LookMoneyFailHint = "查询意外中断,请重新查询";
	//取消修改密码
	public static final String CancelAlterPasswordHint = "取消修改密码,不会对您原来的密码造成任何影响\n确定取消密码修改吗?";
	//修改密码提示
	public static final String AlterPasswordHint = "确定要修改密码吗?";
	//修改密码成功
	public static final String AlterPasswordSuccessHint = "修改密码成功";
	//修改密码失败
	public static final String AlterPasswordFailHint = "密码错误";
	//修改密码要求提示
	public static final String AlterPasswordRequireHint = "密码不能为空,新密码两次输入要一致";
	//转账失败
	public static final String OutputMoneyFailHint = "转账失败\n密码错误,或余额不足";
	//取消转账
	public static final String CancelOutputMoneyHint = "取消转账,不会对您的钱财造成任何损失\n确定取消转账吗?";
	//转账与本人卡号相同
	public static final String OutputMoneySameAccountHint = "要转账到的银行卡号,不能与本人卡号相同";
	
	private static final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	
	public static void setAllJComponentsFontAndCursor(Container container){
		int count = container.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component component = container.getComponent(i);
			if( component instanceof JButton || component instanceof JTextField ){
				component.setCursor( handCursor );
			}
			if(component instanceof JComponent ){
				component.setFont(component.getFont().deriveFont(Font.PLAIN));
			}
			if( component instanceof Container ){
				setAllJComponentsFontAndCursor((Container) component);
			}
		}
	}
	
	public static void setScreenCenterPosition(Component component, int width,int height){
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = ( dimension.width - width ) / 2;
		int y = ( dimension.height - height ) / 2;
		component.setBounds(x, y, width, height);
	}

	public static void showModalMessageDialog(Window window, String message){
		JOptionPane.showMessageDialog( window, message,"信息",JOptionPane.WARNING_MESSAGE);
	}
	
	public static int showModalConfirmDialog(Window window, String message){
		return JOptionPane.showConfirmDialog( window, message, "提示", JOptionPane.YES_NO_OPTION);
	}
	
	public static String RMBFormat(double money){
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
		String rmb = numberFormat.format(money);
		return rmb;
	}
	
}
