package org.tmarciniak.cfmtp.listener;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.annotation.JmsListener;
import org.tmarciniak.cfmtp.config.ApplicationConfig;
import org.tmarciniak.cfmtp.model.TradeMessage;
import org.tmarciniak.cfmtp.processor.TradeMessageProcessor;

@Named
public class TradeMessageListener {

	private static Log logger = LogFactory.getLog(TradeMessageListener.class);

	@Inject
	ApplicationConfig applicationConfig;

	@Inject
	TradeMessageProcessor tradeMessageProcessor;

	@JmsListener(destination = ApplicationConfig.CFMTP_QUEUE_NAME, containerFactory = ApplicationConfig.CFMTP_JMS_LISTENER_CONTAINER_FACTORY)
	public void processTradeMessage(String tradeMessage) {
		try {
			logger.trace("Listener received: " + tradeMessage);
			tradeMessageProcessor.process(applicationConfig.objectMapper()
					.readValue(tradeMessage, TradeMessage.class));
		} catch (IOException e) {
			logger.error(e);
		}
	}

}
