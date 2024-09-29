package org.example.backend.domain.message.service;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;

@Service
public class BindingService {
	private final AmqpAdmin amqpAdmin;

	public BindingService(AmqpAdmin amqpAdmin) {
		this.amqpAdmin = amqpAdmin;
	}

	public void bindingQueue(Queue queue, TopicExchange exchange, Long roomId){
		Binding binding = BindingBuilder.bind(queue).to(exchange).with("room."+roomId);
		System.out.println("바인딩 완료 하였습니당");
		amqpAdmin.declareBinding(binding);
	}
}
