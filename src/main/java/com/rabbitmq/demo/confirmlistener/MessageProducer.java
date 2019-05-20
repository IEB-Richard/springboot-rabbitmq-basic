package com.rabbitmq.demo.confirmlistener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MessageProducer {
	
	public static void main(String[] args) {
		
		// 01: Create connectionFactory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.10.101");
		connectionFactory.setPort(5672);
//		connectionFactory.setUsername("guest");
//		connectionFactory.setPassword("guest");
		connectionFactory.setVirtualHost("/");
		
		Connection connection = null;
		Channel channel = null;
		
		try {
			// 02: Create connection from connectionFactory
			connection = connectionFactory.newConnection();
			
			// 03: Create channel from connection
			channel = connection.createChannel();
			
			// 04: set the channel to run in confirmSelect() mode
			channel.confirmSelect();
			
			String exchangeName = "test_confirm_exchange";
			String routingKey = "confirm.save";
			
			// 05: Send the message
			String msg = "Hello RabbitMQ - Send confirm message!";
			channel.basicPublish(exchangeName, routingKey, null, msg.getBytes("UTF-8"));
			
			// 06: Add one confirmListener
			channel.addConfirmListener(new ConfirmListener() {

				@Override
				public void handleAck(long deliveryTag, boolean multiple) throws IOException {
					System.out.println("-------ack-------");
				}

				@Override
				public void handleNack(long deliveryTag, boolean multiple) throws IOException {
					System.out.println("--------no ack-------");
				}
				
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
}
