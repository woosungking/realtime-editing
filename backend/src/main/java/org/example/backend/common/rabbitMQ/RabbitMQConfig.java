package org.example.backend.common.rabbitMQ;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
	//Queue 등록
	@Bean
	public Queue queue() {
		return new Queue("chat.queue");
	}

	//Exchange 등록
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange("chat.exchange", true, false);
	} // 클라이언트가 메시지를 발행하면 exchange 로 들어가고, exchange가 메시지를 보고 알맞는 queue로 보내줌.
	  // 조금더 정확하게 말 하면 메시지가 발행이 되면 controller로 들어간 후 컨트롤러에서 여기서 정의해놓은 exchange로 메시지를 전달
	  //rabbitTemplate.convertAndSend("chat.exchange", "room." + roomId, message); 컨트롤러에서 위에서 정의해준 exchange의 경로로 메시지를 다시 전달해줌
	  //교환기에서 큐로 보낼때 아무 큐로 보낼 수 없기때문에 이걸 구분하기 위한 키를 "라우팅키"라고 한다. 컨트롤러에서 교환기의 주소 chat.change 뒤에 room. 으로 넣은 부분이 라우팅 키 이다.

	//Exchange와 Queue 바인딩
	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {

		return BindingBuilder.bind(queue).to(exchange).with("room.*");
		// 교환기와 큐를 만들었으면, 스프링에게 어떤 교환기랑 어떤 큐랑 상호작용을 하는지 알려줘야하는데 해당 부분이다.
		// rabbitTemplate.convertAndSend("chat.exchange", "room." + roomId, message);
		// 컨트롤러에서 교환기로 메시지를 넘기는 과정에서 room.+roomId 로 했는데, 이 부분이 라우팅 키 이다, 바인더 부분에서는
		// 라우팅키의 규칙을 알려주는것이다.
	}
	// 이 부분이 없어도 Exchanger로 메시지는 들어가지만, 라우팅이 되어진 큐가 없어서 메시지가 소비되지 않음.
	// 교환기의 메시지 마빡부분에 room.1 , room.2, room.3 이런식으로 노비마냥 낙인이 있으면, binding 부분에서 지정한 라우팅 규칙을 보고
	// 해당 이름에 일치하는 큐에 메시지를 보내줌.

	@Bean
	SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(jsonMessageConverter());
		factory.setPrefetchCount(50);
		return factory;
	}

	/* messageConverter를 커스터마이징 하기 위해 Bean 새로 등록 */

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setHost("localhost");
		factory.setVirtualHost("/");
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setPort(5672);
		return factory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		rabbitTemplate.setRoutingKey("room.*");
		return rabbitTemplate;
		//rabbitTemplate.convertAndSend("chat.exchange", "room." + roomId, message);
		// 컨트롤러에서 rabbitTemplate을 이용해서 convertAndSend도 하는건데, 이거 관련해서 설정을 해주는 부분이라고 보면 된다.
	}



	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		// //LocalDateTime serializable을 위해
		// ObjectMapper objectMapper = new ObjectMapper();
		// objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		// objectMapper.registerModule(dateTimeModule());
		// //toMessage를 호출 자동 직렬화
		// return new Jackson2JsonMessageConverter(objectMapper);
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public JavaTimeModule dateTimeModule() {
		return new JavaTimeModule();
	}
}
