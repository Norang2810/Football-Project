package com.example.football.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.football.entity.Badge;
import com.example.football.repository.BadgeRepository;
import com.example.football.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    public DataLoader(BadgeRepository badgeRepository, UserRepository userRepository) {
        this.badgeRepository = badgeRepository;
        this.userRepository = userRepository;
    }

    @Transactional // 트랜잭션을 사용하여 모든 작업을 하나의 단위로 처리
    @Override
    public void run(String... args) throws Exception {
        // 1. 외래 키 초기화: user 테이블에서 badge_id를 NULL로 설정
        userRepository.updateBadgeToNull(); // Custom Query 사용
        System.out.println("User 테이블의 badge_id가 초기화되었습니다.");

        // 2. badge 테이블 초기화: 기존 데이터 삭제
        badgeRepository.deleteAll();
        System.out.println("Badge 테이블의 데이터가 삭제되었습니다.");

        // 3. 새로운 데이터 삽입
        badgeRepository.save(new Badge(2L, "은색 뱃지", "images/badges/silver_badge.png", "Silver badge", 30000));
        badgeRepository.save(new Badge(3L, "금색 뱃지", "images/badges/gold_badge.png", "Gold badge", 50000));
        badgeRepository.save(new Badge(1L, "동색 뱃지", "images/badges/bronze_badge.png", "Bronze badge", 10000));
        badgeRepository.save(new Badge(4L, "다이아몬드 뱃지", "images/badges/diamond_badge.svg", "Diamond badge", 100000));
        System.out.println("Badge 테이블이 새 데이터로 초기화되었습니다.");
    }
}
