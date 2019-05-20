package com.rabbitmq.demo.exchange.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer4FanoutExchange {
	
	public static void main(String[] args) throws IOException, TimeoutException {
		// Step 01: Create ConnectionFactory Object
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
			
			// Step 04: Declare fanout exchange and routine key
			String exchangeName = "test_fanout_exchange";
			
			// Step 05: let's send the message, 
			// 			Note: fanout exchange type will ignore routingKey
			for(int i = 0; i < 10; i ++) {
				String message = "Hello RabbitMQ for Fanout Exchange Message " + i;
				channel.basicPublish(exchangeName, "routingKey was not needed", null, message.getBytes());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// please don't forget to close the connections
			channel.close();
			connection.close();
		}

	}

}
