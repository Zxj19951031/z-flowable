package org.zipper.flowable.app.constant.enums;

/**
 * 流程实例阶段枚举
 *
 * @author zhuxj
 * @since 2020/10/28
 */
public enum InstanceStage {
    /**
     * 开始-0
     * <p>
     * 这个状态会在实例创建后第一时间被更改为审批中或已完成
     * 这是一个很短暂的状态，用于给定记录一个初始值
     */
    START,
    /**
     * 草稿-1
     */
    DRAFT,
    /**
     * 审批中-2
     */
    APPROVE,
    /**
     * 已完成-3
     */
    FINISH,
    /**
     * 已撤回-4
     */
    REVOKE;

}
