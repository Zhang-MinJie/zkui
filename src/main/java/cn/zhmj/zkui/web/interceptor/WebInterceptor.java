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
package cn.zhmj.zkui.web.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.zhmj.zkui.utils.ServletUtil;

@Component
public class WebInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ServletUtil.INSTANCE.getLogger(this.getClass()).info(request, request.getParameterMap());
		HttpSession session = request.getSession();
		if (!request.getRequestURI().contains("/login") && !request.getRequestURI().contains("/acd/appconfig")) {
			RequestDispatcher dispatcher;
			if (session != null) {
				if (session.getAttribute("authName") == null || session.getAttribute("authRole") == null || session.getAttribute("zk") == null) {
					response.sendRedirect("/login");
					ServletUtil.INSTANCE.getLogger(WebInterceptor.class).info(request, "暂无权限");
					session.invalidate();
					return false;
				}
			} else {
				request.setAttribute("fail_msg", "登陆超时，请重新登陆");
				dispatcher = request.getRequestDispatcher("/login");
				dispatcher.forward(request, response);
				ServletUtil.INSTANCE.getLogger(WebInterceptor.class).info(request, "session失效");
				return false;
			}
		} else if(request.getRequestURI().contains("/login")) {
			session.invalidate();
		}
		// 只有返回true才会继续向下执行，返回false取消当前请求
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// Interceptor>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// Interceptor>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）")
	}
}
