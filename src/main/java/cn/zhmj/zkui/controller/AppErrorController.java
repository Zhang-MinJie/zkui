package cn.zhmj.zkui.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.zhmj.zkui.utils.ServletUtil;

@Controller
public class AppErrorController implements ErrorController {
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorPage(Map<String, Object> templateParam, HttpServletRequest request) {
		ServletUtil.INSTANCE.getLogger(this.getClass()).info(request, request.getParameterMap());
		templateParam.put("error", request.getAttribute("errorMsg") == null ? "404 访问地址错误" : request.getAttribute("errorMsg"));
    	return getErrorPath();
    }

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
