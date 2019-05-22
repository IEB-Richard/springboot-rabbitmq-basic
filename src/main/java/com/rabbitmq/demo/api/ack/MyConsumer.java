package com.rabbitmq.demo.api.ack;

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
		System.out.println("[x]Received '" + new String(body) + "'");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if ((Integer)properties.getHeaders().get("num") == 0) {
			channel.basicNack(envelope.getDeliveryTag(), false, true);
		} else {
			
			channel.basicAck(envelope.getDeliveryTag(), false);
		}
		
	}

}
