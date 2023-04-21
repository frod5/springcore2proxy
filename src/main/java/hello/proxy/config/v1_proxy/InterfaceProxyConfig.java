package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceProxyConfig {
    @Bean
    public OrderControllerV1 orderController(LogTrace trace) {
        OrderControllerV1Impl controllerImpl = new OrderControllerV1Impl(orderService(trace));
        return new OrderControllerInterfaceProxy(controllerImpl,trace);
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace trace) {
        OrderServiceV1Impl orderServiceImpl = new OrderServiceV1Impl(orderRepository(trace));
        return new OrderServiceInterfaceProxy(orderServiceImpl,trace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace trace) {
        OrderRepositoryV1Impl repositoryImpl = new OrderRepositoryV1Impl();
        return new OrderRepositoryInterfaceProxy(repositoryImpl,trace);
    }
}
