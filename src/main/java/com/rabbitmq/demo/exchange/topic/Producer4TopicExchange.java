package com.rabbitmq.demo.exchange.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer4TopicExchange {
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		// step01: create a new connection factory and configure the connection
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.10.101");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");

		Connection connection = null;
		Channel channel = null;
		try {
			// Step 02: get Connection
			connection = connectionFactory.newConnection();
			
			// Step 03: get Channel
			channel = connection.createChannel();
			
			// Step 04: Declare exchange and routine key
			String exchangeName = "test_topic_exchange";
			String routingKey1 = "user.save";
			String routingKey2 = "user.update";
			String routingKey3 = "user.delete.abc";
			
			// Step 05: let's send the message
			String message = "Hello RabbitMQ for Direct Exchange Message...";
			channel.basicPublish(exchangeName, routingKey1, null, message.getBytes());
			channel.basicPublish(exchangeName, routingKey2, null, message.getBytes());
			channel.basicPublish(exchangeName, routingKey3, null, message.getBytes());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// please don't forget to close the connections
			channel.close();
			connection.close();
		}
		
	}
}
