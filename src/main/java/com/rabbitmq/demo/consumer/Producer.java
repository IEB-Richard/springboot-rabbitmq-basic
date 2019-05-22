package com.rabbitmq.demo.consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
	
	public static void main(String[] args) throws IOException, TimeoutException{
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.10.101");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		String exchangeName = "test_consumer_exchange";
		String routingKey = "consumer.save";
		String message = "Hello RabbitMQ - Consumer message";
		
		for(int i=0; i<5;i++) {
			channel.basicPublish(exchangeName, routingKey, true, null, message.getBytes());
		}
	
	}

}
