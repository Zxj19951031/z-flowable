package org.zipper.flowable.app.constant.errors;

/**
 * 流程内容异常
 *
 * @author zhuxj
 * @since 2020/10/13
 */
public enum FlowableError implements IErrorCode {
    PROCESS_DEFINITION_NOT_EXIST(10001, "流程定义不存在"),
    XML_FORMAT_ERROR(10002,"流程配置格式有误")
    ;

    private final int code;
    private final String description;

    FlowableError(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int getCode() {
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