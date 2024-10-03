package org.example.backend.domain.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.domain.message.entity.Message;
import org.example.backend.domain.message.service.MessageService;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final MessageService messageService;

    public RedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, MessageService messageService) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.messageService = messageService;
    }

    public void setValue(String key, Object value) {

        String parsedMessage;
        try {
            parsedMessage = objectMapper.writeValueAsString(value);
            System.out.println("제이슨=> 문자열 파싱 완료염");
        } catch (JsonProcessingException e) {
            System.out.println("제이슨 => 문자열로 파싱중 오류 발생 :  " + e);
            throw new RuntimeException(e);
        }
        System.out.println("레디스에 넣습니당");
        System.out.println(parsedMessage);
        ListOperations<String, Object> messageList = redisTemplate.opsForList();
        messageList.leftPush(key, parsedMessage);
//        {
//            "content":"내맥북..",
//                "sender":"앱등이"
//        }

        //일반적인 값만, 넣으면 모르겠는데. 하나의 키에 값이 여러개가 추가되어야 하는 상황이라 opsforlist를 사용했음
    }

//    public void setValue(String key, MessageRequest value) {
//        try {
//            // RedisTemplate을 사용하여 직접 저장
//            ListOperations<String, Object> messageList = redisTemplate.opsForList();
//            messageList.leftPush(key, value);
//            System.out.println("레디스에 객체 저장 성공: " + value);
//        } catch (Exception e) {
//            System.out.println("레디스에 저장 중 오류 발생: " + e.getMessage());
//            throw new RuntimeException("레디스에 값을 저장하는 데 실패했습니다.", e);
//        }
//    }

    public void showAllValue(){
        Set<String> allKeys = redisTemplate.keys("*"); //채팅방은 room. 으로 시작하니 필터링을 걸어줌

        for(String key : allKeys){
            List<Object> message = redisTemplate.opsForList().range(key,0,-1);
            System.out.println(message);
        }

    }

    public void getAndSaveAllValue() throws JsonProcessingException {
        Set<String> allkeys = redisTemplate.keys("*");
        for(String key : allkeys){
            while (true) {
                Object popMessage = redisTemplate.opsForList().rightPop(key);
                if(popMessage == null){
                    break;
                }
                String stredMessage = (String)popMessage;
                System.out.println(stredMessage);
                Message message = objectMapper.readValue(stredMessage, Message.class);
                messageService.saveMessage(message);



            }
            }

        }

    }

