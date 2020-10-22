package org.zipper.flowable.app.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 认证相关工具
 *
 * @author zhuxj
 * @since 2020/10/21
 */
public class AuthenticationUtil {

    /**
     * 获取前访问用户相关信息
     *
     * @return Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
