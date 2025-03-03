package ru.gb.aspect.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

  private final LoggingProperties properties;

  @Pointcut("@annotation(ru.gb.aspect.logging.Logging)") // method
  public void loggingMethodsPointcut() {}

  @Pointcut("@within(ru.gb.aspect.logging.Logging)") // class
  public void loggingTypePointcut() {}

  @Around(value = "loggingMethodsPointcut() || loggingTypePointcut()")
  public Object loggingMethod(ProceedingJoinPoint pjp) throws Throwable {
    String methodName = pjp.getSignature().getName();


    Object[] args = pjp.getArgs();


    log.atLevel(properties.getLevel()).log(
            "Before -> TimesheetService#{} - Args: {}",
            methodName,
            properties.isPrintArgs() ? Arrays.toString(args) : "Args logging is disabled"
    );

    try {
      return pjp.proceed();
    } finally {

      log.atLevel(properties.getLevel()).log(
              "After -> TimesheetService#{} - Args: {}",
              methodName,
              properties.isPrintArgs() ? Arrays.toString(args) : "Args logging is disabled"
      );
    }
  }
}
