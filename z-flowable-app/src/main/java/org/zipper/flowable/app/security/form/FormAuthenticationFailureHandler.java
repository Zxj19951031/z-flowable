package org.zipper.flowable.app.security.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.zipper.flowable.app.constant.errors.SystemError;
import org.zipper.helper.web.response.ResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 */
@Component
public class FormAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(FormAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        SystemError error;
        logger.error("登录失败", exception);
        if(exception instanceof BadCredentialsException){
            error = SystemError.USERNAME_PASSWORD_ERROR;
        }else {
            error = SystemError.AUTHENTICATE_ERROR;
        }
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(error.getCode());
        response.getWriter().write(ResponseEntity.error(error).toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
