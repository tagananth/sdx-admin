package org.servicedx.event.service;

import java.io.IOException;

import org.servicedx.security.resource.IPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AdminConsumer implements IPath
{

	private static final long	serialVersionUID	= -3892033320967613405L;
	private final Logger		logger				= LoggerFactory.getLogger(AdminConsumer.class);

	@KafkaListener(topics = MESSAGES_TOPIC, groupId = USER_ID)
	public void consume(String message) throws IOException
	{
		logger.info(String.format("#### -> Consumed message -> %s", message));
	}
}