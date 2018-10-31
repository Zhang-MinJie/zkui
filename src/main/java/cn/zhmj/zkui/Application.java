package cn.zhmj.zkui;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/** 
 * @author 
 * @date 2018年3月22日
 * @version 0.0.1
 *
 */
@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
	})
@Configuration
@EnableAutoConfiguration
@ServletComponentScan
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		logger.info("启动成功。");
    }
	
	@Bean("zkProperties")
	@ConfigurationProperties(prefix = "zookeeper")
	public Properties getZkProperties() {
		return new Properties();
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.freemarker")
	@SuppressWarnings("deprecation")
	public FreeMarkerConfigurer freeMarkerConfigurer() throws ClassNotFoundException {
		freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_SLF4J);
	    return new FreeMarkerConfigurer();
	}

}
