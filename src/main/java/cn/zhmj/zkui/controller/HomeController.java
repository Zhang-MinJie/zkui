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
package cn.zhmj.zkui.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.zhmj.zkui.service.ZooKeeperService;
import cn.zhmj.zkui.utils.zooKeeper.LeafBean;
import cn.zhmj.zkui.utils.zooKeeper.ZKNode;
import cn.zhmj.zkui.utils.zooKeeper.ZooKeeperUtil;

@Controller
public class HomeController {

	@Autowired
	private ZooKeeperService zooKeeperService;

	@Autowired
	private Properties zkProperties;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "home";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String homeView(Map<String, Object> templateParam, HttpServletRequest request)
			throws KeeperException, InterruptedException {
		String zkPath = request.getParameter("zkPath");
		String navigate = request.getParameter("navigate");
		HttpSession session = request.getSession();
		ZooKeeper zk = (ZooKeeper) session.getAttribute("zk");
		String authRole = session.getAttribute("authRole") == null ? ZooKeeperUtil.ROLE_USER
				: (String) session.getAttribute("authRole");

		if (StringUtils.isEmpty(zkPath)) {
			zkPath = "/";
		}
		templateParam.put("displayPath", zkPath);
		templateParam.put("parentPath", zkPath.substring(0, zkPath.lastIndexOf("/")));
		templateParam.put("currentPath", "/".equals(zkPath) ? zkPath : zkPath + "/");

		ZKNode zkNode = ZooKeeperUtil.INSTANCE.listNodeEntries(zk, templateParam.get("displayPath").toString(),
				authRole);
		templateParam.put("nodeLst", zkNode.getNodeLst());
		templateParam.put("leafLst", zkNode.getLeafBeanLSt());
		templateParam.put("breadCrumbLst", templateParam.get("displayPath").toString().split("/"));
		templateParam.put("scmRepo", zkProperties.getProperty("scmRepo"));
		templateParam.put("scmRepoPath", zkProperties.getProperty("scmRepoPath"));
		templateParam.put("navigate", navigate);
		if (session.getAttribute("flashMsg") != null) {
			templateParam.put("flashMsg", session.getAttribute("flashMsg"));
			session.setAttribute("flashMsg", null);
		}
		templateParam.put("authName", session.getAttribute("authName"));
		templateParam.put("authRole", session.getAttribute("authRole"));
		return "home";
	}

	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String homeOp(@RequestParam("action") String action, @RequestParam("displayPath") String displayPath,
			@RequestParam("currentPath") String currentPath, @RequestParam("newNode") String newNode,
			@RequestParam("newProperty") String newProperty, @RequestParam("newValue") String newValue,
			@RequestParam("searchStr") String searchStr, HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> templateParam) throws KeeperException, InterruptedException, IOException {
		HttpSession session = request.getSession();
		String authRole = (String) session.getAttribute("authRole");
		ZooKeeper zk = (ZooKeeper) session.getAttribute("zk");

		switch (action) {
		case "Save Node":
			if (!newNode.equals("") && !currentPath.equals("") && authRole.equals(ZooKeeperUtil.ROLE_ADMIN)) {
				// Save the new node.
				zooKeeperService.addNode(currentPath, newNode, zk);
				request.getSession().setAttribute("flashMsg", "Node created!");
			}
			break;
		case "Save Property":
			if (!newProperty.equals("") && !currentPath.equals("") && authRole.equals(ZooKeeperUtil.ROLE_ADMIN)) {
				// Save the new node.
				ZooKeeperUtil.INSTANCE.createNode(currentPath, newProperty, newValue, zk);
				request.getSession().setAttribute("flashMsg", "Property Saved!");
			}
			break;
		case "Update Property":
			if (!newProperty.equals("") && !currentPath.equals("") && authRole.equals(ZooKeeperUtil.ROLE_ADMIN)) {
				// Save the new node.
				ZooKeeperUtil.INSTANCE.setPropertyValue(currentPath, newProperty, newValue, zk);
				request.getSession().setAttribute("flashMsg", "Property Updated!");
			}
			break;
		case "Search":
			Set<LeafBean> searchResult = ZooKeeperUtil.INSTANCE.searchTree(searchStr, zk, authRole);
			templateParam.put("searchResult", searchResult);
			templateParam.put("displayPath", displayPath);
			return "forward:search";
		case "Delete":
			String[] nodeChkGroup = request.getParameterValues("nodeChkGroup");
			String[] propChkGroup = request.getParameterValues("propChkGroup");
			if (authRole.equals(ZooKeeperUtil.ROLE_ADMIN)) {
				if (propChkGroup != null) {
					for (String prop : propChkGroup) {
						List<String> delPropLst = Arrays.asList(prop);
						ZooKeeperUtil.INSTANCE.deleteLeaves(delPropLst, zk);
					}
				}
				if (nodeChkGroup != null) {
					for (String node : nodeChkGroup) {
						List<String> delNodeLst = Arrays.asList(node);
						ZooKeeperUtil.INSTANCE.deleteFolders(delNodeLst, zk);
					}
				}
				request.getSession().setAttribute("flashMsg", "Delete Completed!");
			}
			break;
		default:
		}
		return "redirect:home?zkPath=" + displayPath;
	}

	@RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
	public String search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "search";
	}
}
