package org.zipper.flowable.app.service;

/**
 * @author zhuxj
 * @since 2020/10/14
 */
public interface MessageService {

    /**
     * 邮件消息通知
     */
    public void mail();

    /**
     * 短信消息通知
     */
    public void sms();
}
