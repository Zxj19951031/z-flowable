package org.zipper.flowable.app.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zipper.flowable.app.constant.errors.SystemError;
import org.zipper.helper.exception.HelperException;
import org.zipper.helper.web.response.ResponseEntity;

public class ControllerAop {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAop.class);

    public ControllerAop() {
    }

    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();

        ResponseEntity<?> result;
        try {
            result = (ResponseEntity<?>) pjp.proceed();
            logger.info(pjp.getSignature() + " 耗时 : " + (System.currentTimeMillis() - startTime) + " ms");
        } catch (Throwable var6) {
            result = this.handlerException(pjp, var6);
        }

        return result;
    }

    private ResponseEntity<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ResponseEntity<?> result;
        if (e instanceof HelperException) {
            result = ResponseEntity.error((HelperException) e);
        } else {
            logger.error(pjp.getSignature() + " 方法异常 ", e);
            result = ResponseEntity.error(HelperException.newException(SystemError.UNKNOWN_ERROR, "非预期异常，请联系管理员"));
        }

        return result;
    }
}
