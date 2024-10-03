package org.example.backend.domain.message.controller;

import org.example.backend.domain.message.dto.MessageRequest;
import org.example.backend.domain.message.entity.Message;
import org.example.backend.domain.message.service.MessageService;
import org.example.backend.domain.room.entity.Room;
import org.example.backend.domain.room.service.RoomService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

	private final RoomService roomService;

	public RestChatController(RabbitTemplate rabbitTemplate, MessageService messageService, RoomService roomService) {
		this.rabbitTemplate = rabbitTemplate;
		this.messageService = messageService;

		this.roomService = roomService;
	}

	@PostMapping("/chat.enter.{roomId}")
	public void receiveMessage(@PathVariable("roomId") Long roomId, @RequestBody MessageRequest messageRequest){
		System.out.println(messageRequest);
		Room room = roomService.doesExistRoom(roomId);
		Message message = new Message.Builder().setContent(messageRequest.getContent()).setSender(messageRequest.getSender()).setRoomId(room).build();
		System.out.println(message.getId());
		System.out.println(message.getContent());
		// exchange는 하드코딩으로 넣을거임,
		messageService.sendMessageToExchanger("chat.exchange",roomId,message);

	}

	@GetMapping("/topic/room.{roomId}")
	public Message sendMessage(@PathVariable("roomId") Long roomId){

//		MessageRequest messageRequest = (MessageRequest)rabbitTemplate.receiveAndConvert("chat.queue");

		return messageService.popMessage(roomId);

	}
}
