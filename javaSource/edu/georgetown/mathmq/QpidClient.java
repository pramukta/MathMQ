package edu.georgetown.mathmq;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.io.IOException;
import java.util.Properties;

public class QpidClient {
	private Context context;
	private Connection connection;
	private Session session;
	
	private MessageProducer outbox;
	private MessageConsumer inbox;

	boolean active = false;
	
	public QpidClient(String filename) {
		Properties p = new Properties();
		try {
			p.load(this.getClass().getResourceAsStream(filename));			
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		new QpidClient(p);
	}
	
	public QpidClient(Properties p) {
		try {
			context = new InitialContext(p);
			ConnectionFactory cFactory = (ConnectionFactory) context.lookup("MathMQConnectionString");
			connection = cFactory.createConnection();
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}
	
	public void open(String endpoint) {
		if(!active) {
			try {
				connection.start();
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Destination destination = (Destination) context.lookup(endpoint);
			
				inbox = session.createConsumer(destination);
				outbox = session.createProducer(destination);
				active = true;
			} catch (JMSException jmse) {
				jmse.printStackTrace();
			} catch (NamingException ne) {
				ne.printStackTrace();
			}
		}
	}

	public void close() {
		if(active) {
			try {
				this.getConnection().close();
				this.getContext().close();
				active = false;
			} catch (JMSException jmse) {
				jmse.printStackTrace();
			} catch (NamingException ne) {
				ne.printStackTrace();
			}	
		}
	}
	
	public void send(String text) {
		try {
			TextMessage message = this.getSession().createTextMessage(text);
			this.getOutbox().send(message);			
		} catch(JMSException jmse) {
			jmse.printStackTrace();
		}
	}
	
	public String receive(long timeout) {
		try {
			TextMessage message = (TextMessage) inbox.receive(timeout);
			return(message.getText());
		} catch(JMSException jmse) {
			jmse.printStackTrace();
			return("");
		}
	}
	
	// Getters (and Setters)
	
	public Connection getConnection() {
		return(this.connection);
	}
	
	public Context getContext() {
		return(this.context);
	}	

	public MessageConsumer getInbox() {
		return(this.inbox);
	}
	
	public MessageProducer getOutbox() {
		return(this.outbox);
	}	

	public Session getSession() {
		return(this.session);
	}
	
	public boolean isActive() {
		return(active);
	}
}
