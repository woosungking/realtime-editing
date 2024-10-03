package org.example.backend.domain.message.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.backend.domain.redis.service.RedisService;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QueueSchedulingService {

	private final AmqpAdmin amqpAdmin;
	private final RestTemplate restTemplate; // 외부 api요청 보내기 가능함, 레빗에다 보내는 중인데 설정파일을 레빗에 의존적이게 적어놨으니 참고
	private final QueueService queueService;

	private final RedisService redisService;

	public QueueSchedulingService(AmqpAdmin amqpAdmin, RestTemplate restTemplate, QueueService queueService, RedisService redisService) {
		this.amqpAdmin = amqpAdmin;
		this.restTemplate = restTemplate;
		this.queueService = queueService;
		this.redisService = redisService;
	}

	@Scheduled(fixedRate = 60000)
	public void emptyQueueCleaning(){
		List<Map<String,Object>> queueList = restTemplate.getForObject("http://localhost:15672/api/queues", List.class);
		// 테스트 해보니까 아래와 같은 형식임. List<Map<"string","object">> 로 받으면 되겠다.
		// [
			// {
			// 	"name": "chat.queue",
			// 	"messages": 0,
			// 	"durable": true,
			// 	"state": "running",
			// 	"message_stats": {
			// 	"ack": 28,
			// 		"deliver": 28,
			// 		"get": 0,
			// 		"publish": 29
				// }
			// },{},{}
		//}

		for(Map<String,Object> queue: Objects.requireNonNull(queueList)){
			// System.out.println(queue.get("messages").getClass().getName()); 타입 검사 해봄. 믄지열이 아니넹 ㅋㅋ
			if(queue.get("messages").equals(0)){
				String queueName= (String) queue.get("name"); // room.1, room.2 이런식으로 나오기때문에 슬라이싱
				int indexNum = queueName.lastIndexOf(".");
				String roomIdStr = queueName.substring(indexNum+1);
				try {
					Long roomId = Long.parseLong(roomIdStr);
					System.out.println("비어있는 큐 청소중 큐 이름:"+roomId);
					queueService.deleteQueue(roomId);
				}catch (Exception e){
					System.out.println("큐의 엔드포인트 : "+roomIdStr + " 로 인해 필터링이 안됩니다.(숫자가 아님.)");
					System.out.println("모든큐를 필터릴 한거니 놀라지 마세용");
				}finally {
					continue;
				}
			}
			}

		}

		@Scheduled(fixedRate = 10000)
		public void cachingData() throws JsonProcessingException {
//			redisService.showAllValue();
			redisService.getAndSaveAllValue();
			System.out.println("ㅋㅋㅋ");
		}

	}

