package com.rabbitmq.demo.confirmlistener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class MessageConsumer {
	
	public static void main(String[] args) {
		
		// 01: Create connection factory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.10.101");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		connectionFactory.setVirtualHost("/");
		
		Connection connection = null;
		Channel channel = null;
			
		try {
			// 02: create connection through connectionFactory
			connection = connectionFactory.newConnection();
			
		    // 03: create a channel from the connection
			channel = connection.createChannel();
			
			String exchangeName = "test_confirm_exchange";
			String routingKey = "confirm.save";
			String queueName = "test_confirm_queue";
			
			channel.exchangeDeclare(exchangeName, "topic", true);
			channel.queueDeclare(queueName, true, false, false, null);
			channel.queueBind(queueName, exchangeName, routingKey);
			
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			};
			
			channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
			
//			Consumer consumer = new DefaultConsumer(channel) {
//
//				@Override
//				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
//						byte[] body) throws IOException {
//
//					String message = new String(body, "UTF-8");
//					System.out.println(" [x] Received '" + message + "'");
//				}
//				
//			};
//			
//			channel.basicConsume(queueName, true, consumer);
			
			
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
