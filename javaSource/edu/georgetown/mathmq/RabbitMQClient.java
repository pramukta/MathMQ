package edu.georgetown.mathmq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import com.rabbitmq.client.*;


public class RabbitMQClient {
	Connection connection;
	Channel channel;
	String exchangeName, queueName;
	boolean connected;
	String lastMessage;
	
	public RabbitMQClient(String host, int port, String user, String password, String vhost) {
		ConnectionFactory cFactory = new ConnectionFactory();
		cFactory.setUsername(user);
		cFactory.setPassword(password);
		cFactory.setHost(host);
		cFactory.setPort(port);

		try {
			connection = cFactory.newConnection();
			connected = true;
			channel = connection.createChannel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connected = false;
	}

	public void connect(String exchangeName, String exchangeType, String queueName) {
		if(!connected) {
			try {
				lastMessage = exchangeName + " " + exchangeType + " " + queueName;
				// channel.exchangeDeclare(exchangeName, exchangeType, false, true, false, null);
				channel.queueDeclare(queueName, true, false, false, null);
				channel.queueBind(queueName, exchangeName, queueName); // I think everyone but java might make the queue name and its routing key the same
				this.exchangeName = exchangeName;
				this.queueName = queueName;
				connected = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean isConnected() {
		return(this.connected);
	}
	
	public String getLastMessage() {
		return(this.lastMessage);
	}
	public void close() {
		try {
			this.connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(String text) {
		if(connected) {
			try {
				channel.basicPublish(this.exchangeName, this.queueName, null, text.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String receive() {
		if(connected) {
			boolean autoAck = true;
	
			GetResponse response;
			try {
				response = channel.basicGet(this.queueName, autoAck);
				if (response == null) {
					return("");
				} else {
				    AMQP.BasicProperties props = response.getProps();
				    return(new String( response.getBody() ));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				lastMessage = "shit.";
				e.printStackTrace();
			}			
		}
		return("");
	}
	
	
}



