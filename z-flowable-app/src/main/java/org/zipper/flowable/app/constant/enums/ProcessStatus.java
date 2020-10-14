package org.zipper.flowable.app.constant.enums;

public enum ProcessStatus {
    /**
     *未发布
     */
    unpublished(1),
    /**
     * 已发布
     */
    published(2);

    private final int value;

    ProcessStatus(int value) {
        this.value = value;
    }

    public static ProcessStatus get(int value) {
        for (ProcessStatus processStatus : ProcessStatus.values()) {
            if (processStatus.value == value) {
                return processStatus;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
