package by.ywzn.abutmentServer.config;

import javax.jms.Topic;
import javax.sql.DataSource;

import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ServerConfig {
	private @Value("${mq.reciveTopic}") String reciveTopic;

	private @Value("${jdbc.driverClassName}") String driverClassName;
	private @Value("${jdbc.url}") String url;
	private @Value("${jdbc.username}") String username;
	private @Value("${jdbc.password}") String password;

	@Bean
	public Topic topic() {
		return new ActiveMQTopic(reciveTopic);
	}

	@Bean(name = "dataSource")
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	@Bean(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
}
