package com.rabbitmq.demo.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MsgProducer {
	private final static String QUEUE_NAME = "test001";

	public static void main(String[] args) throws Exception {

		// step01: create a new connection factory and configure the connection
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("127.0.0.1");
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
			
			// Step04: Declare the queue, which will create the queue, if you 
			//         create the queue in consumer class, this step could be 
			//		   ignored.
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			// Step05: Send the data via channel
			for (int i = 0; i < 5; i++) {
				String msg = "Hello RabbitMQ!";
				channel.basicPublish("", QUEUE_NAME, null, msg.getBytes("UTF-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Step06: Important! please close the connections
			channel.close();
			connection.close();
		}

	}
}
