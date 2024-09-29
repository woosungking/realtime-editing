package org.example.backend.domain.message.controller;

import java.util.Objects;

import org.example.backend.domain.message.dto.MessageRequest;
import org.example.backend.domain.message.service.MessageService;
import org.example.backend.domain.message.service.QueueService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/publish")
public class RestChatController {

	private final RabbitTemplate rabbitTemplate;
	private final MessageService messageService;

	public RestChatController(RabbitTemplate rabbitTemplate, MessageService messageService) {
		this.rabbitTemplate = rabbitTemplate;
		this.messageService = messageService;

	}

	@PostMapping("/chat.enter.{roomId}")
	public void receiveMessage(@PathVariable("roomId") Long roomId, @RequestBody MessageRequest message){
		System.out.println(message);
		// exchange는 하드코딩으로 넣을거임,
		messageService.sendMessageToExchanger("chat.exchange",roomId,message);

	}

	@GetMapping("/topic/room.{roomId}")
	public MessageRequest sendMessage(@PathVariable("roomId") Long roomId){
		MessageRequest messageRequest = (MessageRequest)rabbitTemplate.receiveAndConvert("chat.queue");
		return messageRequest;

	}
}
