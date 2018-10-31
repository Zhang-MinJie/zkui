/**
 *
 * Copyright (c) 2014, Deem Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package cn.zhmj.zkui.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.zhmj.zkui.service.ZooKeeperService;

@WebListener
public class SessionListener implements HttpSessionListener {

	private static final Logger logger = LoggerFactory.getLogger(SessionListener.class);

	@Autowired
	private ZooKeeperService zooKeeperService;

	@Autowired
	private Properties zkProperties;

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		logger.info("Session - 创建");
		ZooKeeper zk = null;
		try {
			zk = zooKeeperService.conn(zkProperties);
		} catch (IOException e) {
			logger.error("[zk连接异常]", e);
		} catch (InterruptedException e) {
			logger.error("[zk连接异常]", e);
		}
		event.getSession().setAttribute("zk", zk);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		if (event.getSession().getAttribute("zk") != null) {
			try {
				ZooKeeper zk = (ZooKeeper) event.getSession().getAttribute("zk");
				zk.close();
			} catch (InterruptedException ex) {
				logger.error("[Session销毁无法关闭ZooKeeper] - {}", Arrays.toString(ex.getStackTrace()));
			}
		}
		logger.info("Session - 销毁");
	}

}
