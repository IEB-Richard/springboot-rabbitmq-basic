package com.rabbitmq.demo.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer4FanoutExchange {
	
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
			String exchangeName = "test_fanout_exchange";
			String exchangeType = "fanout";
			String queueName = "test_fanout_queue";
			String routingKey = "";		// fanout exchange don't need routingKey
			
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
