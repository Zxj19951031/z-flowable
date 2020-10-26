package org.zipper.flowable.app.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.zipper.flowable.app.constant.errors.SystemError;
import org.zipper.helper.web.response.ResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhuxj
 * @since 2020/10/23
 */
@Component
public class SystemAccessDeniedHandler implements AccessDeniedHandler {


    private static final Logger logger = LoggerFactory.getLogger(SystemAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.error("无权访问", accessDeniedException);
        SystemError error = SystemError.AUTHORIZE_ERROR;
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(error.getCode());
        response.getWriter().write(ResponseEntity.error(error).toString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
