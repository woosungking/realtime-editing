package org.example.backend.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns("http://localhost:5173")  // 와일드카드 패턴 사용 가능
			.allowedMethods("GET", "POST", "PUT", "DELETE");
	}

	@Bean
	public RestTemplate restTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("guest", "guest"));
		//rabbitMQ 플러그인을 이용해서 rabbit에 api요청을 하기위해 인증정보를 넣어야하는데, 여기에 넣으면 종속적이라. 서비스 단으로 빼도 될듯
		//당장은  다른곳에 요청 할 일이 없으니 의존성 주입과 동시에 설정해놓았음.
		return restTemplate;
	}
}