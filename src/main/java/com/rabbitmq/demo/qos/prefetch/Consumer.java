package com.rabbitmq.demo.qos.prefetch;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {
	public static void main(String[] args) throws IOException, TimeoutException {
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.10.101");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		String exchangeName = "test_qos_exchange";
		String routingKey = "qos.#";
		String queueName = "test_qos_queue";
		
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		// accept one line of message per time
		channel.basicQos(0, 1, false);
		
		// important: autoAck must be set to false
		channel.basicConsume(queueName, false, new MyConsumer(channel));
		
	}
}
