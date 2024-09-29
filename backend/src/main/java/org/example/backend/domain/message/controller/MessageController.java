// package org.example.backend.domain.message.controller;
//
// import org.example.backend.domain.message.dto.MessageRequest;
// import org.example.backend.domain.redis.service.RedisPublisher;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.redis.listener.ChannelTopic;
// import org.springframework.messaging.handler.annotation.DestinationVariable;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;
//
// import lombok.Getter;
// import lombok.RequiredArgsConstructor;
//
// @RestController
// public class MessageController {
//
// 	private final RedisPublisher redisPublisher;
// 	public MessageController(RedisPublisher redisPublisher) {
// 		this.redisPublisher = redisPublisher;
// 	}
//
// 	@MessageMapping("/redis") // 즉 얘는 수신기의 역할
// 	//설정한 /publish 뒤에 붙음, 그럼 최종 결과물은 /send/{roomId} 가 되겠지 ?
// 	//일반적인 컨트롤러 클레스에 RequestMapping("/api/vi") 해놓고 매서드에 PostMapping("/create")를 한다고 가정라면
// 	//최종적 으로 프론트에서 요청하는 url은 /api/v1/create 가 되는거라고 생각하면 된다.
// 	// @SendTo("/queue/room/{roomId}") // enableStompBroker에서 /topic과 /queue를 설정을 해둬서 둘중 하나 골라써야함, 이건 컨벤션에 불과하니 참고~
// 	public MessageRequest enter(MessageRequest message){
// 		ChannelTopic channelTopic = new ChannelTopic("/topic/room");
// 		//RedisConfig 에 chaneeltopic에있는 엔드포인트랑 하나라도 일치하지 않으면 subscriber 의 onMessage로 전달이 안도ㅔ요오
// 		System.out.println("zzzz");
// 		redisPublisher.publisher(channelTopic, message);
// 		return message;
// 	};
// }
