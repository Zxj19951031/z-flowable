package org.zipper.flowable.app.constant.errors;

import org.zipper.helper.exception.IErrorCode;

/**
 * 流程内容异常
 *
 * @author zhuxj
 * @since 2020/10/13
 */
public enum FlowableError implements IErrorCode {
    PROCESS_DEFINITION_NOT_EXIST(10001, "process definition not exist"),
    ;

    private final int code;
    private final String description;

    FlowableError(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.description;
    }

    public String toString() {
        return String.format("Code:[%s], Describe:[%s]", this.code, this.description);
    }
}