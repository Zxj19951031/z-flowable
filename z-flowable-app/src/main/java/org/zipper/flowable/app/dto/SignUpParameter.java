package org.zipper.flowable.app.dto;

/**
 * 注册参数
 *
 * @author zhuxj
 * @since 2020/10/22
 */
public class SignUpParameter {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
