package org.example.backend.domain.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
