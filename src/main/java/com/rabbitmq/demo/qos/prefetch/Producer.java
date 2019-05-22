package com.rabbitmq.demo.qos.prefetch;

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
		
		String exchangeName = "test_qos_exchange";
		String routingKey = "qos.save";
		String message = "Hello RabbitMQ - QoS message";
		
		for(int i=0; i<5;i++) {
			channel.basicPublish(exchangeName, routingKey, true, null, message.getBytes());
		}
	
	}

}
