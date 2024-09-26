// package org.example.backend.domain.message.controller;
//
// import org.example.backend.domain.message.dto.MessageRequest;
// import org.springframework.messaging.handler.annotation.DestinationVariable;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;
//
// @RestController
// public class MessageController {
// 	@CrossOrigin("*")
// 	@MessageMapping("/{roomId}") // 즉 얘는 수신기의 역할
// 	//StompConfig에서 설정한 /send 뒤에 붙음, 그럼 최종 결과물은 /send/{roomId} 가 되겠지 ?
// 	//일반적인 컨트롤러 클레스에 RequestMapping("/api/vi") 해놓고 매서드에 PostMapping("/create")를 한다고 가정라면
// 	//최종적 으로 프론트에서 요청하는 url은 /api/v1/create 가 되는거라고 생각하면 된다.
// 	@SendTo("/topic/{roomId}") // 얘는 발신기의 역할
// 	// @SendTo("/queue/room/{roomId}") // enableStompBroker에서 /topic과 /queue를 설정을 해둬서 둘중 하나 골라써야함, 이건 컨벤션에 불과하니 참고~
// 	public MessageRequest enter(@DestinationVariable("roomId") Long roomId, MessageRequest message){
// 		return message;
// 	};
// }
