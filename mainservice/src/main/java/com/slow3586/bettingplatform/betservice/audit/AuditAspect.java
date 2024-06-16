package com.slow3586.bettingplatform.betservice.audit;

import com.slow3586.bettingplatform.api.TraceDto;
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
    static String TOPIC = "trace";
    static String SERVICE_NAME = "main";
    AuditAsyncService auditAsyncService;
    @Lazy
    Tracer tracer;

    @Pointcut("within(com.slow3586.bettingplatform..*) " +
        "&& !within(com.slow3586.bettingplatform.betservice.audit..*)")
    public void anyThisApp() {
    }

    @Pointcut("execution(public * *(..))")
    public void anyPublic() {
    }

    @Pointcut("!within(is(EnumType)) && !within(is(FinalType))")
    public void notFinal() {
    }

    @Pointcut("!@within(com.slow3586.bettingplatform.betservice.audit.AuditDisabled) && @within(org.springframework.stereotype.Service)")
    public void anyComponent() {
    }

    @Pointcut("anyPublic() && anyThisApp() && anyComponent()")
    public void fullTracing() {
    }

    @Around("within(com.slow3586.bettingplatform..*) " +
        "&& !within(com.slow3586.bettingplatform.betservice.audit..*))" +
        "&& !within(is(EnumType)) && !within(is(FinalType))" +
        "&& !within(com.slow3586.bettingplatform.betservice.audit..*))" +
        "&& !@within(com.slow3586.bettingplatform.betservice.audit.AuditDisabled)" +
        "&& @within(org.springframework.stereotype.Service)" +
        "&& @within(org.springframework.stereotype.Repository)" +
        "&& execution(public * *(..))")
    public Object joinPoint(@NonNull ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final TraceDto.TraceDtoBuilder traceInfo = getTrace(proceedingJoinPoint);

        auditAsyncService.send(traceInfo.eventId("enter"));

        final Object result = proceedingJoinPoint.proceed();

        auditAsyncService.send(traceInfo.eventId("exit").methodResult(Objects.toString(result)));

        return result;
    }

    @AfterThrowing(pointcut = "!within(com.slow3586.bettingplatform.betservice.audit..*))" +
        "&& !within(is(EnumType)) && !within(is(FinalType))" +
        "&& !within(com.slow3586.bettingplatform.betservice.audit..*))" +
        "&& !@within(com.slow3586.bettingplatform.betservice.audit.AuditDisabled)" +
        "&& execution(public * *(..))",
        throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Throwable exception) throws Throwable {
        final TraceDto.TraceDtoBuilder traceInfo = getTrace(joinPoint);

        auditAsyncService.send(traceInfo
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
