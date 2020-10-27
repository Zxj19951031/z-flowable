package org.zipper.flowable.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zipper.flowable.app.vo.ResourceNode;
import org.zipper.flowable.app.dto.parameter.SignUpParameter;
import org.zipper.flowable.app.entity.Member;
import org.zipper.flowable.app.security.AuthenticationUtil;
import org.zipper.flowable.app.service.AuthenticationService;
import org.zipper.flowable.app.service.ResourceService;
import org.zipper.helper.web.response.ResponseEntity;

import javax.annotation.Resource;
import java.util.List;

/**
 * 认证权限中心管理
 *
 * @author zhuxj
 * @since 2020/10/21
 */
@RestController
@RequestMapping(value = "authentication")
public class AuthenticationController {

    @Resource
    private AuthenticationService authenticationService;

    @Resource
    private ResourceService resourceService;

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

    /**
     * 获取用户资源权限
     *
     * @return 带有按钮内容的菜单树
     */
    @PostMapping(value = "/resource/get")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<List<ResourceNode>> getResource() {

        String username = AuthenticationUtil.getAuthentication().getName();
        Member member = authenticationService.getByUsernameEqual(username);
        List<ResourceNode> nodes = resourceService.getResourceNodeByUserId(member.getId());
        return ResponseEntity.success(nodes);
    }
}
