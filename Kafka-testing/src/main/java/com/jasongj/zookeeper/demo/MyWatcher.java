package com.jasongj.zookeeper.demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class MyWatcher implements Watcher {

	MyApplication create = null;
	String path = null;

	public MyWatcher(MyApplication create, String path) {
		super();
		this.create = create;
		this.path = path;
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println(event.getPath() + " | " + event.getType().name());
		try {
			String leaderNode = create.getLeaderNode("/chroot");
			System.out.println("Now, leader is " + leaderNode);
			//通知下一个leader节点开始工作
			System.out.println("Inform node " + leaderNode + " to start...");
			create.doSth();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
