package com.example.demo.message;

import java.util.Date;

public class TestMessage {

	public Date messageTime;
	public String messageText;

	public TestMessage() {
	}
	
	public TestMessage(String messageText) {
		super();
		this.messageText = messageText;
		this.messageTime = new Date();
	}

	@Override
	public String toString() {
		return "TestMessage [messageTime=" + messageTime + ", messageText=" + messageText + "]";
	}
	
	
}
