package org.zipper.flowable.app.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zipper.flowable.app.constant.FlowableVariableKey;
import org.zipper.flowable.app.service.MessageService;

import javax.annotation.Resource;

/**
 * @author zhuxj
 * @since 2020/10/14
 */
@Component
public class MessageDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDelegate.class);

    @Resource
    private MessageService messageService;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        String msgMode = (String) delegateExecution.getVariable(FlowableVariableKey.MESSAGE_MODE);
        if (FlowableVariableKey.MAIL.equals(msgMode)) {
            this.messageService.mail();
        } else if (FlowableVariableKey.SMS.equals(msgMode)) {
            this.messageService.sms();
        } else {
            LOGGER.warn("未知的消息发送方式，流程将跳过本节点");
        }
    }
}
