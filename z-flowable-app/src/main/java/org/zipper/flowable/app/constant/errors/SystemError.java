package org.zipper.flowable.app.constant.errors;

import org.zipper.helper.exception.IErrorCode;

/**
 * 系统内容异常
 *
 * @author zhuxj
 * @since 2020/10/13
 */
public enum SystemError implements IErrorCode {
    NOT_LOGIN(401,"用户未登录"),
    AUTHENTICATE_ERROR(401, "认证失败"),
    USERNAME_PASSWORD_ERROR(401,"用户名或密码错误"),
    PARAMETER_ERROR(400,"缺失参数"),
    UNKNOWN_ERROR(500, "未知异常");

    private final int code;
    private final String description;

    SystemError(int code, String description) {
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