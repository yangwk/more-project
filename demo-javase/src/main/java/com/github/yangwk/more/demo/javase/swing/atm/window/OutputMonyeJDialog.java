package com.github.yangwk.more.demo.javase.swing.atm.window;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.github.yangwk.more.demo.javase.swing.atm.dao.BankCard;
import com.github.yangwk.more.demo.javase.swing.atm.dao.DAOFactory;

public class OutputMonyeJDialog extends JDialog{


	private static final long serialVersionUID = 1L;
	private JTextField desAccountJTextFied;
	private JTextField desNameJTextField;
	private JTextField outMoneyJTextField;
	private JPasswordField passwordJPasswordField;
	private JButton ensureJButton;
	private JButton returnJButton;
	private int width = ATMTool.JDialogWidth;
	private int height = ATMTool.JDialogHeight;
	private CardLayout cardLayout;
	private Container container;
	private JPanel centerJPanel;
	private int currentCard = 1;
	private ATMApplicationJFrame atmApplicationJFrame;
	private OutputMonyeJDialog outputMonyeJDialog;
	private MyGlassPane myGlassPane;
	private String desCardId;
	private String masterName;
	private double outputMoney;
	private String cardPassword;
	public OutputMonyeJDialog( ATMApplicationJFrame atmApplicationJFrame ){
		super();
		super.setVisible(false);
		this.atmApplicationJFrame = atmApplicationJFrame;
		myGlassPane = new MyGlassPane(this);
		initComponent();
		this.setUndecorated(true);
		this.setSize(width, height);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		ATMTool.setScreenCenterPosition(this, width, height);
		
//		super.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
		super.setVisible(true);
		
		ATMTool.setAllJComponentsFontAndCursor(this);
		
		cardLayout.first(centerJPanel);
		
		outputMonyeJDialog = this;
	}
	
	private void initComponent(){
		container = this.getContentPane();
		
		JPanel southJPanel = new JPanel();
		southJPanel.setBackground(ATMTool.JDialogBackground);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER,20,20);
		southJPanel.setLayout(flowLayout);
		
		MyActionListener myActionListener = new MyActionListener();
		
		ensureJButton = new JButton("下一步");
		ensureJButton.addActionListener(myActionListener);
		
		returnJButton = new JButton("返回主界面");
		returnJButton.addActionListener(myActionListener);
		
		southJPanel.add(ensureJButton);
		southJPanel.add(returnJButton);
		
		container.add(southJPanel,BorderLayout.SOUTH);
		//
		centerJPanel = new JPanel();
		cardLayout = new CardLayout();
		centerJPanel.setLayout(cardLayout);
		
		int jLabelWidth = 130;	//jlabel统一宽度
		int jTextFieldWith = 160;	//jtextfield统一宽度
		int sameHeight = 30;	//统一高度
		//1
		JPanel desAccountJPanel = new JPanel();
		desAccountJPanel.setBackground(ATMTool.JDialogBackground);
		desAccountJPanel.setLayout(flowLayout);
		
		JLabel desAccountJLabel = new JLabel("要转账到的银行卡号");
		desAccountJLabel.setPreferredSize(new Dimension(jLabelWidth, sameHeight));
		desAccountJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		MyMouseListener myMouseListener = new MyMouseListener();
		
		desAccountJTextFied = new JTextField();
		desAccountJTextFied.setPreferredSize(new Dimension(jTextFieldWith, sameHeight));
		desAccountJTextFied.setEditable(false);
		desAccountJTextFied.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		desAccountJTextFied.setForeground(Color.blue);
		desAccountJTextFied.setBackground(Color.white);
		desAccountJTextFied.addMouseListener(myMouseListener);
		
		desAccountJPanel.add(desAccountJLabel);
		desAccountJPanel.add(desAccountJTextFied);
		//2
		JPanel desNameJPanel = new JPanel();
		desNameJPanel.setBackground(ATMTool.JDialogBackground);
		desNameJPanel.setLayout(flowLayout);
		
		JLabel desNameJLabel = new JLabel("要转账到的户主姓名");
		desNameJLabel.setPreferredSize(new Dimension(jLabelWidth, sameHeight));
		desNameJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		desNameJTextField = new JTextField();
		desNameJTextField.setPreferredSize(new Dimension(jTextFieldWith, sameHeight));
		desNameJTextField.setEditable(false);
		desNameJTextField.setForeground(Color.blue);
		
		desNameJPanel.add(desNameJLabel);
		desNameJPanel.add(desNameJTextField);
		//3
		JPanel outMoneyJPanel = new JPanel();
		outMoneyJPanel.setBackground(ATMTool.JDialogBackground);
		outMoneyJPanel.setLayout(flowLayout);
		
		JLabel outMoneyJLabel = new JLabel("转账金额");
		outMoneyJLabel.setPreferredSize(new Dimension(jLabelWidth, sameHeight));
		outMoneyJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		outMoneyJTextField = new JTextField();
		outMoneyJTextField.setPreferredSize(new Dimension(jTextFieldWith, sameHeight));
		outMoneyJTextField.setEditable(false);
		outMoneyJTextField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		outMoneyJTextField.setForeground(Color.blue);
		outMoneyJTextField.setBackground(Color.white);
		outMoneyJTextField.addMouseListener(myMouseListener);
		
		outMoneyJPanel.add(outMoneyJLabel);
		outMoneyJPanel.add(outMoneyJTextField);
		//4
		JPanel passwordJPanel = new JPanel();
		passwordJPanel.setBackground(ATMTool.JDialogBackground);
		passwordJPanel.setLayout(flowLayout);
		
