package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();
//        Object result = method.invoke(target, args);  //args는 메소드 호출시 필요한 파라미터
        Object result = invocation.proceed();
        long endTime = System.currentTimeMillis();
        log.info("TimeProxy 종료 resultTime = {}",endTime - startTime);
        return result;
    }
}
