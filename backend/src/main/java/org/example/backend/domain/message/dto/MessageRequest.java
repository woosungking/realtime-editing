package org.example.backend.domain.message.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageRequest {
	private String content;
	private String sender;
}
