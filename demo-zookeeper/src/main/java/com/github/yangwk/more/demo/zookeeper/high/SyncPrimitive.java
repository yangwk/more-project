package com.github.yangwk.more.demo.zookeeper.high;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.github.yangwk.more.demo.zookeeper.simple.PrintUtils;

public class SyncPrimitive implements Watcher {

	static ZooKeeper zk = null;
	static Integer mutex;

	String root;

	SyncPrimitive(String address) {
		if (zk == null) {
			try {
				System.out.println("Starting ZK:");
				zk = new ZooKeeper(address, 3000, this);
				mutex = new Integer(-1);
				System.out.println("Finished starting ZK: " + zk);
			} catch (IOException e) {
				zk = null;
				e.printStackTrace();
			}
		}
	}

	@Override
	synchronized public void process(WatchedEvent event) {
		synchronized (mutex) {
			PrintUtils.print(event);	//
			mutex.notify();
		}
	}

	/**
	 * Barrier
	 */
	static public class Barrier extends SyncPrimitive {
		int size;
		String znodename;

		/**
		 * Barrier constructor
		 *
		 * @param address
		 * @param root
		 * @param size
		 * @param znodename
		 */
		Barrier(String address, String root, int size, String znodename) {
			super(address);
			this.root = root;
			this.size = size;
			this.znodename = znodename;

			// Create barrier node
			if (zk != null) {
				try {
					Stat s = zk.exists(root, false);
					if (s == null) {
						this.root = zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					}
				} catch (KeeperException e) {
					System.out.println("Keeper exception when instantiating queue: " + e.toString());
				} catch (InterruptedException e) {
					System.out.println("Interrupted exception");
				}
			}

		}

		/**
		 * Join barrier
		 *
		 * @return
		 * @throws KeeperException
		 * @throws InterruptedException
		 */

		boolean enter() throws KeeperException, InterruptedException {
			String createdNode = zk.create(root + "/" + znodename, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			this.znodename = createdNode.split("/")[2];
			while (true) {
				synchronized (mutex) {
					List<String> list = zk.getChildren(root, true);
					{
						for(String s : list){
							System.out.println(s);
						}
					}
					if (list.size() < size) {
						mutex.wait();
					} else {
						return true;
					}
				}
			}
		}

		/**
		 * Wait until all reach barrier
		 *
		 * @return
		 * @throws KeeperException
		 * @throws InterruptedException
		 */

		boolean leave() throws KeeperException, InterruptedException {
			zk.delete(root + "/" + znodename, 0);
			while (true) {
				synchronized (mutex) {
					List<String> list = zk.getChildren(root, true);
					if (list.size() > 0) {
						mutex.wait();
					} else {
						return true;
					}
				}
			}
		}
	}

	/**
	 * Producer-Consumer queue
	 */
	static public class Queue extends SyncPrimitive {
		String znodename;

		/**
		 * Constructor of producer-consumer queue
		 *
		 * @param address
		 * @param znodename
		 */
		Queue(String address, String root, String znodename) {
			super(address);
			this.root = root;
			this.znodename = znodename;
			
			// Create ZK node name
			if (zk != null) {
				try {
					Stat s = zk.exists(root, false);
					if (s == null) {
						zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					}
				} catch (KeeperException e) {
					System.out.println("Keeper exception when instantiating queue: " + e.toString());
				} catch (InterruptedException e) {
					System.out.println("Interrupted exception");
				}
			}
		}

		/**
		 * Add element to the queue.
		 *
		 * @param i
		 * @return
		 */

		boolean produce(int i) throws KeeperException, InterruptedException {
			ByteBuffer b = ByteBuffer.allocate(4);
			byte[] value;

			// Add child with value i
			b.putInt(i);
			value = b.array();
			zk.create(root + "/" + znodename, value, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);

			return true;
		}

		/**
		 * Remove first element from the queue.
		 *
		 * @return
		 * @throws KeeperException
		 * @throws InterruptedException
		 */
		int consume() throws KeeperException, InterruptedException {
			int retvalue = -1;
			Stat stat = null;

			while (true) {
				synchronized (mutex) {
					List<String> list = zk.getChildren(root, true);
					if (list.size() == 0) {
						System.out.println("Going to wait");
						mutex.wait();
					} else {
						// get first child
						String minChild = list.get(0);
						Integer min = new Integer(minChild.substring(znodename.length()));
						for (String s : list) {
							Integer tempValue = new Integer(s.substring(znodename.length()));
							if (tempValue < min){
								min = tempValue;
								minChild = s;
							}
						}
						
						System.out.println("znode: " + root + "/" + minChild);
						
						byte[] b = zk.getData(root + "/" + minChild, false, stat);
						zk.delete(root + "/" + minChild, 0);
						
						ByteBuffer buffer = ByteBuffer.wrap(b);
						retvalue = buffer.getInt();

						return retvalue;
					}
				}
			}
		}
	}

	/**
	 * for barrierTest: 127.0.0.1:2181 /test 1
	 * for queueTest produce: 127.0.0.1:2181 /test 2 p
	 * for queueTest consume: 127.0.0.1:2181 /test 2 c
	 * @param args
	 */
	public static void main(String args[]) {
		String address = null, root = null;
		int size = 0;
		if(args == null){
			System.err.println("args is null");
            System.exit(2);
		}
		
		address = args[0];
		root = args[1];
		size = Integer.valueOf(args[2]);
		
		if(args.length == 3){
			barrierTest(address, root, size);
		}else if(args.length == 4){
			String oper = args[3];
			
			queueTest(address, root, size, oper);
		}

	}

	public static void queueTest(String address, String root, int size, String oper) {
		String znodename = "queueTest";
		Queue q = new Queue(address, root, znodename);

		if (oper.equals("p")) {
			System.out.println("Producer");
			for (int i = 0; i < size; i++){
				try {
					q.produce(10 + i);
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {

				}
			}
		} else {
			System.out.println("Consumer");

			for (int i = 0; i < size; i++) {
				try {
					int r = q.consume();
					System.out.println("Item: " + r);
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {

				}
			}
		}
	}

	public static void barrierTest(String address, String root, int size) {
		String znodename = "barrierTest";
		Barrier b = new Barrier(address, root, size, znodename);
		try {
			boolean flag = b.enter();
			System.out.println("Entered barrier: " + size);
			if (!flag)
				System.out.println("Error when entering the barrier");
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

		}
		
		try {
			b.leave();
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		System.out.println("Left barrier");
	}
}