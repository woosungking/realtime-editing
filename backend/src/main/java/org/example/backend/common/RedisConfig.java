package org.example.backend.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory(){
		return new LettuceConnectionFactory("localhost", 6379);
	}
	// 레디스와의 연결을 위한 Connection생성을 해주는 담당, Lettuce를 사용함.
	@Bean
	public RedisTemplate<String,Object> redisTemplate(){
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(redisConnectionFactory());

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		redisTemplate.setDefaultSerializer(new StringRedisSerializer());

		return redisTemplate;
	}
}
