package org.example.backend.domain.message.service;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
	private final AmqpAdmin amqpAdmin;
	private final BindingService bindingService;

	private final ExchangeService exchangeService;

	public QueueService(AmqpAdmin amqpAdmin, BindingService bindingService, ExchangeService exchangeService) {
		this.amqpAdmin = amqpAdmin;
		this.bindingService = bindingService;
		this.exchangeService = exchangeService;
	}

	public Queue doesQueueExist(Long roomId, String exchangeName){
		String queueName = "room."+roomId;
		Queue queue = new Queue(queueName);
		if(amqpAdmin.getQueueProperties(queueName)!=null){
			System.out.println("큐가 존재합니당");
			return queue;
		}else{
			System.out.println("큐가 존재하지 않아 만들게요");
			amqpAdmin.declareQueue(queue);
			TopicExchange topicExchange = exchangeService.doesExistExchange(exchangeName);
			bindingService.bindingQueue(queue,topicExchange,roomId);
			return queue;
		}
	}

	public void deleteQueue(Long roomId){
		String queueName = "room." + roomId;
		amqpAdmin.deleteQueue(queueName);
	}




}
