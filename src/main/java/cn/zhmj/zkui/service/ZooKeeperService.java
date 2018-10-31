package cn.zhmj.zkui.service;

import java.io.IOException;
import java.util.Properties;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public interface ZooKeeperService {
	public ZooKeeper conn(Properties zkProperties) throws IOException, InterruptedException;
	public void addNode(String currentPath, String newNode, ZooKeeper zk) throws KeeperException, InterruptedException;
}
