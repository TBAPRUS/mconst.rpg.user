package mconst.rpg.user.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class Log4MethodAspect {
    @Pointcut("@annotation(mconst.rpg.user.annotations.Log4Method)")
    public void callAtLog4MethodAnnotation() {}

    @Before("callAtLog4MethodAnnotation()")
    public void beforeCallAt(JoinPoint jp) {
        log.info("before: {}", jp.toString());
    }
}
