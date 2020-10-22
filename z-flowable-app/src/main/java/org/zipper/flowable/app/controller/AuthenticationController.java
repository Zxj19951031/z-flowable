package org.zipper.flowable.app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zipper.flowable.app.dto.SignUpParameter;
import org.zipper.flowable.app.service.AuthenticationService;
import org.zipper.helper.web.response.ResponseEntity;

import javax.annotation.Resource;

/**
 * 认证管理
 *
 * @author zhuxj
 * @since 2020/10/21
 */
@RestController
@RequestMapping(value = "auth")
public class AuthenticationController {

    @Resource
    private AuthenticationService authenticationService;

    /**
     * 注册新的用户
     *
     * @return 用户编号
     */
    @PostMapping(value = "signUp")
    public ResponseEntity<Integer> signUp(@RequestBody SignUpParameter parameter) {

        int userId = authenticationService.signUp(parameter.getUsername(), parameter.getPassword());
        return ResponseEntity.success(userId);
    }
}
