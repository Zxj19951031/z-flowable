package org.zipper.flowable.app.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zipper.flowable.app.constant.SystemException;
import org.zipper.flowable.app.constant.errors.SystemError;
import org.zipper.flowable.app.entity.Member;
import org.zipper.flowable.app.mapper.AuthenticationMapper;
import org.zipper.flowable.app.service.AuthenticationService;

import javax.annotation.Resource;

/**
 * @author zhuxj
 * @since 2020/10/22
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Resource
    private AuthenticationMapper authenticationMapper;

    @Override
    public int signUp(String username, String password) {
        if (username == null || username.equals("")) {
            throw SystemException.newException(SystemError.PARAMETER_ERROR, "用户名不可为空，请填写正确的用户名");
        }
        if (checkUsernameExist(username)) {
            throw SystemException.newException(SystemError.PARAMETER_ERROR, "用户名已存在，请重新填写");
        }
        if (password == null || password.equals("")) {
            throw SystemException.newException(SystemError.PARAMETER_ERROR, "密码不可为空，请填写正确的密码");
        }


        String encryptedPwd = new BCryptPasswordEncoder().encode(password);
        Member member = new Member(username, encryptedPwd);
        this.authenticationMapper.insert(member);

        return member.getId();
    }

    @Override
    public Member getByUsernameEqual(String username) {
        return this.authenticationMapper.selectByUsernameEqual(username);
    }


    @Override
    public boolean checkUsernameExist(String username) {
        return this.authenticationMapper.selectByUsernameEqual(username) != null;
    }
}
