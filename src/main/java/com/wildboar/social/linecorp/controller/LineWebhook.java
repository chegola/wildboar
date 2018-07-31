package com.wildboar.social.linecorp.controller;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.wildboar.config.ApplicationProperties;


@LineMessageHandler
@Component
public class LineWebhook {

	   private final Logger log = LoggerFactory.getLogger(LineWebhook.class);
	   
	   @Autowired
	   private ApplicationProperties applicationProperties; 
	   

	   // @Async
	   public void sendMessage(String providerUserId, String text) {
		   final LineMessagingClient client = LineMessagingClient
			        .builder(applicationProperties.getLine().getLineBotChannelToken())
			        .build();
		   
		   final TextMessage textMessage = new TextMessage(text);
		   final PushMessage pushMessage = new PushMessage(providerUserId, textMessage);
		   final BotApiResponse botApiResponse;
		   try {
			   botApiResponse = client.pushMessage(pushMessage).get();
		   } catch (InterruptedException e) {
			   log.debug(e.getStackTrace().toString());
			   return;
		   } catch (ExecutionException e) {
			   e.printStackTrace();
			   return;
		   }
		   
		   log.debug(botApiResponse.getMessage());

	   }
}
