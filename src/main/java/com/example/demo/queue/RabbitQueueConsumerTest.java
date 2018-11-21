package com.example.demo.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.demo.message.TestMessage;

@Service
public class RabbitQueueConsumerTest {

	private static Logger LOG = LoggerFactory.getLogger(RabbitQueueConsumerTest.class);

	
    @RabbitListener(containerFactory = "myRabbitListenerContainerFactory", queues = RabbitQueuePublisherTest.QUEUE_NAME)
    public void receiveMessage(final TestMessage message) throws Exception {
    
    	LOG.info("Listener got: "+message);

    	//try {
    		throw new Exception("An example Exception");
//    	} catch (Exception e) {
//    		// Need to throw AmqpRejectAndDontRequeueException to get item to the dead letter queue
//    		throw new AmqpRejectAndDontRequeueException(e);
//    	}
    	
    }

    	
    	
}
