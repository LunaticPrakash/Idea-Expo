package com.ideaexpobackend.idea_expo_backend.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@Aspect
@Component
public class MethodArgsAndReturnValuesWithExecutionTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger("com.ideaexpobackend");

    @Around("@annotation(com.ideaexpobackend.idea_expo_backend.annotations.LogMethodArgsAndReturnValuesWithExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.toShortString();
        String className = methodSignature.getDeclaringTypeName();
        String threadName = Thread.currentThread().getName();

        // Get method arguments and parameter annotations
        Object[] methodArgs = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();

        // Mask sensitive parameters
        Object[] filteredArgs = maskSensitiveData(methodArgs, parameterAnnotations);

        logger.info("Starting execution of {} in class {} on thread {} with arguments {}", methodName, className, threadName, Arrays.toString(filteredArgs));

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Exception in {}.{}: {}", className, methodName, throwable.getMessage());
            throw throwable;
        }

        long endTime = System.currentTimeMillis();
        logger.info("Completed execution of {} in {} ms with return value {}", methodName, endTime - startTime, result);

        return result;
    }

    private Object[] maskSensitiveData(Object[] methodArgs, Annotation[][] parameterAnnotations) {
        Object[] filteredArgs = new Object[methodArgs.length];

        for (int i = 0; i < methodArgs.length; i++) {
            boolean isSensitive = false;

            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof com.ideaexpobackend.idea_expo_backend.annotations.Sensitive) {
                    isSensitive = true;
                    break;
                }
            }

            if (isSensitive) {
                filteredArgs[i] = "**********"; // Mask sensitive data
            } else {
                filteredArgs[i] = methodArgs[i];
            }
        }

        return filteredArgs;
    }
}
