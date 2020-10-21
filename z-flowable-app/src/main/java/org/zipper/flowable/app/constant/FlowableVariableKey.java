package org.zipper.flowable.app.constant;

/**
 * 用于flowable的流程变量关键字
 *
 * @author zhuxj
 * @since 2020/10/13
 */
public final class FlowableVariableKey {

    /**
     * 发起人
     */
    public static final String INITIATOR = "initiator";
    /**
     * 消息代理发送方式
     */
    public static final String MESSAGE_MODE = "messageMode";
    /**
     * 消息代理发送方式——邮件发送
     */
    public static final String MAIL = "mail";
    /**
     * 消息代理发送方式——短信发送
     */
    public static final String SMS = "sms";
    /**
     * 审批结果
     */
    public static final String APPROVE_RESULT = "approveResult";
    /**
     * 审批结果——通过
     */
    public static final String PASS = "pass";
    /**
     * 审批结果——拒绝
     */
    public static final String REJECT = "reject";
    /**
     * 审批结果——驳回至
     */
    public static final String redirect = "redirect";

}
