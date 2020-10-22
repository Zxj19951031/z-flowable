package org.zipper.flowable.app.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.zipper.helper.web.response.ResponseEntity;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出成功处理器
 */
@Component
public class SystemLogoutSuccessHandler implements LogoutSuccessHandler {
    @Resource
    private DaoUserDetailService daoUserDetailService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");

        daoUserDetailService.removeToken(authentication.getName());
        response.getWriter().write(ResponseEntity.success("登出成功").toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
