package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();
        //Enhancer : CGLIB는 Enhancer 를 사용해서 프록시를 생성한다.
        Enhancer enhancer = new Enhancer();

        //enhancer.setSuperclass(ConcreteService.class) : CGLIB는 구체 클래스를 상속 받아서 프록시를 생성할 수 있다. 어떤 구체 클래스를 상속 받을지 지정한다.
        enhancer.setSuperclass(ConcreteService.class);

        //enhancer.setCallback(new TimeMethodInterceptor(target)) 프록시에 적용할 실행 로직을 할당한다.
        enhancer.setCallback(new TimeMethodInterceptor(target));

        //enhancer.create() : 프록시를 생성한다. 앞서 설정한 enhancer.setSuperclass(ConcreteService.class) 에서 지정한 클래스를 상속 받아서 프록시가 만들어진다.
        ConcreteService proxy = (ConcreteService) enhancer.create();
        log.info("targetClass = {}",target.getClass());
        log.info("proxyClass = {}",proxy.getClass());

        proxy.call();

        //JDK 동적 프록시는 인터페이스를 구현(implement)해서 프록시를 만든다. CGLIB는 구체 클래스를 상속 (extends)해서 프록시를 만든다.
    }
}
