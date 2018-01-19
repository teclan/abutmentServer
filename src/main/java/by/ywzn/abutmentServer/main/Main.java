package by.ywzn.abutmentServer.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

import by.ywzn.abutmentServer.config.ServerConfig;


@Configuration
@EnableJms
@EnableAutoConfiguration
@EnableScheduling
@Import(ServerConfig.class )
@ComponentScan({"by.ywzn.abutmentServer"})
// @PropertySource(value = { "classpath:abutmentServer-application.properties"
// })
@PropertySource({ "${MY_CONFIG_FILE}" })
public class Main {
	 public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
