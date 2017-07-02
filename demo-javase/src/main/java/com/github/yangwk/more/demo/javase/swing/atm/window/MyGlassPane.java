package com.github.yangwk.more.demo.javase.swing.atm.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyGlassPane extends JPanel implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private JDialog jDialog = null;
	private JFrame jFrame = null;
	private JLabel statusLabel;
	private int timeoutSecond = 5;	//超时,默认5秒
	private boolean isTimeOut = false;	//标记是否超时
	private boolean isShowOpaqueStatusText = false;
	
	private Object lockObject = new Object();	//控制同步的对象
	
	public MyGlassPane( JFrame jFrame ) {
		this.jFrame = jFrame;
		initComponent();
		this.jFrame.setGlassPane(this);
	}
	
	public MyGlassPane( JDialog jDialog ) {
		this.jDialog = jDialog;
		initComponent();
		this.jDialog.setGlassPane(this);
	}
	
	private void initComponent(){
		statusLabel = new JLabel("正在操作,请稍等...");
		statusLabel.setFont( statusLabel.getFont().deriveFont(Font.BOLD,24.0F));
		statusLabel.setForeground(new Color(87,3,171));
		try {
			//动态gif的路径,若默认user.dir被修改,可能提示找不到资源
			String path = System.getProperty("user.dir").replace('\\', '/').concat("/myresource/");
			statusLabel.setIcon(new ImageIcon(path+"actioning.gif" ) );
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		statusLabel.setHorizontalAlignment(JLabel.CENTER);
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
		this.setLayout(new BorderLayout());
		this.add(statusLabel);
	}

	public void showGlassPane() {
		synchronized(lockObject){	//同步
			new Thread(this).start();
		}
	}
	
	public void hideGlassPane(){
		synchronized(lockObject){	//同步
			this.setVisible(false);
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			isShowOpaqueStatusText = false;
			lockObject.notify();	//唤醒
		}
	}
	
	public void setSecond(int second){
		this.timeoutSecond = second;
	}
	
	public boolean isTimeOut(){
		return this.isTimeOut;
	}
	
	public void showOpaqueStatusText(String message, ImageIcon icon){
		synchronized (lockObject) {
			statusLabel.setText(message);
			statusLabel.setIcon(icon);
			this.setOpaque(true);	//不透明
			this.setVisible(true);
			isShowOpaqueStatusText = true;
		}
	}
	
	//拥有对象锁
	@Override
	public void run() {
		synchronized(lockObject){	//这里也要加同步,因为这是new一个新线程;不加同步,该新线程不可能拥有对象锁
			this.setOpaque(false);	//透明
			this.setVisible(true);
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			try {
				lockObject.wait(timeoutSecond*1000);	//释放锁,等待一定时间后再执行
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			//仍然显示,但不showOpaqueStatusText(String, ImageIcon)
			if( this.isShowing() && ! isShowOpaqueStatusText ){	
				isTimeOut = true;	//超时
				this.hideGlassPane();
				if(jDialog != null && jDialog.isShowing() ){
					ATMTool.showModalMessageDialog(jDialog, "操作超时");
				}
				else if(jFrame != null && jFrame.isShowing() ){
					ATMTool.showModalMessageDialog(jFrame, "操作超时");
				}
			}
		}
	}
}
