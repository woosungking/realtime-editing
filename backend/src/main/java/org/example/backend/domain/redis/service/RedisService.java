package org.example.backend.domain.redis.service;

import java.time.Duration;

public interface RedisService {

	public void setValue(String key, String value);
	public void setValue(String key, String value, Duration duration);
	public String getValue(String key);
	public void deleteValue(String key);

}
