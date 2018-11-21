package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.message.TestMessage;
import com.example.demo.queue.RabbitQueueConsumerTest;
import com.example.demo.queue.RabbitQueuePublisherTest;

@RestController
public class TestController {

	private static Logger LOG = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	RabbitQueuePublisherTest rabbitQueueTest;
	
	@RequestMapping("/queue")
	public String greeting(@RequestParam(value = "messageText", defaultValue = "sample text") String messageText) {

		TestMessage message = new TestMessage(messageText);
		
		// TODO: Queue a message
		LOG.info("Message to queue: " + message);
		
		rabbitQueueTest.postToRabbitMQ(message);

		return "ok";
	}

}
