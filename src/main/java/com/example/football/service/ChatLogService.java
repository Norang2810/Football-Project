package com.example.football.service;

import com.example.football.entity.ChatLog;
import com.example.football.entity.User;
import com.example.football.repository.ChatLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatLogService {

    @Autowired
    private ChatLogRepository chatLogRepository;

    public ChatLog saveChatLog(Long matchId, User user, String content) {
        ChatLog chatLog = new ChatLog();
        chatLog.setMatchId(matchId);
        chatLog.setUser(user); // User 객체 설정
        chatLog.setContent(content);
        if (user.getBadge() != null) {
            chatLog.setBadge(user.getBadge().getName()); // Badge 이름 설정
        }
        return chatLogRepository.save(chatLog);
    }

    public List<ChatLog> getChatLogs(Long matchId) {
        return chatLogRepository.findByMatchIdOrderByTimestampAsc(matchId);
    }
}
