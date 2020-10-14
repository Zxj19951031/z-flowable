package org.zipper.flowable.app.constant.enums;

/**
 * 流程实例状态
 *
 * @author zhuxj
 * @since 2020/10/13
 */
public enum ProcessInstanceStatus {
    /**
     * 审批中
     */
    approving(1),
    /**
     * 审批通过
     */
    passed(2),
    /**
     * 审批不通过
     */
    rejected(3),
    /**
     * 撤销
     */
    canceled(4);

    private final int value;

    ProcessInstanceStatus(int value) {
        this.value = value;
    }

    public static ProcessInstanceStatus get(int value) {
        for (ProcessInstanceStatus instanceStatus : ProcessInstanceStatus.values()) {
            if (instanceStatus.value == value) {
                return instanceStatus;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
