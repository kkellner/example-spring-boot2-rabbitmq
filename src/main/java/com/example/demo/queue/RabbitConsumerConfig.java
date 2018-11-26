package com.example.demo.queue;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.retry.policy.MapRetryContextCache;
import org.springframework.retry.policy.RetryContextCache;

@Configuration
@ServiceScan
public class RabbitConsumerConfig extends AbstractCloudConfig implements RabbitListenerConfigurer {
	
    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());

    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }
    
	@Bean
	public SimpleRabbitListenerContainerFactory myRabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(this.connectionFactory().rabbitConnectionFactory());
		
		//RetryContextCache cache=new MapRetryContextCache();
		//MissingMessageIdAdvice missingIdAdvice=new MissingMessageIdAdvice(cache);
		
		//factory.setAdviceChain(new Advice[] { statefulRetryOperationsInterceptor() } );
		return factory;
	}
		
	@Bean
	public StatefulRetryOperationsInterceptor statefulRetryOperationsInterceptor(){
	   return RetryInterceptorBuilder.stateful()
	          .maxAttempts(5)
	          .backOffOptions(1000,2.0,10000)
	          .build();
	}
}
