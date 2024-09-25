package org.example.backend.domain.redis.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService{

	private final RedisTemplate<String, Object> redisTemplate;
	@Override
	public void setValue(String key, String value) {
		ValueOperations<String, Object> values = redisTemplate.opsForValue();
		//ValueOperation 들어가보니까 redisTemplate.opsForValue 를 사용하니까 return ValueOperations를 반환함.
		values.set(key,value);
		//config에서 설정한 직렬화 방식으로 직렬화가 된다
	}

	@Override
	public void setValue(String key, String value, Duration duration) {
		ValueOperations<String, Object> values = redisTemplate.opsForValue();
		values.set(key,value,duration );

	}

	@Override
	public String getValue(String key)  {
		ValueOperations<String, Object> values = redisTemplate.opsForValue();
		if (values.get(key) == null) return "";
		return String.valueOf(values.get(key));
	}

	@Override
	public void deleteValue(String key) {
		redisTemplate.delete(key);
	}
}
