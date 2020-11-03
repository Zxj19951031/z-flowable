package org.zipper.flowable.app.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.zipper.flowable.app.constant.SystemResponse;
import org.zipper.flowable.app.constant.SystemException;
import org.zipper.flowable.app.constant.errors.SystemError;

/**
 * @author zhuxj
 * @since 2020/10/22
 */
public class ControllerAop {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAop.class);

    public ControllerAop() {
    }

    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();

        SystemResponse<?> result;
        try {
            result = (SystemResponse<?>) pjp.proceed();
            logger.info(pjp.getSignature() + " 耗时 : " + (System.currentTimeMillis() - startTime) + " ms");
        } catch (Throwable var6) {
            result = this.handlerException(pjp, var6);
        }

        return result;
    }

    private SystemResponse<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        SystemResponse<?> result;
        if (e instanceof SystemException) {
            result = SystemResponse.error((SystemException) e);
        } else {
            logger.error(pjp.getSignature() + " 方法异常 ", e);
            result = SystemResponse.error(SystemException.newException(SystemError.SERVER_ERROR, "非预期异常，请联系管理员"));
        }

        return result;
    }
}
