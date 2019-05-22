package com.rabbitmq.demo.qos.prefetch;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class MyConsumer extends DefaultConsumer {
	
	private Channel channel;
	
	// get the channel from the constructor
	public MyConsumer(Channel channel) {
		super(channel);
		this.channel = channel;
	}

	@Override
	public void handleDelivery(
			String consumerTag, 
			Envelope envelope, 
			BasicProperties properties, 
			byte[] body)
			throws IOException {
		
		System.out.println("--------------------Consume QoS Message--------------------");
		System.out.println("Received '" + new String(body) + "'");
		
		// acknowledgement - please try to comment this line to see what will happen?
		channel.basicAck(envelope.getDeliveryTag(), false);
		
	}

}
