package com.example.football;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 메시지를 브로커에 전달
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트가 메시지 전송 시 사용할 prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket")
               .setAllowedOriginPatterns("*")  // 모든 오리진 허용
               .addInterceptors(new HttpSessionHandshakeInterceptor())
               .withSockJS()
               .setWebSocketEnabled(true)
               .setHeartbeatTime(25000);  // 하트비트 시간 설정
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(128 * 1024)    // 메시지 크기 제한: 128KB
                   .setSendBufferSizeLimit(512 * 1024)  // 버퍼 크기: 512KB
                   .setSendTimeLimit(20000);            // 전송 시간 제한: 20초
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                try {
                    return message;
                } catch (Exception e) {
                    System.err.println("Error in WebSocket connection: " + e.getMessage());
                    throw e;
                }
            }
        });
    }
}
