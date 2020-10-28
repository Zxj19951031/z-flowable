package org.zipper.flowable.app.constant.enums;

/**
 * 谁可以发起流程（类型）
 *
 * @author zhuxj
 * @since 2020/10/28
 */
public enum AllowInitiatorType {
    /**
     * 任何人
     */
    ANYBODY,
    /**
     * 按角色
     */
    ROLE,
    /**
     * 按部门
     */
    DEPT,
    /**
     * 指定人员
     */
    MEMBER;
}
