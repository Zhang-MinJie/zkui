package cn.zhmj.zkui.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.zhmj.zkui.utils.CmdUtil;

@Controller
public class MonitorController {
	@Autowired
	private Properties zkProperties;

	@RequestMapping(value = "/monitor", method = RequestMethod.GET)
	public String index(Map<String, Object> templateParam,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String zkServer = zkProperties.getProperty("clientUrls");
		String[] zkServerLst = zkServer.split(",");
		StringBuffer stats = new StringBuffer();
		for (String zkObj : zkServerLst) {
			stats.append("<br/><hr/><br/>").append("Server: ").append(zkObj).append("<br/><hr/><br/>");
			String[] monitorZKServer = zkObj.split(":");
			stats.append(CmdUtil.INSTANCE.executeCmd("stat", monitorZKServer[0], monitorZKServer[1]));
			stats.append(CmdUtil.INSTANCE.executeCmd("envi", monitorZKServer[0], monitorZKServer[1]));
		}
		templateParam.put("displayPath", "/");
		templateParam.put("stats", stats);
		return "monitor";
	}
}
