package com.example.demo.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.message.TestMessage;

@Component
public class RabbitQueuePublisherTest {

	private static Logger LOG = LoggerFactory.getLogger(RabbitQueuePublisherTest.class);

	public static final String QUEUE_NAME = "testQueue";

	@Autowired
	private RabbitTemplate rabbitTemplate;

	// new method for inserting to Rabbit MQ
	public void postToRabbitMQ(TestMessage message) throws AmqpException {

		try {
			rabbitTemplate.convertAndSend(QUEUE_NAME, message);
		} catch (AmqpException amqpException) {
			LOG.error("Exception while sending message to Rabbit MQ", amqpException);
			throw amqpException;
		}
	}

}
