package io.pivotal.john.sitestpoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableIntegration
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public IntegrationFlow jazz(MagicMaker magicMaker) {
		return IntegrationFlows
			.from(Jms.messageDrivenChannelAdapter(jmsConnectionFactory)
				.destination("the.vibe")
				.jmsMessageConverter(messageConverter))
			.<Groove>handle((groove, __) -> magicMaker.bibby(groove))
			.log()
			.handle(Jms.outboundAdapter(jmsConnectionFactory)
				.configureJmsTemplate(c -> c.jmsMessageConverter(messageConverter))
				.destination("the.air"))
			.get();
	}

	@Bean
	public MappingJackson2MessageConverter messageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("DocumentType");
		return converter;
	}

	@Autowired
	private ConnectionFactory jmsConnectionFactory;

	@Autowired
	private MessageConverter messageConverter;

}
