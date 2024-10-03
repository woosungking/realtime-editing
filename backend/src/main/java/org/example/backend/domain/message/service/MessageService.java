package org.example.backend.domain.message.service;

import org.example.backend.domain.message.dto.MessageRequest;
import org.example.backend.domain.message.entity.Message;
import org.example.backend.domain.message.repository.MessageRepository;
import org.example.backend.domain.redis.service.RedisService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;

@Service
public class MessageService {
	private final QueueService queueService;
	private final RabbitTemplate rabbitTemplate;

	private final RedisService redisService;

	private final MessageRepository messageRepository;


	public MessageService(QueueService queueService, RabbitTemplate rabbitTemplate, @Lazy RedisService redisService, MessageRepository messageRepository) {
		this.queueService = queueService;
		this.rabbitTemplate = rabbitTemplate;
		this.redisService = redisService;
		this.messageRepository = messageRepository;
	}

	public void validation(Long roomId, @Nullable String exchangeName){
		//큐가 존재하는지만 보고 없다면 큐를 생성 후 바인딩까지 해줘야함.
		//큐가 존재하면 바인딩은 자동적으로 존재하게 만들었기때문에

		queueService.doesQueueExist(roomId, exchangeName);

	}

	public void sendMessageToExchanger(String exchangeName, Long roomId, Message message){
		System.out.println("sendMessage 실행");
		validation(roomId,exchangeName);
		System.out.println("유효성 검사 종료");
		System.out.println(message.getContent());
		//레디스 저장 로직 할껀데, 아이디어는 키값으로는 "room.*" value 는 message로 ㄱㄱ
		rabbitTemplate.convertSendAndReceive(exchangeName,"room."+roomId,message);
		redisService.setValue(String.valueOf(roomId),message);
		System.out.println("전달완료여");
	}

	public Message popMessage(Long roomId){
		Message message = (Message)rabbitTemplate.receiveAndConvert("room."+roomId);
		return message;
	}

	public void saveMessage(Message message){
		System.out.println("저장완료여");
		messageRepository.save(message);

	}

}
