package com.example.football;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.messaging.MessageDeliveryException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

@Component
public class WebSocketEventListener {

    private static final int MAX_USERS = 100; // 최대 사용자 수 제한
    private final AtomicInteger activeUsers = new AtomicInteger(0);
    private final SimpMessagingTemplate messagingTemplate;
    private final ConcurrentHashMap<String, String> sessionUserMap = new ConcurrentHashMap<>();

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        
        try {
            if (sessionId != null && !sessionUserMap.containsKey(sessionId)) {
                int currentUsers = activeUsers.get();
                if (currentUsers >= MAX_USERS) {
                    // 최대 사용자 수 초과 시 연결 거부
                    throw new MessageDeliveryException("채팅방이 가득 찼습니다. 최대 인원: " + MAX_USERS);
                }
                
                sessionUserMap.put(sessionId, sessionId);
                int userCount = activeUsers.incrementAndGet();
                System.out.println("New user connected. Session ID: " + sessionId + ", Total users: " + userCount);
                
                // 연결 직후 현재 사용자 수를 브로드캐스트
                broadcastUserCount(userCount);
            }
        } catch (Exception e) {
            System.err.println("Error in handleSessionConnected: " + e.getMessage());
            if (sessionId != null) {
                sessionUserMap.remove(sessionId);
                activeUsers.decrementAndGet();
            }
            throw e;
        }
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        
        try {
            if (sessionId != null && sessionUserMap.remove(sessionId) != null) {
                int userCount = activeUsers.decrementAndGet();
                if (userCount < 0) {
                    activeUsers.set(0);
                    userCount = 0;
                }
                System.out.println("User disconnected. Session ID: " + sessionId + ", Total users: " + userCount);
                
                // 연결 해제 직후 현재 사용자 수를 브로드캐스트
                broadcastUserCount(userCount);
            }
        } catch (Exception e) {
            System.err.println("Error in handleSessionDisconnected: " + e.getMessage());
        }
    }

    private void broadcastUserCount(int userCount) {
        try {
            System.out.println("Broadcasting user count: " + userCount);
            messagingTemplate.convertAndSend("/topic/user-count", userCount);
        } catch (Exception e) {
            System.err.println("Error broadcasting user count: " + e.getMessage());
        }
    }
    
    // 현재 사용자 수를 반환하는 메서드 추가
    public int getCurrentUserCount() {
        return activeUsers.get();
    }
}
