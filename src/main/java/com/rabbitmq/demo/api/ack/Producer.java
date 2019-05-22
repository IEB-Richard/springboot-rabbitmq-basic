package com.rabbitmq.demo.api.ack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
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
		
		String exchangeName = "test_ack_exchange";
		String routingKey = "ack.save";
		
		
		for(int i=0; i<5;i++) {
			
			Map<String, Object> headers = new HashMap<String, Object>();
			headers.put("num", i);
			
			AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
					.deliveryMode(2)
					.contentEncoding("UTF-8")
					.headers(headers)
					.build();
			
			String message = "Hello RabbitMQ - ack message " + i;
			channel.basicPublish(exchangeName, routingKey, true, properties, message.getBytes());
		}
	
	}

}
