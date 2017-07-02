package com.github.yangwk.more.demo.javase.swing.atm.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.github.yangwk.more.demo.javase.swing.atm.dao.BankCard;
import com.github.yangwk.more.demo.javase.swing.atm.dao.DAOFactory;
import com.github.yangwk.more.demo.javase.swing.atm.dao.IBankCardDAO;

public class ATMApplicationJFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel westJPanel;
	private JPanel eastJPanel;
	private JPanel centerJPanel;
	private JButton getMoneyJButton; // 取款
	private JButton addMoneyJButton; // 存款
	private JButton lookMoneyJButton; // 查余额
	private JButton outputMonyeJButton; // 转账
	private JButton alterPasswordJButton; // 修改密码
	private JButton exitJButton; // 取卡
	private JTextField accountJTextField;
	private JLabel welcomeJLabel;
	private JLabel hintJLabel;
	private MyGlassPane myGlassPane;
	private BankCard bankCard;
	private ATMApplicationJFrame atmApplicationJFrame;
	public ATMApplicationJFrame( BankCard bankCard ) {
		super();
		this.bankCard = bankCard;
		
		this.setTitle("ATM系统");
		this.setVisible(true);
		this.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );	//不能关闭
		myGlassPane = new MyGlassPane(this);
		int width = 600;
		int height = 400;

		ATMTool.setScreenCenterPosition(this, width, height);
		this.setMinimumSize(new Dimension(width, height));

		initComponent();

		ATMTool.setAllJComponentsFontAndCursor(this);
		
		atmApplicationJFrame = this;
	}

	private void initComponent() {
		int uniteWidth = 150;
		westJPanel = new JPanel();
		westJPanel.setPreferredSize(new Dimension(uniteWidth, 0));
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		westJPanel.setLayout(gridbag);
		
		MyActionListener myActionListener = new MyActionListener();
		
		addMoneyJButton = new JButton("存款");
		addMoneyJButton.addActionListener(myActionListener);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		Dimension dimension = addMoneyJButton.getPreferredSize();
		c.ipady = uniteWidth - dimension.height;
		gridbag.setConstraints(addMoneyJButton, c);
		
		outputMonyeJButton = new JButton("转账");
		outputMonyeJButton.addActionListener(myActionListener);
		dimension = outputMonyeJButton.getPreferredSize();
		c.ipady = uniteWidth - dimension.height;
		gridbag.setConstraints(outputMonyeJButton, c);

		westJPanel.add(addMoneyJButton);
		westJPanel.add(outputMonyeJButton);

		//
		eastJPanel = new JPanel();
		eastJPanel.setPreferredSize(new Dimension(uniteWidth, 0));
		eastJPanel.setLayout(gridbag);
		
		lookMoneyJButton = new JButton("查询余额");
		lookMoneyJButton.addActionListener(myActionListener);
		dimension = lookMoneyJButton.getPreferredSize();
		c.ipady = uniteWidth - dimension.height;
		gridbag.setConstraints(lookMoneyJButton, c);
		
		getMoneyJButton = new JButton("取款");
		getMoneyJButton.addActionListener(myActionListener);
		dimension = getMoneyJButton.getPreferredSize();
		c.ipady = uniteWidth - dimension.height;
		gridbag.setConstraints(getMoneyJButton, c);

		eastJPanel.add(lookMoneyJButton);
		eastJPanel.add(getMoneyJButton);
		//
		centerJPanel = new JPanel();
		centerJPanel.setLayout(gridbag);
		
		hintJLabel = new JLabel(ATMTool.SafeHint);
		hintJLabel.setForeground(Color.red);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 0, 10, 10);
		c.weightx = 0;
		c.weighty = 0;
		c.ipady = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(hintJLabel, c);
		
		welcomeJLabel = new JLabel("您的银行卡号是:");
		gridbag.setConstraints(welcomeJLabel, c);
		
		accountJTextField = new JTextField();
		accountJTextField.setText( bankCard.getCardId() );	//显示卡号
		Color backGround = centerJPanel.getBackground();
		accountJTextField.setBackground( backGround );
		accountJTextField.setBorder(new EmptyBorder(2,2,2,2));
		accountJTextField.setEditable(false);
		accountJTextField.setForeground(Color.blue);
		gridbag.setConstraints(accountJTextField, c);
		
		alterPasswordJButton = new JButton("修改密码");
		alterPasswordJButton.addActionListener(myActionListener);
		dimension = alterPasswordJButton.getPreferredSize();
		c.ipadx = 90 - dimension.width;
		c.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(alterPasswordJButton, c);
		
		exitJButton = new JButton("退卡");
		exitJButton.addActionListener(myActionListener);
		dimension = exitJButton.getPreferredSize();
		c.ipadx = 90 - dimension.width;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(exitJButton, c);
		
		centerJPanel.add(hintJLabel);
		centerJPanel.add(welcomeJLabel);
		centerJPanel.add(accountJTextField);
		centerJPanel.add(alterPasswordJButton);
		centerJPanel.add(exitJButton);
		//
		Container container = this.getContentPane();
		container.add(westJPanel, BorderLayout.WEST);
		container.add(eastJPanel, BorderLayout.EAST);
		container.add(centerJPanel, BorderLayout.CENTER);
	}
	
	private class MyActionListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if(source == getMoneyJButton){
				setVisible(false);
				new GetMoneyJDialog( atmApplicationJFrame );
			}
			else if(source == addMoneyJButton){
				setVisible(false);
				new AddMoneyJDialog( atmApplicationJFrame );
			}
			else if(source == lookMoneyJButton){
				myGlassPane.showGlassPane();	//友好提示
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep( ATMTool.GlassPaneShowTime );	//延迟
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
						IBankCardDAO iBankCardDAO = DAOFactory.getIBankCardDAOSingleInstance();
						double lookMoney = iBankCardDAO.lookMoney( getBankCard().getCardId() );
						if( lookMoney < 0D ){
							myGlassPane.hideGlassPane();
							ATMTool.showModalMessageDialog(atmApplicationJFrame, ATMTool.LookMoneyFailHint );
						}
						else{
							bankCard.setCardMoney(lookMoney);	//必须保存查询的余额,由lookmoneyjdialog获取
							myGlassPane.hideGlassPane();
							setVisible(false);
							new LookMoneyJDialog(atmApplicationJFrame);
						}
					}
				}).start();
			}
			else if(source == outputMonyeJButton){
				setVisible(false);
				new OutputMonyeJDialog( atmApplicationJFrame );
			}
			else if(source == alterPasswordJButton){
				setVisible(false);
				new AlterPasswordJDialog( atmApplicationJFrame );
			}
			else if(source == exitJButton){
				dispose();
			}
		}
	}
	
	public BankCard getBankCard(){
		return this.bankCard;
	}
	
	public void setBankCard(BankCard bankCard){
		this.bankCard = bankCard;
	}
	
}
