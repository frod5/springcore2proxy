package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

/**
 * 프록시를 사용하는 디자인 패턴은, 프록시 패턴과 데코레이터 패턴이 있다.
 * 프록시 패턴은 접근제어를 목적으로하는 프록시를 사용하는 패턴이다.
 * 접근제어 - 권한에 따른 접근 차단, 캐싱 ,지연 로딩
 *
 * 프록시 패턴 예제
 */
@Slf4j
public class CacheProxy implements Subject {

    private  Subject target;
    private String cacheValue;

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");

        if(cacheValue == null) {
            cacheValue = target.operation();
        }

        return cacheValue;
    }
}
