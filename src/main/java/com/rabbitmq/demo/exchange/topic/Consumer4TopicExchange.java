package com.rabbitmq.demo.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer4TopicExchange {
	
	public static void main(String[] args) {
		// Step 01: Create ConnectionFactory Object
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.10.101");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		connectionFactory.setAutomaticRecoveryEnabled(true);
		connectionFactory.setNetworkRecoveryInterval(3000);
		
		Connection connection = null;
		Channel channel = null;
		try {
			// Step 02: get Connection
			connection = connectionFactory.newConnection();
			
			// Step 03: get Channel
			channel = connection.createChannel();
			
			// Step 04: Declare exchange and routine key
			String exchangeName = "test_topic_exchange";
			String exchangeType = "topic";
			String queueName = "test_topic_queue";
//			String routingKey = "user.#";
			String routingKey = "user.*";
			
			// Step 05: declare exchange & queue, bind queue to exchange with routingKey
			channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
			channel.queueDeclare(queueName, false, false, false, null);
			channel.queueBind(queueName, exchangeName, routingKey);
			
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "utf-8");
				System.out.println("[x] Received '" + message + "'");
			};
			
			// let the consumer consume the queueName now
			channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// don't close the connections, as we want the consumer
			// keep on listening to the queue
		}
	}
}
