package com.example.football.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.football.repository.ChatLogRepository;

import java.util.List;

@Service
public class ChatLogCleanupService {

    @Autowired
    private ChatLogRepository chatLogRepository;


    @Scheduled(cron = "0 */5 * * * ?") // 5분마다 실행
    @Transactional
    public void cleanupOldChatLogs() {
        // 오래된 데이터 ID 조회
    	 List<Long> oldLogIds = chatLogRepository.findOldestLogIds(); // LIMIT 100이 고정됨

        if (!oldLogIds.isEmpty()) {
            // 오래된 데이터 삭제
            chatLogRepository.deleteByIdIn(oldLogIds);
            System.out.println("삭제된 로그 ID: " + oldLogIds);
        } else {
            System.out.println("삭제할 데이터가 없습니다.");
        }
    }
}
