package org.example.backend.domain.message.controller;

import java.time.LocalDateTime;

import org.example.backend.domain.message.dto.MessageRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ChatController {
	private final static String CHAT_QUEUE_NAME = "chat.queue";
	private final RabbitTemplate rabbitTemplate;
	private final SimpMessagingTemplate simpMessagingTemplate;
	@Autowired // 의존성 주입이 오토와이어 안쓰	먄 안돼는데.. 알아보기
	ChatController(RabbitTemplate rabbitTemplate, SimpMessagingTemplate simpMessagingTemplate){
		this.rabbitTemplate = rabbitTemplate;
		this.simpMessagingTemplate = simpMessagingTemplate;

	}


	// /pub/chat.message.{roomId} 로 요청하면 브로커를 통해 처리
	// /exchange/chat.exchange/room.{roomId} 를 구독한 클라이언트에 메시지가 전송된다.
	@MessageMapping("chat.enter.{roomId}")
	public void enterUser(@DestinationVariable("roomId") Long roomId, @Payload MessageRequest message) {
		System.out.println("operation enter");
		System.out.println(message.getContent());
		rabbitTemplate.convertAndSend("chat.exchange", "room." + roomId, message);

	}

	@MessageMapping("chat.message.{roomId}")
	public void sendMessage(@DestinationVariable("roomId") Long roomId, @Payload MessageRequest message) {
		rabbitTemplate.convertAndSend("chat.exchange", "room." + roomId, message);
	}

	//기본적으로 chat.queue가 exchange에 바인딩 되어있기 때문에 모든 메시지 처리
	@RabbitListener(queues = "chat.queue")
	public void receive(MessageRequest message) {
		System.out.println("received : " + message.getContent());
		// rabbitTemplate.convertSendAndReceive("/topic.room.1", message.getContent());
		// 이 부분은 ACK를 받아야 다음작업으로 넘어가기때문에, 웹소켓에서 쓰기에는 너무 느림.
		simpMessagingTemplate.convertAndSend("/topic/room.1", message.getContent());

	}
}