package com.github.yangwk.more.demo.javase.swing.atm.window;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class LookMoneyJDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField accountJTextField;
	private JTextField rmbJTextField;
	private JButton returnJButton;
	private int width = ATMTool.JDialogWidth;
	private int height = ATMTool.JDialogHeight;
	private ATMApplicationJFrame atmApplicationJFrame;
	public LookMoneyJDialog(ATMApplicationJFrame atmApplicationJFrame){
		super();
		super.setVisible(false);
		this.atmApplicationJFrame = atmApplicationJFrame;
		initComponent();
		this.setUndecorated(true);
		this.setSize(width, height);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		ATMTool.setScreenCenterPosition(this, width, height);
		
//		super.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
		super.setVisible(true);
		
		ATMTool.setAllJComponentsFontAndCursor(this);
	}
	
	private void initComponent(){
		Container container = this.getContentPane();
		container.setBackground(ATMTool.JDialogBackground);
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		container.setLayout(gridbag);
		
		c.anchor = GridBagConstraints.CENTER; 
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(50,10,50,10);
		JLabel accountJLabel = new JLabel("银行卡号");
		accountJLabel.setForeground(Color.blue);
		accountJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.ipadx = 60 - accountJLabel.getPreferredSize().width;
		gridbag.setConstraints( accountJLabel, c);
		container.add(accountJLabel );
		
		accountJTextField = new JTextField( atmApplicationJFrame.getBankCard().getCardId() );
		accountJTextField.setEditable(false);
		accountJTextField.setForeground(Color.black);
		accountJTextField.setPreferredSize(new Dimension(180, 30));
		c.ipadx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints( accountJTextField, c);
		container.add(accountJTextField );
		
		c.gridwidth = 1;
		JLabel haveJLabel = new JLabel("余额");
		haveJLabel.setForeground(Color.blue);
		haveJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.ipadx = 60 - haveJLabel.getPreferredSize().width;
		gridbag.setConstraints( haveJLabel, c);
		container.add(haveJLabel );
		
		String moneyStr = ATMTool.RMBFormat( atmApplicationJFrame.getBankCard().getCardMoney() );
		rmbJTextField = new JTextField( moneyStr );
		rmbJTextField.setEditable(false);
		rmbJTextField.setForeground(Color.black);
		rmbJTextField.setPreferredSize(new Dimension(180, 30));
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.ipadx = 0;
		gridbag.setConstraints( rmbJTextField, c);
		container.add(rmbJTextField );
		
		MyActionListener myActionListener = new MyActionListener();
		
		returnJButton = new JButton("返回主界面");
		returnJButton.addActionListener(myActionListener);
		gridbag.setConstraints( returnJButton, c);
		container.add(returnJButton );
	}


	private class MyActionListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if(source == returnJButton){
				dispose();
				atmApplicationJFrame.setVisible(true);
			}
		}
	}
	
}
