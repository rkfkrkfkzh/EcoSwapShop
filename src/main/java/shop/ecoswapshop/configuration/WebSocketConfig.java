package shop.ecoswapshop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //WebSocket 서버를 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    //WebSocket 메시지 브로커 관련 설정을 정의

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) { // 메시지 브로커 옵션을 구성
        config.enableSimpleBroker("/topic"); // 구독
        config.setApplicationDestinationPrefixes("/app"); // @MessageMapping으로 주석이 달린 메서드로 라우팅
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //STOMP 엔드포인트를 등록
        registry.addEndpoint("/ws").withSockJS(); // ws 엔드포인트를 WebSocket 연결의 엔드포인트로 설정
        // SockJS를 사용하여 WebSocket을 지원하지 않는 브라우저에서도 폴백 옵션을 제공
    }
}
