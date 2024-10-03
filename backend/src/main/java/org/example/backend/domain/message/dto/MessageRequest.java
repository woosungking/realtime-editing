package org.example.backend.domain.message.dto;

import lombok.Getter;

@Getter
public class MessageRequest {
	private String content;
	private String sender;
	MessageRequest(){}
	MessageRequest(String content, String sender){
		this.content = content;
		this.sender = sender;

	}
}
