package com.github.yangwk.more.demo.javase.swing.animation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * 动画型的窗口
 * <p>支持拖动到窗口边缘隐藏、抖动、抖动时失去焦点闪烁
 * @author yangwk
 *
 */
public class AnimationJFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	// 方向
	private enum Direction {
		leftdown, leftup, rightup, rightdown, left, right, up
	}

	private JButton joggleJButton;
	private AnimationJFrame thisJFrame = this;

	// 抖动方向
	private Direction joggleDirection = Direction.leftdown;
	// 是否已经开始抖动
	private volatile boolean startJoggle = false;

	private Point mouseInitPoint = new Point(0, 0);

	private Point jFrameInitPoint = new Point(0, 0);
	// 滑动的方向
	private Direction slideDirection = Direction.left;
	
	private boolean beginSlideInverse = false;
	
	private FlashWindowListener flashWindowListener = new FlashWindowListener();
	private MyMouseListener myMouseListener = new MyMouseListener();
	private SlideListener slideListener = new SlideListener();
	private SlideInverseListener slideInverseListener = new SlideInverseListener();

	public AnimationJFrame() {
		super();
		initComponent();
	}

	private void initComponent() {
		int width = 500;
		int height = 500;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setResizable(false);
		this.setTitle("可以抖动的窗口");
		Dimension screenSize = getScreenSize();
		int x = (screenSize.width - width) / 2;
		int y = (screenSize.height - height) / 2;
		this.setLocation(x, y);
		addComponent();
		// window事件
		this.addWindowListener(flashWindowListener);
		// mouse事件
		this.addMouseListener(myMouseListener);
		// mouse移动事件
		this.addMouseMotionListener(slideListener);
		//
		this.setVisible(true);
	}

	private void addComponent() {
		joggleJButton = new JButton("开始抖动");
		joggleJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (startJoggle) {
					return;
				}
				startJoggle = true; // 标记开始抖动
				joggleJButton.setEnabled(false);
				new Thread(new JoggleRunnable()).start();
			}
		});

		this.add(joggleJButton, BorderLayout.NORTH);
	}

	//获取屏幕大小
	private Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	//线程睡眠
	private void sleeping(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	//取消滑动,主要作用是防止MouseDragged反复执行
	private void cancelSlide(){
		thisJFrame.removeMouseMotionListener(slideListener);
	}
	
	//恢复滑动,cancelSlide()和recoverSlide()切换,为了确保每次只执行一个MouseDragged事件
	private void recoverSlide(){
		thisJFrame.addMouseMotionListener(slideListener);
	}
	
	//取消逆向滑动
	private void cancelSlideInverse(){
		thisJFrame.removeMouseMotionListener(slideInverseListener);
	}
	
	//恢复逆向滑动
	private void recoverSlideInverse(){
		thisJFrame.addMouseMotionListener(slideInverseListener);
	}

	private class FlashWindowListener extends WindowAdapter {
		@Override
		public void windowDeactivated(WindowEvent e) {
			if (startJoggle) {
				new Thread(new FlashRunnable()).start();
			}
			//只有逆向滑动执行过,才能对窗口失去焦点判断,否则有Bug,即:
			//产生mouseDragged事件后,滑动隐藏,此时,立即让窗口失去焦点,就会出现Bug
			//Bug原因是:滑动隐藏后窗口坐标为负数,窗口失去焦点执行滑动隐藏,会从坐标0开始,将出现回滚的Bug效果
			//该判断,控制只有执行了逆向滑动才能奏效
			if(beginSlideInverse){
				Point point = thisJFrame.getLocation();
				if (point.y <= 0) {
					slideDirection = Direction.up;
					new Thread(new SlideRunnable()).start();
				} else if (point.x <= 0) {
					slideDirection = Direction.left;
					new Thread(new SlideRunnable()).start();
				} else if (point.x + thisJFrame.getSize().width >= getScreenSize().width) {
					slideDirection = Direction.right;
					new Thread(new SlideRunnable()).start();
				} 
			}
		}
	}

	private class MyMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
		}
		@Override
		public void mousePressed(MouseEvent e) {
			mouseInitPoint = e.getLocationOnScreen();
			jFrameInitPoint = thisJFrame.getLocationOnScreen();
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}

	private class SlideListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			Point mouseNowPoint = e.getLocationOnScreen();
			//鼠标移动产生的差值
			int xGap = mouseNowPoint.x - mouseInitPoint.x;
			int yGap = mouseNowPoint.y - mouseInitPoint.y;
			//jframe原坐标加上差值
			int newX = jFrameInitPoint.x + xGap;
			int newY = jFrameInitPoint.y + yGap;

			if (newY <= 0) {
				cancelSlide();	//取消滑动
				slideDirection = Direction.up;
				new Thread(new SlideRunnable()).start();
			} else if (newX <= 0) {
				cancelSlide();	//取消滑动
				slideDirection = Direction.left;
				new Thread(new SlideRunnable()).start();
			} else if (newX + thisJFrame.getSize().width >= getScreenSize().width) {
				cancelSlide();	//取消滑动
				slideDirection = Direction.right;
				new Thread(new SlideRunnable()).start();
			} else {
				thisJFrame.setLocation(newX, newY);
			}
		}
		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}
	
	private class SlideInverseListener implements MouseMotionListener{
		@Override
		public void mouseDragged(MouseEvent e) {
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			//
			
			cancelSlideInverse();	//取消逆向滑动
			new Thread(new SlideInverseRunnable()).start();
		}
	}

	// 抖动的线程
	private class JoggleRunnable implements Runnable {

		@Override
		public void run() {
			int joggleCycle = 10; // 抖动周期,一个周期是一个顺序
			int joggleGap = 5; // 抖动的幅度,像素
			int joggleFPS = 30; // 帧率,1秒抖动n次,非抖动n周期
			Point point = thisJFrame.getLocationOnScreen();
			// 抖动的方向顺序:左下,左上,右上,右下
			for (int i = 0; i < joggleCycle; i++) {
				for (int k = 0; k < Direction.values().length; k++) {
					if (joggleDirection == Direction.leftdown) {
						thisJFrame.setLocation(point.x - joggleGap, point.y
								+ joggleGap);
						joggleDirection = Direction.leftup;
					} else if (joggleDirection == Direction.leftup) {
						thisJFrame.setLocation(point.x - joggleGap, point.y
								- joggleGap);
						joggleDirection = Direction.rightup;
					} else if (joggleDirection == Direction.rightup) {
						thisJFrame.setLocation(point.x + joggleGap, point.y
								- joggleGap);
						joggleDirection = Direction.rightdown;
					} else if (joggleDirection == Direction.rightdown) {
						thisJFrame.setLocation(point.x + joggleGap, point.y
								+ joggleGap);
						joggleDirection = Direction.leftdown;
					}
					sleeping(1000 / joggleFPS);
				}
			}
			thisJFrame.setLocation(point.x, point.y); // 恢复原来位置
			startJoggle = false; // 标记停止抖动
			joggleJButton.setEnabled(true);
		}

	}

	// 任务栏闪烁的线程
	private class FlashRunnable implements Runnable {

		@Override
		public void run() {
			thisJFrame.setVisible(true);	//thisJFrame.toFront();
			sleeping(10);
		}

	}

	// 滑动隐藏的线程
	private class SlideRunnable implements Runnable {

		@Override
		public void run() {
			beginSlideInverse = false;
			
			Point point = thisJFrame.getLocationOnScreen();
			int time = 1000 / 30;
			Insets insets = thisJFrame.getInsets();
			//先取左,右边框的最大值
			int maxValue = Math.max(insets.left, insets.right);
			//最后,可获得左,右,下边框的最大值
			maxValue = Math.max(maxValue, insets.bottom);
			int showPix = maxValue+1; // 要在屏幕显示的像素
			int movePix = 100; // 移动的像素
			int startX = 0, startY = 0, distance = 0;
			if (slideDirection == Direction.left) {
				startX = 0;
				startY = point.y;
				distance = thisJFrame.getWidth() - showPix;
				for (; startX <= distance; startX += movePix) {
					thisJFrame.setLocation(-startX, startY);
					sleeping(time);
				}
				thisJFrame.setLocation(-distance, startY);
			} else if (slideDirection == Direction.right) {
				startX = getScreenSize().width - thisJFrame.getSize().width;
				startY = point.y;
				distance = thisJFrame.getWidth() - showPix;
				for (int x = startX; x <= startX + distance; x += movePix) {
					thisJFrame.setLocation(x, startY);
					sleeping(time);
				}
				thisJFrame.setLocation(startX + distance, startY);
			} else if (slideDirection == Direction.up) {
				startX = point.x;
				startY = 0;
				distance = thisJFrame.getHeight() - showPix;
				for (; startY <= distance; startY += movePix) {
					thisJFrame.setLocation(startX, -startY);
					sleeping(time);
				}
				thisJFrame.setLocation(startX, -distance);
			}
			
			//逆向滑动
			recoverSlideInverse();
		}
	}
	
	private class SlideInverseRunnable implements Runnable{
		@Override
		public void run() {
			beginSlideInverse = true;
			
			Point point = thisJFrame.getLocationOnScreen();
			int time = 1000 / 30;
			int movePix = 100; // 移动的像素
			int startX = 0, startY = 0;
			if (slideDirection == Direction.left) {
				startX = point.x;	//负数
				startY = point.y;
				for (; startX <= 0; startX += movePix) {
					thisJFrame.setLocation(startX, startY);
					sleeping(time);
				}
				thisJFrame.setLocation(0, startY);
			} 
			else if (slideDirection == Direction.right) {
				startX = point.x;	//正数
				startY = point.y;
				int endX = getScreenSize().width - thisJFrame.getWidth();
				for (int x = startX; x >= endX; x -= movePix) {
					thisJFrame.setLocation(x, startY);
					sleeping(time);
				}
				thisJFrame.setLocation(endX, startY);
			} 
			else if (slideDirection == Direction.up) {
				startX = point.x;
				startY = point.y;	//负数
				for (; startY <= 0; startY += movePix) {
					thisJFrame.setLocation(startX, startY);
					sleeping(time);
				}
				thisJFrame.setLocation(startX, 0);
			}
			
			//滑动
			recoverSlide();
		}
	}


}
