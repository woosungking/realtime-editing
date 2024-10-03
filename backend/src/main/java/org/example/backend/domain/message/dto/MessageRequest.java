package org.example.backend.domain.message.dto;

import lombok.Getter;

@Getter
public class MessageRequest {
	private String content;
	private String sender;

	private String roomId;
	MessageRequest(){}
	MessageRequest(String content, String sender){
		this.content = content;
		this.sender = sender;

	}

	public void setRoomId(String roomId){
		this.roomId=roomId;
	}
}
