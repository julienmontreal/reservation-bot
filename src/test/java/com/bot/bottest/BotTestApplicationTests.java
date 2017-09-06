package com.bot.bottest;

import com.bot.bottest.dto.request.Message;
import com.bot.bottest.dto.request.Messaging;
import com.bot.bottest.dto.request.Recipient;
import com.bot.bottest.dto.send.SendMessage;
import com.bot.bottest.dto.send.SendPayload;
import com.bot.bottest.dto.send.SendRecipient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BotTestApplicationTests {

	@Test
	public void contextLoads() {
	}

	@InjectMocks
	private BotController botController;

	@Test
	public void testSendAPI(){

		SendRecipient recipient =  new SendRecipient("1513101308755628");
		SendMessage message = new SendMessage();
		message.setText("text");
		SendPayload sendPayload = new SendPayload(recipient, message);

		botController.callSendAPI(sendPayload);
	}
	@Test
	public void testReceivedMessage(){
		Messaging event = new Messaging();
		event.setMessage(new Message("mid", "text", "attach"));
		event.setRecipient(new Recipient("123"));
		event.setSender(new Messaging.Sender("123"));
		event.setTimestamp(new Date());
		botController.receivedMessage(event);
	}

}
