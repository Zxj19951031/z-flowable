package org.zipper.flowable.app.mapper;

import org.zipper.flowable.app.entity.Member;

/**
 * @author zhuxj
 * @since 2020/10/22
 */
public interface AuthenticationMapper {
    /**
     * 新增一条用户记录
     *
     * @param member
     * @return
     */
    int insert(Member member);


    /**
     * 根据用户名等值查询用户
     *
     * @param username 用户名
     * @return member
     */
    Member selectByUsernameEqual(String username);

}
