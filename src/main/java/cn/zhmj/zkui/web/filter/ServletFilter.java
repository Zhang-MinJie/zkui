package cn.zhmj.zkui.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import cn.zhmj.zkui.utils.ServletUtil;

/**
 * Servlet Filter implementation class ServletFilter 注解注册过滤器：实现
 * javax.servlet.Filter接口 filterName 是过滤器的名字 urlPatterns 是需要过滤的请求
 * ，这里只过滤servlet/* 下面的所有请求
 */
@Component
@WebFilter(filterName = "ServletFilter", urlPatterns = "/*")
public class ServletFilter implements Filter {

	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
		// 过滤器被销毁。
		ServletUtil.INSTANCE.getLogger(this.getClass()).debug("过滤器被销毁");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ServletUtil.INSTANCE.getLogger(this.getClass()).debug((HttpServletRequest) request, request.getParameterMap());
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		// 初始化过滤器。
		ServletUtil.INSTANCE.getLogger(this.getClass()).debug("初始化过滤器");
	}
}