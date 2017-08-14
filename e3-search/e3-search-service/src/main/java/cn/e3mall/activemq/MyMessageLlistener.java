package cn.e3mall.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageLlistener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage=(TextMessage) message;
		try {
			String text = textMessage.getText();
			System.out.println(text);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
