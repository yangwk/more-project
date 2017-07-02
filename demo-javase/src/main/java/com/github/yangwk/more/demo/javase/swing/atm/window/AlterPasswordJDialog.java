package com.github.yangwk.more.demo.javase.swing.atm.window;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.github.yangwk.more.demo.javase.swing.atm.dao.BankCard;
import com.github.yangwk.more.demo.javase.swing.atm.dao.DAOFactory;
import com.github.yangwk.more.demo.javase.swing.atm.dao.IBankCardDAO;

public class AlterPasswordJDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private JPasswordField oldPsdJPasswordField;
	private JPasswordField newPsdJPasswordField;
	private JPasswordField againNewPswJPasswordField;
	private JButton returnJButton;
	private JButton ensureJButton;
	private int width = ATMTool.JDialogWidth;
	private int height = ATMTool.JDialogHeight;
	private ATMApplicationJFrame atmApplicationJFrame;
	private MyGlassPane myGlassPane;
	private String newCardPassword ;
	public AlterPasswordJDialog(ATMApplicationJFrame atmApplicationJFrame){
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
	}
	
	private void initComponent(){
		Container container = this.getContentPane();
		container.setBackground(ATMTool.JDialogBackground);
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		container.setLayout(gridbag);
		
		c.anchor = GridBagConstraints.WEST; 
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(30,10,30,10);
		JLabel oldPsdJLabel = new JLabel("输入旧密码");
		addMyJLabel(container, oldPsdJLabel, gridbag, c);
		
		MyMouseListener myMouseListener = new MyMouseListener(this);
		
		oldPsdJPasswordField = new JPasswordField();
		oldPsdJPasswordField.setBackground(Color.white);
		oldPsdJPasswordField.addMouseListener(myMouseListener);
		c.gridwidth = GridBagConstraints.REMAINDER;
		addMyJTextField(container, oldPsdJPasswordField, gridbag, c);
		
		JLabel newPsdJLabel = new JLabel("输入新密码");
		c.gridwidth = 1;
		addMyJLabel(container, newPsdJLabel, gridbag, c);
		
		newPsdJPasswordField = new JPasswordField();
		newPsdJPasswordField.setBackground(Color.white);
		newPsdJPasswordField.addMouseListener(myMouseListener);
		c.gridwidth = GridBagConstraints.REMAINDER;
		addMyJTextField(container, newPsdJPasswordField, gridbag, c);
		
		JLabel againNewPsdJLabel = new JLabel("再次输入新密码");
		c.gridwidth = 1;
		addMyJLabel(container, againNewPsdJLabel, gridbag, c);
		
		againNewPswJPasswordField = new JPasswordField();
		againNewPswJPasswordField.setBackground(Color.white);
		againNewPswJPasswordField.addMouseListener(myMouseListener);
		c.gridwidth = GridBagConstraints.REMAINDER;
		addMyJTextField(container, againNewPswJPasswordField, gridbag, c);
		
		MyActionListener myActionListener = new MyActionListener(this);
		
		ensureJButton = new JButton("修改密码");
		ensureJButton.addActionListener(myActionListener);
		c.gridwidth = 1;
		addMyJButton(container, ensureJButton, gridbag, c);
		
		returnJButton = new JButton("返回主界面");
		returnJButton.addActionListener(myActionListener);
		addMyJButton(container, returnJButton, gridbag, c);
	}
	
	private void addMyJLabel(Container container,JLabel jLabel,GridBagLayout gridbag,GridBagConstraints c){
		jLabel.setForeground(Color.blue);
		jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.ipadx = 120 - jLabel.getPreferredSize().width;
		gridbag.setConstraints( jLabel, c);
		container.add(jLabel );
	}
	
	private void addMyJTextField(Container container,JTextField jTextField,GridBagLayout gridbag,GridBagConstraints c){
		jTextField.setEditable(false);
		jTextField.setForeground(Color.black);
		jTextField.setPreferredSize(new Dimension(60, 30));
		gridbag.setConstraints( jTextField, c);
		container.add(jTextField );
	}
	
	private void addMyJButton(Container container,JButton jButton,GridBagLayout gridbag,GridBagConstraints c){
		gridbag.setConstraints( jButton, c);
		container.add(jButton );
	}
	
	private class MyMouseListener extends MouseAdapter{
		private AlterPasswordJDialog alterPasswordJDialog;
		public MyMouseListener(AlterPasswordJDialog alterPasswordJDialog){
			this.alterPasswordJDialog = alterPasswordJDialog;
		}
		@Override
		public void mousePressed(MouseEvent e) {
			Object source = e.getSource();
			if(source == oldPsdJPasswordField){
				new KeyboardJDialog(oldPsdJPasswordField,ATMTool.PasswordLength, alterPasswordJDialog );
			}
			else if(source == newPsdJPasswordField){
				new KeyboardJDialog(newPsdJPasswordField,ATMTool.PasswordLength, alterPasswordJDialog );
			}
			else if(source == againNewPswJPasswordField){
				new KeyboardJDialog(againNewPswJPasswordField,ATMTool.PasswordLength, alterPasswordJDialog );
			}
		}

	}

	private class MyActionListener implements ActionListener{
		private AlterPasswordJDialog alterPasswordJDialog;
		public MyActionListener(AlterPasswordJDialog alterPasswordJDialog){
			this.alterPasswordJDialog = alterPasswordJDialog;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if(source == returnJButton){
				int result = ATMTool.showModalConfirmDialog(alterPasswordJDialog, ATMTool.CancelAlterPasswordHint );
				if(result == ATMTool.YES){
					dispose();
					atmApplicationJFrame.setVisible(true);
				}
			}
			else if(source == ensureJButton){
				if( ! checkInformation() ){
					return ;
				}
				int result = ATMTool.showModalConfirmDialog(alterPasswordJDialog, ATMTool.AlterPasswordHint );
				if(result != ATMTool.YES){
					return ;
				}
				myGlassPane.showGlassPane();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep( ATMTool.GlassPaneShowTime );
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
						BankCard bankCard = atmApplicationJFrame.getBankCard();
						IBankCardDAO iBankCardDAO = DAOFactory.getIBankCardDAOSingleInstance();
						bankCard = iBankCardDAO.alterPassword( bankCard, newCardPassword );
						if(bankCard == null){
							myGlassPane.hideGlassPane();
							ATMTool.showModalMessageDialog(alterPasswordJDialog, ATMTool.AlterPasswordFailHint );
						}
						else{
							myGlassPane.hideGlassPane();
							atmApplicationJFrame.setBankCard(bankCard);	//保存对象,里面包含新的密码
							ATMTool.showModalMessageDialog(alterPasswordJDialog, ATMTool.AlterPasswordSuccessHint );
							dispose();
							atmApplicationJFrame.setVisible(true);
						}
					}
				}).start();
			}
		}
	}
	
	private boolean checkInformation(){
		char[] oldChars = oldPsdJPasswordField.getPassword();
		char[] newChars = newPsdJPasswordField.getPassword();
		char[] againNewChars = againNewPswJPasswordField.getPassword();
		if(oldChars == null || newChars == null || againNewChars == null ){
			ATMTool.showModalMessageDialog(this, ATMTool.AlterPasswordRequireHint );
			return false;
		}
		String oldPsd = new String(oldChars);
		String newPsd = new String(newChars);
		String againNewPsd= new String(againNewChars);
		if( ! oldPsd.matches( ATMTool.PasswordPattern ) || ! newPsd.matches(ATMTool.PasswordPattern) 
				|| ! againNewPsd.matches(ATMTool.PasswordPattern )){
			ATMTool.showModalMessageDialog(this, ATMTool.PasswordErrorHint );
			return false;
		}
		if( ! newPsd.equals(againNewPsd) ){
			ATMTool.showModalMessageDialog(this, ATMTool.AlterPasswordRequireHint );
			return false;
		}
		atmApplicationJFrame.getBankCard().setCardPassword(oldPsd);	//将输入的旧密码存入
		newCardPassword = newPsd;	//全局的新密码
		return true;
	}
}
