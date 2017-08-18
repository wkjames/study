package com.jasongj.zookeeper.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class MyApplication {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws KeeperException
	 */

	private static ZooKeeper zk = null;

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

		MyApplication create = new MyApplication();
		zk = new ZooKeeper("zookeeper0:2181", 6000, null);
		//如果没有根节点，就create下
		// zk.create("/chroot", null, Ids.OPEN_ACL_UNSAFE,
		// CreateMode.PERSISTENT);

		// 创建第子节点
		final String createdPath = zk.create("/chroot/leader", null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		// String node2Watch = create.getNode2Watch("/chroot");
		Stat stat = null;
		String node2Watch = null;
		// String leader = create.getLeaderNode("/chroot");
		// if(leader == null | createdPath.equals(leader))
		//监听自己创建的节点，如果该节点被删除，则该进程退出
		stat = zk.exists(createdPath, new Watcher(){
			@Override
			public void process(WatchedEvent event) {
				System.out.println(createdPath + " node exits..");
				System.exit(1);
			}
		});
		//判断该节点是否为leader
		if (!create.isLeader(createdPath.substring(8), "/chroot")) {
			// 如果不是leader，就监听比自己小的最大节点
			node2Watch = create.getNode2Watch("/chroot");
			//监听比自己小的最大节点，防止惊群效应
			stat = zk.exists("/chroot/" + node2Watch, new MyWatcher(create, "/chroot/" + node2Watch));
			System.out.println(createdPath + " node is created, and watches node " + node2Watch);
		}else{
			System.out.println(createdPath + " node is created, you are the leader now...");
		}
		Thread.sleep(1000000);
		zk.close();
	}

	/*
	 * 获取当前path的子节点
	 */
	public List<String> getChildren(String path) throws KeeperException, InterruptedException {
		List<String> children = zk.getChildren(path, false);
		return children;
	}

	/*
	 * 得到需要监听哪个Node
	 */
	public String getNode2Watch(String path) throws KeeperException, InterruptedException {
		List<String> children = zk.getChildren(path, false);
		if (children.isEmpty())
			return null;
		Collections.sort(children);
		String leaderNode = null;
		if (children.size() == 1)
			leaderNode = children.get(0);
		else {
			leaderNode = children.get(children.size() - 2);
		}
		return leaderNode;
	}

	/*
	 * 根据Path获取当前leader节点，如果没有子节点，就返回null
	 */
	public String getLeaderNode(String path) throws KeeperException, InterruptedException {
		String leader = null;
		List<String> children = getChildren(path);
		if (children.isEmpty())
			leader = null;
		else {
			Collections.sort(children);
			leader = children.get(0);
		}

		return leader;
	}

	/*
	 * 判断子节点childPath是否为Path下的leader
	 */
	public boolean isLeader(String childPath, String path) throws KeeperException, InterruptedException {
		String leader = getLeaderNode(path);
		if (childPath.equals(leader))
			return true;
		else
			return false;
	}
	
	/*
	 *获得leader，并做点事情
	 */
	public void doSth(){
		try {
			System.out.println(getLeaderNode("/chroot") + " starts to do sth...");
		} catch (KeeperException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 找比该节点大的下一个节点
	 */
	
	/*
	 * 找比bashPath节点小的最大节点
	 */
	public String getPriorBiggestNode(String path, String basePath) throws KeeperException, InterruptedException {
		List<String> children = zk.getChildren(path, false);
		children.add(basePath);
		
		Collections.sort(children);
		int pos = children.indexOf(basePath);
		String node = null;
		if(pos>=0){
			node = children.get(pos-1);
		}else
			node = children.get(0);
		
		return node;
	}
}
