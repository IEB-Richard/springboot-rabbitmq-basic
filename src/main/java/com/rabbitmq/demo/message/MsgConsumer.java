package com.rabbitmq.demo.message;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class MsgConsumer {

	private final static String QUEUE_NAME = "test001";

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
			// Step02: Create connection with connection factory
			connection = connectionFactory.newConnection();

			// Step03: create a channel with connection
			channel = connection.createChannel();

			// Step04: declare(create) one queue, the queue we are listening to
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			// Step05: Create a consumer - where there is a message arrived,
			// the handleDelivery method will be called
			System.out.println("We consumer are waiting your messages");
			Consumer consumer = new DefaultConsumer(channel){
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println("[x] - Message Received '" + message + "'");
					Map<String, Object> headers = properties.getHeaders();
					System.out.println("    with header attribute1: " + headers.get("my_attr1"));
				}
			};
			
			// Step06: let the consumer consume the queue
			channel.basicConsume(QUEUE_NAME, true, consumer);
			
		} catch (Exception e) {
			System.out.println("Opps, there is something wrong!");
			e.printStackTrace();
		} finally {
//			System.out.println("Connection was closed!");
//			channel.close();
//			connection.close();
		}
	}
}
