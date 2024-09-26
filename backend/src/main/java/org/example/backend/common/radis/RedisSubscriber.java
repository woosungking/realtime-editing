package org.example.backend.common;

import org.example.backend.domain.message.dto.MessageRequest;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

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
		//pattern: 메시지가 발행된 채널의 패턴입니다. 이는 사용자가 구독한 채널이 아니라, 해당 메시지가 발행된 채널의 패턴을 나타냅니다.
		String publishMessage = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());

		MessageRequest messageRequest = objectMapper.convertValue(publishMessage, MessageRequest.class);

		messageSendingOperations.convertAndSend("/topic/room",messageRequest);
	}
} //레디스 템플릿을 사용하는 아이디어, 적용해보자 지금은 테스트만