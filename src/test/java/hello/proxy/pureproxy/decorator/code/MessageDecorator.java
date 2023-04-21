package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

/**
 * 프록시를 사용하는 디자인 패턴은, 프록시 패턴과 데코레이터 패턴이 있다.
 * 데코레이터 패턴은 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행한다.
 * 예) 요청 값이나, 응답 값을 중간에 변형한다.
 * 예) 실행 시간을 측정해서 추가 로그를 남긴다.
 *
 * 데코레이터 패턴 예제
 */
@Slf4j
public class MessageDecorator implements Component {

    private RealComponent component;

    public MessageDecorator(RealComponent component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("Message Decorator 실행");

        String operation = component.operation();
        String decoResult = "*****" + operation + "*****";
        log.info("MessageDecorator 꾸미기 적용 전 = {}, 적용 후 = {}",operation,decoResult);
        return decoResult;
    }
}
