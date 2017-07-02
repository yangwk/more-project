package com.github.yangwk.more.demo.javase.swing.atm.window;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.github.yangwk.more.demo.javase.swing.atm.dao.BankCard;
import com.github.yangwk.more.demo.javase.swing.atm.dao.DAOFactory;
import com.github.yangwk.more.demo.javase.swing.atm.dao.IBankCardDAO;

public class AtmLoginJFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private JButton loginJButton ;
	private JButton cancelJButton ;
	private JLabel accountHintJLabel;
	private JLabel passwordHintJLabel;
	private JTextField accountJTextField;
	private JPasswordField passwordJPasswordField;
	private JLabel accountJLabel;
	private JLabel passwordJLabel;
	private JLabel hintJLabel;
	private MyGlassPane myGlassPane;
	private int uniteWidth = 240;
	private int uniteHeight = 25;
	private int Vgap = 20;
	private int Hgap = 10;
	private boolean useKeyboard ;	//是否用ATM键盘
	private int loginCount = 0;	//登录次数
	public AtmLoginJFrame(){
		super();
		this.useKeyboard = true ;
		makeWindow();
	}
	
	public AtmLoginJFrame(boolean useKeyboard){
		super();
		this.useKeyboard = useKeyboard;
		makeWindow();
	}
	
	private void makeWindow(){
		myGlassPane = new MyGlassPane(this);
		this.setVisible(true);
		this.setTitle("欢迎使用ATM系统--请登录");
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		Insets insets = this.getInsets();
		int width = 300;
		int height = insets.top + (uniteHeight+Hgap)*6 + Hgap + insets.bottom;
		ATMTool.setScreenCenterPosition(this, width, height);
		
		initComponent();
		
		ATMTool.setAllJComponentsFontAndCursor(this);
	}
	
	private void initComponent(){
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER,Vgap,Hgap);
		this.setLayout( flowLayout );
		
		hintJLabel = new JLabel( ATMTool.SafeHint );
		hintJLabel.setForeground(Color.red);
		hintJLabel.setPreferredSize(new Dimension(uniteWidth, uniteHeight));
		hintJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		accountJLabel = new JLabel("银行卡号");
		accountJLabel.setPreferredSize(new Dimension(60, uniteHeight));
		accountJLabel.setForeground(Color.blue);
		accountJLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		MyMouseListener myFocusListener = null;
		if(useKeyboard){
			myFocusListener = new MyMouseListener(this);
		}
		
		accountJTextField = new JTextField();
		accountJTextField.setPreferredSize(new Dimension(160, uniteHeight));
		accountJTextField.setBackground(Color.white);
		if(useKeyboard){
			accountJTextField.setEditable(false);
			accountJTextField.addMouseListener(myFocusListener);
		}
		
		accountHintJLabel = new JLabel();
		accountHintJLabel.setPreferredSize(new Dimension(uniteWidth, uniteHeight));
		accountHintJLabel.setForeground(Color.red);
		accountHintJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		passwordJLabel = new JLabel("输入密码");
		passwordJLabel.setPreferredSize(new Dimension(60, uniteHeight));
		passwordJLabel.setForeground(Color.blue);
		passwordJLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		passwordJPasswordField = new JPasswordField();
		passwordJPasswordField.setPreferredSize(new Dimension(160, uniteHeight));
		passwordJPasswordField.setBackground(Color.white);
		if( useKeyboard ){
			passwordJPasswordField.setEditable(false);
			passwordJPasswordField.addMouseListener(myFocusListener);
		}
		
		passwordHintJLabel = new JLabel();
		passwordHintJLabel.setPreferredSize(new Dimension(uniteWidth, uniteHeight));
		passwordHintJLabel.setForeground(Color.red);
		passwordHintJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		ActionListener actionListener = new MyActionListener();
		
		loginJButton = new JButton("登录");
		loginJButton.setPreferredSize(new Dimension(60, uniteHeight));
		loginJButton.addActionListener(actionListener);
		
		cancelJButton = new JButton("取消");
		cancelJButton.setPreferredSize(new Dimension(60, uniteHeight));
		cancelJButton.addActionListener(actionListener);
		
		Container contentPane = this.getContentPane();
		contentPane.add(hintJLabel);
		contentPane.add( accountJLabel );
		contentPane.add(accountJTextField);
		contentPane.add(accountHintJLabel);
		contentPane.add(passwordJLabel);
		contentPane.add(passwordJPasswordField);
		contentPane.add(passwordHintJLabel);
		contentPane.add(loginJButton);
		contentPane.add(cancelJButton);
		
	}
	
	private class MyActionListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if(source == loginJButton){	//登录
				if( checkInformation() ){
					myGlassPane.showGlassPane();	//友好提示
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep( ATMTool.GlassPaneShowTime );	//延迟1秒,以可以看到glasspane
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
							loginCount ++;	//次数增加
							IBankCardDAO iBankCardDAO = DAOFactory.getIBankCardDAOSingleInstance();
							BankCard bankCard = new BankCard();
							bankCard.setCardId( accountJTextField.getText() );
							bankCard.setCardPassword( new String( passwordJPasswordField.getPassword() ) );
							bankCard = iBankCardDAO.loginCheck( bankCard );
							if(bankCard != null){
								myGlassPane.hideGlassPane();
								new ATMApplicationJFrame( bankCard );
								dispose();
							}
							else{
								if(loginCount >= 3){	//第3次登录失败
									myGlassPane.hideGlassPane();
									myGlassPane.showOpaqueStatusText(ATMTool.LoginOverHint, null);
									try {
										Thread.sleep( ATMTool.GlassPaneShowTime * 3 );	//延时
									} catch (InterruptedException e) {
										throw new RuntimeException(e);
									}
									dispose();	//退出
								}
								else{
									myGlassPane.hideGlassPane();
									ATMTool.showModalMessageDialog(getOwner(), ATMTool.LoginFailHint );
								}
							}
						}
					}).start();
				}
			}
			else if(source == cancelJButton){	//取消
				dispose();
			}
		}
	}
	
	private boolean checkInformation(){
		String account = accountJTextField.getText();
		if(account == null || ! account.matches( ATMTool.AccountPattern )){
			accountHintJLabel.setText( ATMTool.AccountErrorHint );
			return false;
		}
		accountHintJLabel.setText("");	//不显示
		char[] chars = passwordJPasswordField.getPassword();
		if(chars == null){
			passwordHintJLabel.setText( ATMTool.PasswordErrorHint );
			return false;
		}
		String password = new String(chars);
		if( ! password.matches( ATMTool.PasswordPattern ) ){
			passwordHintJLabel.setText( ATMTool.PasswordErrorHint );
			return false;
		}
		passwordHintJLabel.setText("");	//不显示
		return true;
	}
	
	private class MyMouseListener extends MouseAdapter{
		private AtmLoginJFrame atmLoginJFrame;
		public MyMouseListener(AtmLoginJFrame atmLoginJFrame){
			this.atmLoginJFrame = atmLoginJFrame;
		}
		@Override
		public void mousePressed(MouseEvent e) {
			Object source = e.getSource();
			if(source == accountJTextField){
				new KeyboardJDialog(accountJTextField,ATMTool.AccountLength, atmLoginJFrame);
			}
			else if(source == passwordJPasswordField){
				new KeyboardJDialog(passwordJPasswordField,ATMTool.PasswordLength, atmLoginJFrame);
			}
		}

	}


}
