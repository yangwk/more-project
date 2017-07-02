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
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.github.yangwk.more.demo.javase.swing.atm.dao.DAOFactory;
import com.github.yangwk.more.demo.javase.swing.atm.dao.IBankCardDAO;

public class AddMoneyJDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private JTextField rmbJTextField;
	private JButton ensureJButton;
	private JButton cancelJButton;
	private JButton returnJButton;
	private int width = ATMTool.JDialogWidth;
	private int height = ATMTool.JDialogHeight;
	private MyGlassPane myGlassPane;
	private ATMApplicationJFrame atmApplicationJFrame;
	private double addMoney;
	public AddMoneyJDialog(ATMApplicationJFrame atmApplicationJFrame){
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
		container.setBackground( ATMTool.JDialogBackground );
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		container.setLayout(gridbag);
		
		c.anchor = GridBagConstraints.CENTER; 
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(50,10,50,10);
		c.gridwidth = GridBagConstraints.REMAINDER;
		JLabel tipJLabel = new JLabel("请将钱放入接收口");
		tipJLabel.setForeground(Color.black);
		gridbag.setConstraints( tipJLabel, c);
		container.add(tipJLabel );
		
		c.gridwidth = 1;
		JLabel yjsJLabel = new JLabel("已放入");
		yjsJLabel.setForeground(Color.blue);
		gridbag.setConstraints( yjsJLabel, c);
		container.add(yjsJLabel );
		
		MyMouseListener myMouseListener = new MyMouseListener(this);
		
		rmbJTextField = new JTextField("0.0");
		rmbJTextField.setEditable(false);
		rmbJTextField.setForeground(Color.red);
		rmbJTextField.setPreferredSize(new Dimension(180, 40));
		rmbJTextField.setFont(rmbJTextField.getFont().deriveFont(20.0F));
		rmbJTextField.setBackground(Color.white);
		rmbJTextField.addMouseListener(myMouseListener);
		gridbag.setConstraints( rmbJTextField, c);
		container.add(rmbJTextField );
		
		JLabel rmbJLabel = new JLabel("元");
		rmbJLabel.setForeground(Color.blue);
		c.ipadx = 0;
		c.ipady = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints( rmbJLabel, c);
		container.add(rmbJLabel );
		
		c.gridwidth = 1;
		
		MyActionListener myActionListener = new MyActionListener(this);
		
		ensureJButton = new JButton("确认存款");
		ensureJButton.addActionListener(myActionListener);
		gridbag.setConstraints( ensureJButton, c);
		container.add(ensureJButton );
		
		cancelJButton = new JButton("取消存款");
		cancelJButton.addActionListener(myActionListener);
		gridbag.setConstraints( cancelJButton, c);
		container.add(cancelJButton );
		
		returnJButton = new JButton("返回主界面");
		returnJButton.addActionListener(myActionListener);
		gridbag.setConstraints( returnJButton, c);
		container.add(returnJButton );
	}
	
	private class MyMouseListener extends MouseAdapter{
		private AddMoneyJDialog addMoneyJDialog;
		public MyMouseListener(AddMoneyJDialog addMoneyJDialog){
			this.addMoneyJDialog = addMoneyJDialog;
		}
		@Override
		public void mousePressed(MouseEvent e) {
			new KeyboardJDialog(rmbJTextField,ATMTool.MoneyLength, addMoneyJDialog );
		}

	}

	private class MyActionListener implements ActionListener{
		private AddMoneyJDialog addMoneyJDialog;
		public MyActionListener(AddMoneyJDialog addMoneyJDialog){
			this.addMoneyJDialog = addMoneyJDialog;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if(source == returnJButton){
				ATMTool.showModalMessageDialog(addMoneyJDialog, ATMTool.CancelAddMoneyOKHint );
				dispose();
				atmApplicationJFrame.setVisible(true);
			}
			else if(source == cancelJButton ){
				int result = ATMTool.showModalConfirmDialog(addMoneyJDialog, ATMTool.CancelAddMoneyHint );
				if(result == ATMTool.YES){
					ATMTool.showModalMessageDialog(addMoneyJDialog, ATMTool.CancelAddMoneyOKHint );
					dispose();
					atmApplicationJFrame.setVisible(true);
				}
			}
			else if(source == ensureJButton){
				if( ! checkInformation() )
					return ;
				int result = ATMTool.showModalConfirmDialog(addMoneyJDialog, "您放入的金额为\n"+ATMTool.RMBFormat(addMoney)+"\n确认存款吗?");
				if(result != ATMTool.YES )
					return ;
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
						boolean success = iBankCardDAO.addMoney( addMoney, atmApplicationJFrame.getBankCard().getCardId() );
						if(success){
							myGlassPane.hideGlassPane();
							ATMTool.showModalMessageDialog(addMoneyJDialog, ATMTool.AddMoneySuccessHint );
							dispose();
							atmApplicationJFrame.setVisible(true);
						}
						else{
							myGlassPane.hideGlassPane();
							ATMTool.showModalMessageDialog(addMoneyJDialog, ATMTool.AddMoneyFailHint );
							dispose();
							atmApplicationJFrame.setVisible(true);
						}
					}
				}).start();
			}
		}
		
	}
	
	private boolean checkInformation(){
		String text = rmbJTextField.getText();
		if( ! text.matches( ATMTool.MoneyPattern ) ){
			ATMTool.showModalMessageDialog(this, ATMTool.GetAddMoneyErrorHint );
			return false;
		}
		addMoney = Double.parseDouble( rmbJTextField.getText() );
		if(addMoney <= 0.0D || addMoney % ATMTool.RatedGetAddMoney != 0 ){
			ATMTool.showModalMessageDialog(this, ATMTool.GetAddMoneyErrorHint );
			return false;
		}
		return true;
	}
	
}
