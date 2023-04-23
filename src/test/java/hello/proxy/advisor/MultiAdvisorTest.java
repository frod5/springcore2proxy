package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

@Slf4j
public class MultiAdvisorTest {

    @Test
    @DisplayName("여러 프록시")
    void multiAddvisorTest1() {
        //client -> proxt2(advisor2) -> proxy1(advisor1) -> target

        //proxy1 생성
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory1 = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE ,new Advice1());
        proxyFactory1.addAdvisor(advisor1);

        ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();

        //proxy2 생성 target -> proxy1 입력
        ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE ,new Advice2());
        proxyFactory2.addAdvisor(advisor2);

        ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();


        //실행
        proxy2.save();
    }

    @Test
    @DisplayName("하나의 프록시, 여러 어드바이저")
    void multiAddvisorTest2() {
        //client -> proxy -> advisor2 -> advisor1 -> target
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE ,new Advice1());
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE ,new Advice2());


        //proxy1 생성
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory1 = new ProxyFactory(target);
        proxyFactory1.addAdvisor(advisor2);
        proxyFactory1.addAdvisor(advisor1);

        ServiceInterface proxy = (ServiceInterface) proxyFactory1.getProxy();

        //실행
        proxy.save();

        //중요
        //> 사실 이번 장을 이렇게 풀어서 설명한 이유가 있다. 스프링의 AOP를 처음 공부하거나 사용하면, AOP 적용
        //수 만큼 프록시가 생성된다고 착각하게 된다. 실제 많은 실무 개발자들도 이렇게 생각하는 것을 보았다.
        //> 스프링은 AOP를 적용할 때, 최적화를 진행해서 지금처럼 프록시는 하나만 만들고, 하나의 프록시에 여러
        //어드바이저를 적용한다.
        //> 정리하면 하나의 target 에 여러 AOP가 동시에 적용되어도, 스프링의 AOP는 target 마다 하나의
        //프록시만 생성한다. 이부분을 꼭 기억해두자.
    }

    static class Advice1 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();
        }
    }

    static class Advice2 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();
        }
    }
}
