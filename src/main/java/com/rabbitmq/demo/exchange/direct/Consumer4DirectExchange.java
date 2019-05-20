package com.rabbitmq.demo.exchange.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Consumer4DirectExchange {
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
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
			String exchangeName = "test_direct_exchange";
			String exchangeType = "direct";
			String queueName = "test_direct_queue";
			String routingKey = "test.direct";
			
			// Step 05: declare exchange & queue, bind queue to exchange with routingKey
			channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
			channel.queueDeclare(queueName, false, false, false, null);
			channel.queueBind(queueName, exchangeName, routingKey);
			
			// Step 06: declare the consumer, which contains the callback method handleDelivery
			//		    when the queue received the message, it will call method handleDelivery()
			Consumer consumer = new DefaultConsumer(channel){
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println("Customer Received: '" + message + "'");
				}
			};
			
			// let the consumer consume the queueName now
			channel.basicConsume(queueName, true, consumer);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// don't close the connections, as we want the consumer
			// keep on listening to the queue
		}
	}
}
