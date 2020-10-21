package org.zipper.flowable.app.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zipper.flowable.app.service.MessageService;

import javax.annotation.Resource;

/**
 * 邮件通知委派
 *
 * @author zhuxj
 * @since 2020/10/14
 */
@Component
public class MailDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailDelegate.class);

    @Resource
    private MessageService messageService;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        this.messageService.mail();
    }
}
