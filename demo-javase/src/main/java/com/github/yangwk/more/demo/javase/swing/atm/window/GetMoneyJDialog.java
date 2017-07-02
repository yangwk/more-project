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
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.github.yangwk.more.demo.javase.swing.atm.dao.DAOFactory;
import com.github.yangwk.more.demo.javase.swing.atm.dao.IBankCardDAO;

public class GetMoneyJDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private JTextField rmbJTextField;
	private JButton ensureJButton;
	private JButton returnJButton;
	private int width = ATMTool.JDialogWidth;
	private int height = ATMTool.JDialogHeight;
	private ATMApplicationJFrame atmApplicationJFrame;
	private MyGlassPane myGlassPane;
	private double getMoney = 0.0D;
	public GetMoneyJDialog(ATMApplicationJFrame atmApplicationJFrame){
		super();
		super.setVisible(false);
		myGlassPane = new MyGlassPane(this);
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
		
		c.anchor = GridBagConstraints.WEST ; 
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(50,10,50,10);
		JLabel inMoneyJLabel = new JLabel("输入金额");
		inMoneyJLabel.setForeground(Color.blue);
		inMoneyJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		c.ipadx = 60 - inMoneyJLabel.getPreferredSize().width;
		gridbag.setConstraints( inMoneyJLabel, c);
		container.add(inMoneyJLabel );
		
		MyMouseListener myMouseListener = new MyMouseListener(this);
		
		rmbJTextField = new JTextField("0.0");
		rmbJTextField.setEditable(false);
		rmbJTextField.setForeground(Color.red);
		rmbJTextField.setPreferredSize(new Dimension(180, 30));
		rmbJTextField.addMouseListener(myMouseListener);
		rmbJTextField.setBackground(Color.white);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.ipadx = 0;
		gridbag.setConstraints( rmbJTextField, c);
		container.add(rmbJTextField );
		
		MyActionListener myActionListener = new MyActionListener(this);
		
		ensureJButton = new JButton("确认取款");
		ensureJButton.addActionListener(myActionListener);
		c.gridwidth = 1;
		gridbag.setConstraints( ensureJButton, c);
		container.add(ensureJButton );
		
		returnJButton = new JButton("返回主界面");
		returnJButton.addActionListener(myActionListener);
		gridbag.setConstraints( returnJButton, c);
		container.add(returnJButton );
	}
	
	private class MyMouseListener extends MouseAdapter{
		private GetMoneyJDialog getMoneyJDialog;
		public MyMouseListener(GetMoneyJDialog getMoneyJDialog){
			this.getMoneyJDialog = getMoneyJDialog;
		}
		@Override
		public void mousePressed(MouseEvent e) {
			new KeyboardJDialog(rmbJTextField,ATMTool.MoneyLength, getMoneyJDialog );
		}

	}

	private class MyActionListener implements ActionListener{
		private GetMoneyJDialog getMoneyJDialog;
		public MyActionListener(GetMoneyJDialog getMoneyJDialog){
			this.getMoneyJDialog = getMoneyJDialog;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if(source == returnJButton){
				dispose();
				atmApplicationJFrame.setVisible(true);
			}
			else if(source == ensureJButton){
				if( checkInformation() ){
					int result = ATMTool.showModalConfirmDialog(getMoneyJDialog, "您输入的金额为\n"+ATMTool.RMBFormat(getMoney)+"\n确认取款吗?");
					if(result == ATMTool.YES ){
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
								boolean success = iBankCardDAO.getMoney( getMoney, atmApplicationJFrame.getBankCard().getCardId() );
								if(success){
									myGlassPane.hideGlassPane();
									ATMTool.showModalMessageDialog(getMoneyJDialog, ATMTool.GetMoneySuccessHint );
									dispose();
									atmApplicationJFrame.setVisible(true);
								}
								else{
									myGlassPane.hideGlassPane();
									ATMTool.showModalMessageDialog(getMoneyJDialog, ATMTool.GetMoneyFailHint );
								}
							}
						}).start();
					}
				}
			}
		}
		
	}
	
	private boolean checkInformation(){
		String text = rmbJTextField.getText();
		if( ! text.matches( ATMTool.MoneyPattern ) ){
			ATMTool.showModalMessageDialog(this, ATMTool.GetAddMoneyErrorHint );
			return false;
		}
		getMoney = Double.parseDouble( rmbJTextField.getText() );
		if(getMoney <= 0.0D || getMoney % ATMTool.RatedGetAddMoney != 0){
			ATMTool.showModalMessageDialog(this, ATMTool.GetAddMoneyErrorHint );
			return false;
		}
		return true;
	}
}
