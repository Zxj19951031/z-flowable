package org.zipper.flowable.app.security.form;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.zipper.flowable.app.constant.SystemResponse;
import org.zipper.flowable.app.security.DaoUserDetailService;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证成功处理器,集成JWT认证，所以在登录成功后构建一个token给到客户端
 */
@Component
public class FormAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Resource
    private DaoUserDetailService daoUserDetailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //登录成功后返回token
        String token = daoUserDetailService.saveToken((UserDetails) authentication.getPrincipal());
        response.setContentType("application/json;charset=utf-8");
        response.setHeader(AUTHORIZATION_HEADER, token);
        response.getWriter().write(SystemResponse.success(token).toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
