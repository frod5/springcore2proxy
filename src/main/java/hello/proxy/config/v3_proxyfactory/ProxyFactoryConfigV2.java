package hello.proxy.config.v3_proxyfactory;

import hello.proxy.app.v2.*;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProxyFactoryConfigV2 {

    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace trace) {
        OrderControllerV2 orderController = new OrderControllerV2(orderServiceV2(trace));
        ProxyFactory proxyFactory = new ProxyFactory(orderController);
        proxyFactory.addAdvisor(getAdvisor(trace));

        OrderControllerV2 proxy = (OrderControllerV2) proxyFactory.getProxy();
        log.info("ProxyFactory proxy= {}, target = {}",proxy.getClass(), orderController.getClass());

        return proxy;
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace trace) {
        OrderServiceV2 orderService = new OrderServiceV2(orderRepositoryV2(trace));
        ProxyFactory proxyFactory = new ProxyFactory(orderService);
        proxyFactory.addAdvisor(getAdvisor(trace));
        OrderServiceV2 proxy = (OrderServiceV2) proxyFactory.getProxy();
        log.info("ProxyFactory proxy= {}, target = {}",proxy.getClass(), orderService.getClass());

        return proxy;
    }

    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTrace trace) {
        OrderRepositoryV2 orderRepository = new OrderRepositoryV2();
        ProxyFactory proxyFactory = new ProxyFactory(orderRepository);
        proxyFactory.addAdvisor(getAdvisor(trace));

        OrderRepositoryV2 proxy = (OrderRepositoryV2) proxyFactory.getProxy();
        log.info("ProxyFactory proxy= {}, target = {}",proxy.getClass(), orderRepository.getClass());
        return proxy;
    }

    private Advisor getAdvisor(LogTrace trace) {
        //pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*","order*","save*");

        //advice
        LogTraceAdvice advice = new LogTraceAdvice(trace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
