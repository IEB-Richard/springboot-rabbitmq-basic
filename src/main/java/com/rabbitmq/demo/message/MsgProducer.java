package com.rabbitmq.demo.message;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MsgProducer {
	private final static String QUEUE_NAME = "test001";

	public static void main(String[] args) throws Exception {

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
			
			// Step04: Declare the queue, which will create the queue, if you 
			//         create the queue in consumer class, this step could be 
			//		   ignored.
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			// Additional Step - Setup properties
			// headers contains the self-defined attributes
			Map<String, Object> headers = new HashMap<>();
			headers.put("my_attr1", "my_value1");
			headers.put("my_attr2", "my_value2");
			headers.put("my_attr3", "my_value3");
			
			// You can setup standard attributes with a chain calls
			AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
					.deliveryMode(2)
					.contentEncoding("UTF-8")
					.expiration("10000")
					.headers(headers)
					.build();

			// Step05: Send the data via channel
			for (int i = 0; i < 5; i++) {
				String msg = "Hello RabbitMQ! I am message " + i;
				channel.basicPublish("", QUEUE_NAME, properties, msg.getBytes("UTF-8"));
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
