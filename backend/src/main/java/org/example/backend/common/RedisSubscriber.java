// package org.example.backend.common;
//
// import org.springframework.data.redis.connection.Message;
// import org.springframework.data.redis.connection.MessageListener;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.messaging.simp.SimpMessageSendingOperations;
// import org.springframework.stereotype.Service;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
//
// import lombok.RequiredArgsConstructor;
//
// @Service
// public class RedisSubscriber {
//
// 	public void onMessage(String message, String channel) {
// 		System.out.println("Received message: " + message + " from channel: " + channel);
// 	}
// } //레디스 템플릿을 사용하는 아이디어, 적용해보자 지금은 테스트만