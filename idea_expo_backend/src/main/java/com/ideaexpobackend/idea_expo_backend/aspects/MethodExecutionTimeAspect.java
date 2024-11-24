package com.ideaexpobackend.idea_expo_backend.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodExecutionTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger("com.ideaexpobackend");

    @Around("@annotation(com.ideaexpobackend.idea_expo_backend.annotations.LogMethodExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.toShortString();
        String className = methodSignature.getDeclaringTypeName();
        String threadName = Thread.currentThread().getName();
        logger.info("Starting execution of {} in class {} on thread {}.", methodName, className, threadName);

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Exception in {}.{}: {}", className, methodName, throwable.getMessage());
            throw throwable;
        }

        long endTime = System.currentTimeMillis();
        logger.info("Completed execution of {} in {} ms.", methodName, endTime - startTime);

        return result;
    }
}
