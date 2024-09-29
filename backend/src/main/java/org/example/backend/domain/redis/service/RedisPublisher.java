// package org.example.backend.domain.redis.service;
//
// import org.example.backend.domain.message.dto.MessageRequest;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.listener.ChannelTopic;
// import org.springframework.stereotype.Service;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
//
// @Service
// public class RedisPublisher {
// 	private final RedisTemplate<String, Object> redisTemplate;
//
// 	public RedisPublisher(RedisTemplate<String, Object> redisTemplate) {
// 		this.redisTemplate = redisTemplate;
// 	}
//
// 	public void publisher(ChannelTopic topic, MessageRequest message){
// 		try {
// 			redisTemplate.convertAndSend(topic.getTopic(), message);
// 		} catch (Exception e) {
// 			System.err.println("메시지 직렬화 오류: " + e.getMessage());
// 		}
// 	}
// }
