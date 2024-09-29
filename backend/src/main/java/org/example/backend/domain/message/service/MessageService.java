package org.example.backend.domain.message.service;

import org.example.backend.domain.message.dto.MessageRequest;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;

@Service
public class MessageService {
	private final QueueService queueService;
	private final ExchangeService exchangeService;
	private final BindingService bindingService;
	private final RabbitTemplate rabbitTemplate;


	public MessageService(QueueService queueService, ExchangeService exchangeService, BindingService bindingService, RabbitTemplate rabbitTemplate) {
		this.queueService = queueService;
		this.exchangeService = exchangeService;
		this.bindingService = bindingService;
		this.rabbitTemplate = rabbitTemplate;
	}

	public void validation(Long roomId, @Nullable String exchangeName){
		//큐가 존재하는지만 보고 없다면 큐를 생성 후 바인딩까지 해줘야함.
		//큐가 존재하면 바인딩은 자동적으로 존재하게 만들었기때문에

		queueService.doesQueueExist(roomId, exchangeName);

	}

	public void sendMessageToExchanger(String exchangeName, Long roomId, MessageRequest message){
		System.out.println("sendMessage 실행");
		validation(roomId,exchangeName);
		System.out.println("유효성 검사 종료");

		rabbitTemplate.convertSendAndReceive(exchangeName,"room."+roomId,message);
		System.out.println("전달완료여");
	}

}
