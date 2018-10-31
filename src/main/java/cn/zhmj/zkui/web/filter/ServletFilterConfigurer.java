package cn.zhmj.zkui.web.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class ServletFilterConfigurer {
	@Autowired
    private ServletFilter filter;

    @Bean
    public FilterRegistrationBean<ServletFilter> filterRegistrationBean() {
        FilterRegistrationBean<ServletFilter> registrationBean = new FilterRegistrationBean<ServletFilter>();
        registrationBean.setFilter(filter);

        //设置（模糊）匹配的url
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/login");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(1);
        registrationBean.setEnabled(true);
        return registrationBean;
    }
}
