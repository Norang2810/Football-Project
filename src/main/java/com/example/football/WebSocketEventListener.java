package com.example.football;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WebSocketEventListener {

    private final AtomicInteger activeUsers = new AtomicInteger(0); // 현재 사용자 수를 추적
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        int userCount = activeUsers.incrementAndGet(); // 연결 시 사용자 수 증가
        broadcastUserCount(userCount);
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        int userCount = activeUsers.decrementAndGet(); // 연결 해제 시 사용자 수 감소
        broadcastUserCount(userCount);
    }

    private void broadcastUserCount(int userCount) {
        // 현재 사용자 수를 /topic/user-count 경로로 브로드캐스트
        messagingTemplate.convertAndSend("/topic/user-count", userCount);
    }
}
