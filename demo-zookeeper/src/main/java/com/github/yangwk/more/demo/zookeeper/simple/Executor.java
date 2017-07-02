package com.github.yangwk.more.demo.zookeeper.simple;

/**
 * A simple example program to use DataMonitor to start and
 * stop executables based on a znode. The program watches the
 * specified znode and saves the data that corresponds to the
 * znode in the filesystem. It also starts the specified program
 * with the specified arguments when the znode exists and kills
 * the program if the znode goes away.
 */
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Executor
    implements Watcher, Runnable, DataMonitor.DataMonitorListener
{
    String znode;

    DataMonitor dm;

    ZooKeeper zk;

    String filename;

    public Executor(String hostPort, String znode, String filename) throws KeeperException, IOException {
        this.filename = filename;
        zk = new ZooKeeper(hostPort, 3000, this);
        dm = new DataMonitor(zk, znode, null, this);
    }

    /**
     * 127.0.0.1:2181 /test F:/yangwk/temp/zookeeper/executor.txt
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("args: hostPort znode filename");
            System.exit(2);
        }
        String hostPort = args[0];
        String znode = args[1];
        String filename = args[2];
        try {
            new Executor(hostPort, znode, filename).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***************************************************************************
     * We do process any events ourselves, we just need to forward them on.
     *
     * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.proto.WatcherEvent)
     */
    @Override
    public void process(WatchedEvent event) {
    	dm.process(event);
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!dm.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void exists(byte[] data) {
        if (data == null) {
        	System.out.println("not exists");
        } else {
        	try {
                FileOutputStream fos = new FileOutputStream(filename, true);
                fos.write(data);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
