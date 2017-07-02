package com.github.yangwk.more.demo.javase.swing.atm.window;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

public class KeyboardJDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	private JLabel[] numberJLables;
	private int numberSize = 10;
	private JLabel pointJLabel;	//小数点
	private JLabel ensureJLabel;
	private JLabel deleteJLabel;
	private JLabel clearJLabel;
	private JLabel cancelJLabel;
	private int width = 215;
	private int height = 130;
	private int numberWidth = 30;
	private int numberHeight = 20;
	private NumberActionListener numberActionListener = null;
	private JTextField jTextField ;	//负责的文本框
	private int JTextFieldMaxLength =0;	//文本框允许的字符最大长度
	
	public KeyboardJDialog(JTextField jTextField,int JTextFieldMaxLength ,Window owner){
		super(owner);
		super.setVisible(false);
		this.jTextField = jTextField;
		this.JTextFieldMaxLength = JTextFieldMaxLength;
		numberActionListener = new NumberActionListener();
		initComponent();
		this.setUndecorated(true);
		this.setResizable(false);
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		ATMTool.setAllJComponentsFontAndCursor(this);
		
		super.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);
		
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets( getGraphicsConfiguration() );
		Point point = jTextField.getLocationOnScreen();
		int jtfWidth = jTextField.getWidth();
		int x = point.x + jtfWidth + 5;	//初始在文本框右边5像素
		if( x + width > screenDimension.width ){	//超出了屏幕右边范围
			x = point.x - 5 - width;	//在文本框左边5像素
		}
		int y = point.y ;	//与文本框平行
		if( y + height > screenDimension.height - screenInsets.bottom ){	//超出了屏幕下方(不包括任务栏)范围
			y = screenDimension.height- screenInsets.bottom - height;	//刚好接触
		}
		this.setBounds(x, y, width, height);
		super.setVisible(true);
		
	}

	private void initComponent(){
		Container container = this.getContentPane();
		( (JPanel)container ).setBorder(new LineBorder(Color.orange));
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		container.setLayout(gridbag);
		
		c.anchor = GridBagConstraints.CENTER; 
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5,5,5,5);
		
		JLabel titleJLabel = new JLabel("ATM键盘");
		titleJLabel.setForeground(Color.blue);
		c.gridwidth = GridBagConstraints.REMAINDER;
		addMyJLabelInContainer(titleJLabel,container,gridbag,c);

		numberJLables = new JLabel[numberSize];
		LineBorder lineBorder = new LineBorder(Color.blue);
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		Color backGround = Color.orange;
		int[] number = getRandomNumber();
		for(int i=0;i<numberSize; i++){
			numberJLables[i] = new JLabel( String.valueOf(number[i]) );
			setMyJLabelStyle(numberJLables[i], lineBorder, cursor,backGround);
			if(i<4){
				c.gridwidth = 1;
			}
			else if(i==4){
				c.gridwidth = GridBagConstraints.REMAINDER;
			}
			else if(i>4 && i<numberSize-1){
				c.gridwidth = 1;	//恢复默认
			}
			else{
				c.gridwidth = GridBagConstraints.REMAINDER;	//GridBagConstraints.RELATIVE最多只能有2个
			}
			addMyJLabelInContainer(numberJLables[i],container,gridbag,c);
		}
		pointJLabel = new JLabel(".");
		setMyJLabelStyle(pointJLabel, lineBorder, cursor,backGround);
		c.gridwidth = 1;
		addMyJLabelInContainer(pointJLabel,container,gridbag,c);
		
		backGround = Color.green;
		ensureJLabel = new JLabel("确定");
		setMyJLabelStyle(ensureJLabel, lineBorder, cursor,backGround);
		addMyJLabelInContainer(ensureJLabel,container,gridbag,c);
		
		deleteJLabel = new JLabel("删除");
		setMyJLabelStyle(deleteJLabel, lineBorder, cursor,backGround);
		addMyJLabelInContainer(deleteJLabel,container,gridbag,c);
		
		clearJLabel = new JLabel("清空");
		setMyJLabelStyle(clearJLabel, lineBorder, cursor,backGround);
		addMyJLabelInContainer(clearJLabel,container,gridbag,c);
		
		cancelJLabel = new JLabel("取消");
		setMyJLabelStyle(cancelJLabel, lineBorder, cursor,backGround);
		addMyJLabelInContainer(cancelJLabel,container,gridbag,c);
	}
	
	private void setMyJLabelStyle(JLabel jLabel,LineBorder lineBorder,Cursor cursor,Color backGround){
		jLabel.setBorder( lineBorder );
		jLabel.setForeground(Color.black);
		jLabel.setOpaque(true);
		jLabel.setBackground( backGround );
		jLabel.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel.setCursor( cursor );
		
		jLabel.addMouseListener( numberActionListener );
	}
	
	private void addMyJLabelInContainer(JLabel jLabel,Container container,GridBagLayout gridbag,GridBagConstraints c){
		Dimension dimension = jLabel.getPreferredSize();
		int xGap = numberWidth - dimension.width;
		c.ipadx = (xGap > 0) ? xGap : -xGap;
		int yGap = numberHeight - dimension.height;
		c.ipady = (yGap > 0) ? yGap : -yGap;
		gridbag.setConstraints( jLabel, c);
		container.add(jLabel );
	}
	
	private int[] getRandomNumber(){
		int size = numberSize/2;	//10个数字的一半
		int[] randoms = new int[size];
		Random random = new Random();
		for(int i=0;i<size;i++){
			randoms[i] = random.nextInt(10);	//产生0到9的随机数5个
		}
		int[] number = new int[numberSize];	//10个数字
		for(int i=0;i<number.length;i++){
			number[i] = i;	//从0到9按序
		}
		//5个随机数作为下标,与前5个数字进行交换,达到打乱顺序的效果
		for(int i=0;i<size;i++){
			int temp = number[i];
			number[i]= number[ randoms[i] ];
			number[ randoms[i] ] = temp;
		}
		return number;
	}
	
	private class NumberActionListener extends MouseAdapter{

		@Override
		public void mousePressed(MouseEvent e) {
			Object source = e.getSource();
			if( !( source instanceof JLabel ) ){
				return ;
			}
			if(source == ensureJLabel){
				dispose();
			}
			else if( source == deleteJLabel ){
				String text = jTextField.getText();
				if( text == null){
					return ;
				}
				int length = text.length();
				if(length == 0){
					return ;
				}
				jTextField.setText( text.substring(0, length-1) );
			}
			else if( source == clearJLabel){
				jTextField.setText( "" );
			}
			else if( source == cancelJLabel ){
				jTextField.setText( "" );
				dispose();
			}
			else{	//是数字或.
				String text = jTextField.getText();
				if(text.length() >= JTextFieldMaxLength){
					return ;
				}
				JLabel jLabel = (JLabel)source;
				text = text.concat( jLabel.getText() );
				jTextField.setText(text);
			}
			
		}
		
	}
}
