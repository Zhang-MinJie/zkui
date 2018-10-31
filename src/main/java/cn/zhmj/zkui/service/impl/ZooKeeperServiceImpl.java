package cn.zhmj.zkui.service.impl;

import java.io.IOException;
import java.util.Properties;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.zhmj.zkui.service.ZooKeeperService;
import cn.zhmj.zkui.utils.zooKeeper.ZooKeeperUtil;
/**
 * ZooKeeper 业务类
 * @author 章敏杰
 * @createdTime 2018年10月30日
 */
@Service
public class ZooKeeperServiceImpl implements ZooKeeperService {

    private final static Logger logger = LoggerFactory.getLogger(ZooKeeperServiceImpl.class);
    /**
     * 连接zk
     */
	@Override
	public ZooKeeper conn(Properties zkProperties) throws IOException, InterruptedException {
		logger.info("load params - {}", JSONObject.toJSONString(zkProperties));
        
        String defaultAcl = zkProperties.getProperty("defaultAcl");
        ZooKeeperUtil.INSTANCE.setDefaultAcl(defaultAcl);

		String zkServer = zkProperties.getProperty("clientUrls");
        Integer zkSessionTimeout = Integer.parseInt(zkProperties.getProperty("sessionTimeout"));
        ZooKeeper zooKeeper = ZooKeeperUtil.INSTANCE.createZKConnection(zkServer, zkSessionTimeout);
        return zooKeeper;
	}
	/**
	 * 添加节点
	 */
	@Override
	public void addNode(String currentPath, String newNode, ZooKeeper zk) throws KeeperException, InterruptedException {
		ZooKeeperUtil.INSTANCE.createFolder(currentPath + newNode, "foo", "bar", zk);
	}
}
