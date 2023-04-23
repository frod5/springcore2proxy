package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass = {}",target.getClass());
        log.info("proxyClass = {}",proxy.getClass());

        proxy.save();

        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isFalse();

        //new ProxyFactory(target) : 프록시 팩토리를 생성할 때, 생성자에 프록시의 호출 대상을 함께
        //넘겨준다. 프록시 팩토리는 이 인스턴스 정보를 기반으로 프록시를 만들어낸다. 만약 이 인스턴스에
        //인터페이스가 있다면 JDK 동적 프록시를 기본으로 사용하고 인터페이스가 없고 구체 클래스만 있다면
        //CGLIB를 통해서 동적 프록시를 생성한다. 여기서는 target 이 new ServiceImpl() 의 인스턴스이기
        //때문에 ServiceInterface 인터페이스가 있다. 따라서 이 인터페이스를 기반으로 JDK 동적 프록시를
        //생성한다.
        //proxyFactory.addAdvice(new TimeAdvice()) : 프록시 팩토리를 통해서 만든 프록시가 사용할 부가
        //기능 로직을 설정한다. JDK 동적 프록시가 제공하는 InvocationHandler 와 CGLIB가 제공하는
        //MethodInterceptor 의 개념과 유사하다. 이렇게 프록시가 제공하는 부가 기능 로직을 어드바이스
        //( Advice )라 한다. 번역하면 조언을 해준다고 생각하면 된다.
        //proxyFactory.getProxy() : 프록시 객체를 생성하고 그 결과를 받는다.


        //AopUtils.isAopProxy(proxy) : 프록시 팩토리를 통해서 프록시가 생성되면 JDK 동적 프록시나,
        //CGLIB 모두 참이다.
        //AopUtils.isJdkDynamicProxy(proxy) : 프록시 팩토리를 통해서 프록시가 생성되고, JDK 동적
        //프록시인 경우 참
        //AopUtils.isCglibProxy(proxy) : 프록시 팩토리를 통해서 프록시가 생성되고, CGLIB 동적 프록시인
        //경우 경우 참
        //물론 proxy.getClass() 처럼 인스턴스의 클래스 정보를 직접 출력해서 확인할 수 있다.
    }
}
