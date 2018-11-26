package com.example.demo.queue;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageKeyGenerator;

import java.util.UUID;

public class UniqueMessageKeyGenerator implements MessageKeyGenerator
{

    @Override
    public Object getKey(Message message) {
        // return UUID.randomUUID();
        //return message.getBody().hashCode();
        //return message.hashCode();
        return 5;
    }
}
