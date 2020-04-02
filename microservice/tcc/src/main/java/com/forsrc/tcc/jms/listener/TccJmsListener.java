package com.forsrc.tcc.jms.listener;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

@Component
public class TccJmsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TccJmsListener.class);

    @Autowired
    @Qualifier("tccOAuth2RestTemplate")
    public OAuth2RestTemplate tccOAuth2RestTemplate;

    @JmsListener(destination = "jms/queues/tcc/confirm")
    public void onMessageConfirm(TextMessage message) throws JMSException {
        String text = message.getText();
        LOGGER.info("----> jms/queues/tcc/confirm: {}", message);
        LOGGER.info("  -->                       : {}", text);
    }

    @JmsListener(destination = "jms/queues/tcc/cancel")
    public void onMessageCancel(TextMessage message) throws JMSException {
        String text = message.getText();
        LOGGER.info("----> jms/queues/tcc/cancel: {}", message);
        LOGGER.info("  -->                      : {}", text);
    }
}
