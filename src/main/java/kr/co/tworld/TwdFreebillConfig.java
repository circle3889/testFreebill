package kr.co.tworld;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TwdFreebillConfig extends AbstractCloudConfig {

	@Value("${services.redis.name}")
	private String redisName;

	@Value("${services.database.name}")
	private String databaseName;
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return connectionFactory().redisConnectionFactory(redisName);
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
	    RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
	    template.setConnectionFactory(redisConnectionFactory());
	    return template;
	}
	
	@Bean
	public DataSource dataSource() {
	    return connectionFactory().dataSource(databaseName);
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate test(){
		return new RestTemplate();
	}

}