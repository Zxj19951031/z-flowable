package org.zipper.flowable.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zipper.flowable.app.service.MessageService;

/**
 * @author zhuxj
 * @since 2020/10/14
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @Override
    public void mail() {
        LOGGER.debug("sending mail success");
    }

    @Override
    public void sms() {
        LOGGER.debug("sending sms success");
    }
}
