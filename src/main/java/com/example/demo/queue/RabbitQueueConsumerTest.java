package com.example.demo.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import com.example.demo.message.TestMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RabbitQueueConsumerTest {

	private static Logger LOG = LoggerFactory.getLogger(RabbitQueueConsumerTest.class);

	long MAX_RETRY_COUNT = 3;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RabbitListener(containerFactory = "myRabbitListenerContainerFactory", queues = RabbitQueuePublisherTest.QUEUE_NAME)
    public void receiveMessage(@Header(required = false, name = "x-death") ArrayList<HashMap<String,Object>> xDeath, final TestMessage message) throws Exception {
    
    	LOG.info("Listener got: "+message);
    	try {
    		throw new Exception("An example Exception");
    	} catch (Exception e) {
            if (xDeath!=null) {
                LOG.info("Headers: "+ xDeath);
                long count = (Long)xDeath.get(0).get("count");
                LOG.info("x-death count: "+ count);
                LOG.info("Retries that have been performed: "+ count);
                if (count >= MAX_RETRY_COUNT) {
                    LOG.info("Maximum retry attempts to process message, it will be put into manual queue.  Message:"+message);
                    postToManualQueue(RabbitQueuePublisherTest.QUEUE_MANUAL_NAME, message);
                    return;
                }
            }
            // Need to throw AmqpRejectAndDontRequeueException to get item to the dead letter queue (retry queue)
    		throw new AmqpRejectAndDontRequeueException(e);
    	}

    }

    private void postToManualQueue(String queueName, final TestMessage message) {
        try {
            rabbitTemplate.convertAndSend(queueName, message);
        } catch (AmqpException amqpException) {
            LOG.error("Exception while sending message to Rabbit MQ", amqpException);
            throw amqpException;
        }
    }
    	
}
