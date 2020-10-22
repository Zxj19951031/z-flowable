package org.zipper.flowable.app.service;

import org.zipper.flowable.app.entity.Member;

/**
 * @author zhuxj
 * @since 2020/10/22
 */
public interface AuthenticationService {
    /**
     * 注册用户
     * @param username 用户名
     * @param password 密码
     * @return 用户id
     */
    int signUp(String username, String password);

    /**
     * 通过名称等值匹配获取有效用户记录
     * @param username
     * @return member
     */
    Member getByUsernameEqual(String username);
}
