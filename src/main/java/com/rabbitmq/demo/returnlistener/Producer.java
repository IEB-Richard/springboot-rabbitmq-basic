package com.rabbitmq.demo.returnlistener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;

public class Producer {
	
	public static void main(String[] args) throws IOException, TimeoutException{
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.10.101");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		String exchangeName = "test_return_exchange";
		String routingKey = "return.save";
		String routingKeyError = "abc.save";
		String message = "Hello RabbitMQ - Return message";
		
		channel.addReturnListener(new ReturnListener() {

			@Override
			public void handleReturn(
					int replyCode,
		            String replyText,
		            String exchange,
		            String routingKey,
		            AMQP.BasicProperties properties,
		            byte[] body)
					throws IOException {
				
				System.out.println("--------handle return--------");
				System.out.println("replycode: " + replyCode );
				System.out.println("replyText: " + replyText );
				System.out.println("exchange: " + exchange );
				System.out.println("routingKey: " + routingKey );
				System.out.println("properties: " + properties );
				System.out.println("body: " + new String(body) );
				
			}

			
		});
		
		//channel.basicPublish(exchangeName, routingKey, true, null, message.getBytes());
		channel.basicPublish(exchangeName, routingKeyError, true, null, message.getBytes());
		
	}

}
