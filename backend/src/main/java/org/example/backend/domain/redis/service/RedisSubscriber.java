package org.example.backend.domain.redis.service;

import org.example.backend.domain.message.dto.MessageRequest;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
public class RedisSubscriber implements MessageListener{

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messageSendingOperations;

	public RedisSubscriber(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, SimpMessageSendingOperations messageSendingOperations) {
		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
		this.messageSendingOperations = messageSendingOperations;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			// Redis에서 수신한 메시지를 JSON 문자열로 역직렬화
			System.out.println("수신한 메시지"+message);
			String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
			System.out.println("수신한 메시지: " + publishMessage);


			// JSON 문자열을 MessageRequest 객체로 변환
			MessageRequest messageRequest = objectMapper.readValue(publishMessage, MessageRequest.class);
			System.out.println("오브젝트 메퍼"+messageRequest);

			// 프론트엔드로 WebSocket 메시지 전송 (/topic/room 경로로 전송)
			messageSendingOperations.convertAndSend("/topic/room", messageRequest);
		} catch (Exception e) {
			System.err.println("메시지 역직렬화 오류: " + e.getMessage());
		}
	}

} //레디스 템플릿을 사용하는 아이디어, 적용해보자 지금은 테스트만