		JLabel passwordJLabel = new JLabel("输入密码");
		passwordJLabel.setPreferredSize(new Dimension(jLabelWidth, sameHeight));
		passwordJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		passwordJPasswordField = new JPasswordField();
		passwordJPasswordField.setPreferredSize(new Dimension(jTextFieldWith, sameHeight));
		passwordJPasswordField.setEditable(false);
		passwordJPasswordField.setBackground(Color.white);
		passwordJPasswordField.addMouseListener(myMouseListener);
		
		passwordJPanel.add(passwordJLabel);
		passwordJPanel.add(passwordJPasswordField);
		//
		centerJPanel.add(desAccountJPanel,"1");
		centerJPanel.add(desNameJPanel,"2");
		centerJPanel.add(outMoneyJPanel,"3");
		centerJPanel.add(passwordJPanel,"4");
	
		container.add(centerJPanel,BorderLayout.CENTER);
	}
	
	private class MyMouseListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			if( currentCard == 1 ){
				new KeyboardJDialog(desAccountJTextFied,ATMTool.AccountLength, outputMonyeJDialog );
			}
			else if(currentCard == 3 ){
				new KeyboardJDialog(outMoneyJTextField,ATMTool.MoneyLength, outputMonyeJDialog );
			}
			else if(currentCard == 4){
				new KeyboardJDialog(passwordJPasswordField,ATMTool.PasswordLength, outputMonyeJDialog );
			}
		}

	}
	
	private class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if(source == ensureJButton){
				if( currentCard == 1 ){
					String account = desAccountJTextFied.getText();
					if(account == null || ! account.matches( ATMTool.AccountPattern ) ){
						ATMTool.showModalMessageDialog(outputMonyeJDialog, ATMTool.AccountErrorHint );
						return ;
					}
					if(account.equals(atmApplicationJFrame.getBankCard().getCardId()) ){
						ATMTool.showModalMessageDialog(outputMonyeJDialog, ATMTool.OutputMoneySameAccountHint );
						return ;
					}
					myGlassPane.showGlassPane();
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(ATMTool.GlassPaneShowTime);
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
							String account = desAccountJTextFied.getText();
							String name = DAOFactory.getIBankCardDAOSingleInstance().lookMaster(account);
							if(name == null){
								myGlassPane.hideGlassPane();
								ATMTool.showModalMessageDialog(outputMonyeJDialog, "银行卡号:"+account+"\n不存在" );
							}
							else{
								myGlassPane.hideGlassPane();
								desNameJTextField.setText(name);
								desCardId = account;	//全局的卡号
								masterName = name;	//全局的户主姓名
								currentCard ++;
								cardLayout.next(centerJPanel);
							}
						}
					}).start();
				}
				else if( currentCard == 2 ){
					currentCard ++;
					cardLayout.next(centerJPanel);
				}
				else if(currentCard == 3 ){
					String text = outMoneyJTextField.getText();
					if(text == null || ! text.matches( ATMTool.MoneyPattern ) ){
						ATMTool.showModalMessageDialog(outputMonyeJDialog, ATMTool.AllMoneyErrorHint );
						return ;
					}
					double money = Double.parseDouble(text);
					if(money <= 0.0D){
						ATMTool.showModalMessageDialog(outputMonyeJDialog, ATMTool.AllMoneyErrorHint );
						return ;
					}
					outputMoney = money;	//全局的金额
					currentCard ++;
					cardLayout.next(centerJPanel);
					ensureJButton.setText("确认转账");
				}
				else if(currentCard == 4){
					char[] chars = passwordJPasswordField.getPassword();
					if(chars == null ){
						ATMTool.showModalMessageDialog(outputMonyeJDialog, ATMTool.PasswordErrorHint );
						return ;
					}
					String psd = new String(chars);
					if( ! psd.matches( ATMTool.PasswordPattern) ){
						ATMTool.showModalMessageDialog(outputMonyeJDialog, ATMTool.PasswordErrorHint );
						return ;
					}
					int result = ATMTool.showModalConfirmDialog(outputMonyeJDialog, 
							"您要转账到的银行卡号信息如下:\n银行卡号:"+desCardId+"\n户主姓名:"+masterName+"\n转账金额:"+ATMTool.RMBFormat(outputMoney)+"\n确定要转账吗?" );
					if(result != ATMTool.YES){
						return ;
					}
					cardPassword = psd;	//全局的密码
					myGlassPane.showGlassPane();
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(ATMTool.GlassPaneShowTime);
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
							BankCard bankCard = atmApplicationJFrame.getBankCard();
							bankCard.setCardPassword(cardPassword);	//用户输入的自己密码
							boolean success = DAOFactory.getIBankCardDAOSingleInstance().outputMoney(outputMoney, bankCard, desCardId);
							if(success){
								myGlassPane.hideGlassPane();
								ATMTool.showModalMessageDialog(outputMonyeJDialog, "转账成功\n已转账到的银行卡号是:"+desCardId+"\n转账金额是:"+ATMTool.RMBFormat(outputMoney) );
								dispose();
								atmApplicationJFrame.setVisible(true);
							}
							else{
								myGlassPane.hideGlassPane();
								ATMTool.showModalMessageDialog(outputMonyeJDialog, ATMTool.OutputMoneyFailHint );
								dispose();
								atmApplicationJFrame.setVisible(true);
							}
						}
					}).start();
//					currentCard = 1;
//					ensureJButton.setText("下一步");
//					cardLayout.next(centerJPanel);
				}
			}
			else if(source == returnJButton ){
				int result = ATMTool.showModalConfirmDialog(outputMonyeJDialog, ATMTool.CancelOutputMoneyHint );
				if(result == ATMTool.YES){
					dispose();
					atmApplicationJFrame.setVisible(true);
				}
			}
		}
	}
}
