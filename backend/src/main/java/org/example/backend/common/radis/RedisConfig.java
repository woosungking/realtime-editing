package org.example.backend.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import jakarta.annotation.PostConstruct;

@Configuration
public class RedisConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory(){
		return new LettuceConnectionFactory("localhost", 6379);
	}
	// 레디스와의 연결을 위한 Connection생성을 해주는 담당, Lettuce를 사용함.
	@Bean
	public RedisTemplate<String,Object> redisTemplate(){
		//레디스에 데이터를 읽고 쓰기위해서는 직렬화가 일어나야하는데, 정의를 해주는곳임
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(redisConnectionFactory());

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		redisTemplate.setDefaultSerializer(new StringRedisSerializer());

		return redisTemplate;
	}

	@Bean
	public MessageListenerAdapter listenerAdapter(org.example.backend.common.RedisSubscriber subscriber) {
		return new MessageListenerAdapter(subscriber, "onMessage");
		//Redis로부터 오는 메시지를 잘 듣고 그에 맞는 행동을 해줘”라는 역할을 해요. 함수가 하는 일은 subscriber라는 친구에게 이 도구를 연결해주는 거예요.
		//RedisSubscriber 는 내가 만드는거임. 핸들러마냥
		//onMessage 는 내가 만든 RedisSubscriber 안에있는 메서드임.
	}
	@Bean
	public ChannelTopic channelTopic() {
		return new ChannelTopic("/topic");
		// 구독주소, 프론트에서 /topic 으로 구독설정을 하도록 민들어 줌.
	}
	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(
		RedisConnectionFactory connectionFactory,
		MessageListenerAdapter listenerAdapter,
		ChannelTopic channelTopic
	) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, channelTopic);
		return container;
	}
	@PostConstruct
	public void init() {
		// 초기화 작업을 수행하는 코드 작성
		System.out.println("RedisConfig initialized");
		// 필요하다면 RedisTemplate을 이용하여 초기 데이터를 설정할 수도 있음
	}
}
