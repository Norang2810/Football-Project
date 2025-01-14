package com.example.football.repository;

import com.example.football.entity.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
    List<ChatLog> findByMatchIdOrderByTimestampAsc(Long matchId);
    @Query(value = "SELECT id FROM chat_log ORDER BY timestamp ASC LIMIT 10", nativeQuery = true)
    List<Long> findOldestLogIds();


    // 특정 ID를 기반으로 데이터 삭제
    void deleteByIdIn(List<Long> ids);
    
}
