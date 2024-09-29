package org.example.backend.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {

	//통상 싱글쓰레드로 스케쥴링이 들어가는데, 필요에 따라 병렬로 설정 가능하니 추후에 아이디어 참고 바람.
}
