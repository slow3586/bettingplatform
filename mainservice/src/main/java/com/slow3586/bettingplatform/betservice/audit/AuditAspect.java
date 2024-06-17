package com.slow3586.bettingplatform.betservice.audit;

import com.slow3586.bettingplatform.api.auditservice.TraceDto;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuditAspect {
    static String SERVICE_NAME = "main";
    @Lazy
    AuditService auditService;
    @Lazy
    Tracer tracer;

    @Around("within(com.slow3586.bettingplatform.betservice.*..*) " +
        "&& !within(is(EnumType)) && !within(is(FinalType))" +
        "&& !within(com.slow3586.bettingplatform.betservice.audit..*)) " +
        "&& execution(public * *(..))" +
        "&& @within(org.springframework.stereotype.Service)")
    public Object joinPoint(@NonNull ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final TraceDto.TraceDtoBuilder traceInfo = getTrace(proceedingJoinPoint);

        auditService.sendTraceAsync(traceInfo.eventId("enter"));

        final Object result = proceedingJoinPoint.proceed();

        auditService.sendTraceAsync(traceInfo.eventId("exit").methodResult(Objects.toString(result)));

        return result;
    }

    @AfterThrowing(pointcut =
        "within(com.slow3586.bettingplatform.betservice.*..*) " +
            "&& !within(is(EnumType)) && !within(is(FinalType))" +
            "&& !within(com.slow3586.bettingplatform.betservice.audit..*)) " +
            "&& execution(public * *(..))" +
            "&& @within(org.springframework.stereotype.Service)",
        throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Throwable exception) throws Throwable {
        final TraceDto.TraceDtoBuilder traceInfo = getTrace(joinPoint);

        auditService.sendTraceAsync(traceInfo
            .eventId("error")
            .exceptionClass(exception.getClass().getSimpleName())
            .exceptionMessage(exception.getMessage())
            .exceptionStack(
                StringUtils.substring(
                    Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining(", ")),
                    0, 8000)));
        throw exception;
    }

    protected TraceDto.TraceDtoBuilder getTrace(JoinPoint joinPoint) {
        final TraceContext context = tracer == null ? null : tracer.currentTraceContext().context();
        final String spanId = context == null ? null : context.spanId();
        final String traceId = context == null ? null : context.traceId();
        final String methodArgs = Arrays.stream(joinPoint.getArgs())
            .map(Object::toString)
            .collect(Collectors.joining(", "));
        final String methodName = joinPoint.getSignature().getName();
        final String methodClass = joinPoint.getSignature().getDeclaringTypeName();
        return TraceDto.builder()
            .serviceName(SERVICE_NAME)
            .spanId(spanId)
            .traceId(traceId)
            .methodClass(methodClass)
            .methodName(methodName)
            .methodArgs(methodArgs);
    }

}
