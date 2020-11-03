package org.zipper.flowable.app.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.zipper.flowable.app.constant.SystemResponse;
import org.zipper.flowable.app.constant.errors.SystemError;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证开始入口
 *
 * @author zhuxj
 * @since 2020/10/21
 */
@Component
public class SystemAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        SystemError error = SystemError.NOT_LOGIN;
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(error.getCode());
        httpServletResponse.getWriter().write(SystemResponse.error(error).toString());
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }
}
