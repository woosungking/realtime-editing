package org.example.backend.domain.message.service;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {
	private final AmqpAdmin amqpAdmin;

	public ExchangeService(AmqpAdmin amqpAdmin) {
		this.amqpAdmin = amqpAdmin;
	}
	public TopicExchange doesExistExchange(String exchangeName){
		TopicExchange topicExchange = new TopicExchange(exchangeName);
		try{
			amqpAdmin.declareExchange(topicExchange);
			return topicExchange;
		}catch(Exception e){
			System.out.println("이미 존재하는 교환기 입니다."+e.getMessage());
			return topicExchange;

		}
	}

	public void DeleteExchange(String string){
		amqpAdmin.deleteExchange(string);
		System.out.println("교환기 삭제 완료.");
	}
}
