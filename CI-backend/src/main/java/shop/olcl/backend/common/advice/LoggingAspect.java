package shop.olcl.backend.common.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // @Loggable 어노테이션이 붙은 메서드 실행 전 로깅
    @Before("@within(shop.olcl.backend.common.annotation.Loggable) || @annotation(shop.olcl.backend.common.annotation.Loggable)")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entering method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    // @Loggable 어노테이션이 붙은 메서드 실행 후 로깅
    @AfterReturning(value = "@within(shop.olcl.backend.common.annotation.Loggable) || @annotation(shop.olcl.backend.common.annotation.Loggable)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Method {} returned with value: {}", joinPoint.getSignature(), result);
    }

    // @Loggable 어노테이션이 붙은 메서드에서 예외 발생 시 로깅
    @AfterThrowing(value = "@within(shop.olcl.backend.common.annotation.Loggable) || @annotation(shop.olcl.backend.common.annotation.Loggable)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Method {} threw exception: {}", joinPoint.getSignature(), exception.getMessage());
    }
}
