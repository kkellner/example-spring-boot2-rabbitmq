package com.example.demo.queue;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
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
		//factory.setAdviceChain(new Advice[] { statefulRetryOperationsInterceptor() } );
        //factory.setAdviceChain(new Advice[] { statelessRetryOperationsInterceptor() } );

        //factory.setDefaultRequeueRejected(false);
        factory.setPrefetchCount(50);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(2);
		return factory;
	}

    @Bean
    public RetryOperationsInterceptor statelessRetryOperationsInterceptor(){
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(5)
                .backOffOptions(1000,2.0,10000)
                .recoverer(new RejectAndDontRequeueRecoverer())
               // .retryOperations()
                .build();
    }


    @Bean
	public StatefulRetryOperationsInterceptor statefulRetryOperationsInterceptor(){
	   return RetryInterceptorBuilder.stateful()
              .messageKeyGenerator(new UniqueMessageKeyGenerator())
	          .maxAttempts(5)
	          .backOffOptions(1000,2.0,10000)
              .recoverer(new RejectAndDontRequeueRecoverer())
	          .build();
	}
}
