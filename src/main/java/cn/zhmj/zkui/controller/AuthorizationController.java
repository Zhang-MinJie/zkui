package cn.zhmj.zkui.controller;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.zhmj.zkui.utils.LdapAuth;
import cn.zhmj.zkui.utils.zooKeeper.ZooKeeperUtil;

/**
 * 授权
 * 
 * @author zhangminjie
 *
 */
@Controller
public class AuthorizationController {
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);
	@Value("${system.ldapAuth}")
	private boolean ldapAuth;
	@Value("${system.ldapUrl}")
	private String ldapUrl;
	@Value("${system.ldapDomain}")
	private String ldapDomain;
	@Value("${system.ldapRoleSet}")
	private String ldapRoleSetStr;
	@Value("${system.userSet}")
	private String userSetStr;

	/**
	 * 登陆页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(HttpServletRequest request, HttpServletResponse response) {
		return "login";
	}

	/**
	 * 登陆
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("用户登陆 - {username: {}, password: {}}", username, password);
		HttpSession session = request.getSession(true);
		String role = null;
		Boolean authenticated = false;
		// if ldap is provided then it overrides roleset.
		if (ldapAuth) {
			authenticated = new LdapAuth().authenticateUser(ldapUrl, username, password, ldapDomain);
			if (authenticated) {
				JSONArray jsonRoleSet = JSONObject.parseObject(ldapRoleSetStr).getJSONArray("users");
				for (Iterator<Object> it = jsonRoleSet.iterator(); it.hasNext();) {
					JSONObject jsonUser = (JSONObject) it.next();
					if (jsonUser.get("username") != null && jsonUser.get("username").equals("*")) {
						role = (String) jsonUser.get("role");
					}
					if (jsonUser.get("username") != null && jsonUser.get("username").equals(username)) {
						role = (String) jsonUser.get("role");
					}
				}
				if (role == null) {
					role = ZooKeeperUtil.ROLE_USER;
				}

			}
		} else {
			JSONArray jsonRoleSet = JSONObject.parseObject(userSetStr).getJSONArray("users");
			for (Iterator<Object> it = jsonRoleSet.iterator(); it.hasNext();) {
				JSONObject jsonUser = (JSONObject) it.next();
				if (jsonUser.get("username").equals(username) && jsonUser.get("password").equals(password)) {
					authenticated = true;
					role = (String) jsonUser.get("role");
				}
			}
		}
		if (authenticated) {
			session.setAttribute("authName", username);
			session.setAttribute("authRole", role);
			response.sendRedirect("/home");
		} else {
			session.setAttribute("flashMsg", "用户名和密码错误，请重新输入");
			response.sendRedirect("/login");
		}
	}

	/**
	 * 退出当前账号
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		return "login";
	}
}
