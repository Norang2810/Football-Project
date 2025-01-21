package com.example.football.controller;

import com.example.football.entity.Badge;
import com.example.football.entity.ShopItem;
import com.example.football.entity.Purchase;
import com.example.football.entity.User;
import com.example.football.repository.UserRepository;
import com.example.football.repository.BadgeRepository;
import com.example.football.repository.PurchaseRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ShopController {

    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    private final BadgeRepository badgeRepository;

    @Autowired
    public ShopController(UserRepository userRepository, PurchaseRepository purchaseRepository, BadgeRepository badgeRepository) {
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
        this.badgeRepository = badgeRepository;
    }

    // 상점 페이지를 표시하는 메서드
    @GetMapping("/shop")
    public String shop(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("points", loggedInUser.getPoints()); // 사용자 포인트를 모델에 추가
        }
        model.addAttribute("items", ShopItem.getShopItems()); // 상점 아이템 목록 추가
        model.addAttribute("badges", badgeRepository.findAll()); // 훈장 목록 추가
        return "shop";
    }

    // 아이템 구매 요청을 처리하는 메서드
    @PostMapping("/purchase")
    public String purchaseItem(@RequestParam("itemId") int itemId,
                                @RequestParam(value = "size", required = false) String size,
                                HttpSession session,
                                Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // 로그인하지 않은 경우 로그인 페이지로 리디렉션
        }

        ShopItem item = ShopItem.getItemById(itemId); // 아이템 ID로 아이템 가져오기
        if (item != null && loggedInUser.getPoints() >= item.getPrice()) {
            // 포인트 차감
            loggedInUser.setPoints(loggedInUser.getPoints() - item.getPrice());
            userRepository.save(loggedInUser); // 변경 사항 저장

            // 구매 내역 저장
            Purchase purchase = new Purchase(
                item.getName(),
                size,
                loggedInUser.getUsername(),
                loggedInUser.getAddress(), // 주소 추가
                LocalDateTime.now()
            );
            purchaseRepository.save(purchase);

            model.addAttribute("message", "구매 성공! " + item.getName() + " (" + size + ")을(를) 구매했습니다.");
        } else {
            model.addAttribute("message", "포인트가 부족하거나 잘못된 요청입니다.");
        }

        model.addAttribute("points", loggedInUser.getPoints());
        model.addAttribute("items", ShopItem.getShopItems());
        model.addAttribute("badges", badgeRepository.findAll()); // 훈장 데이터 추가
        return "shop";
    }

 // 훈장 구매 요청을 처리하는 메서드
    @PostMapping("/purchase/badge")
    public String purchaseBadge(@RequestParam("badgeId") Long badgeId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // 로그인하지 않은 경우 로그인 페이지로 리디렉션
        }

        // 훈장 조회
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 뱃지 ID입니다."));

        if (loggedInUser.getPoints() >= badge.getPrice()) {
            // 포인트 차감
            loggedInUser.setPoints(loggedInUser.getPoints() - badge.getPrice());
            
            // 사용자 훈장 업데이트 (엔티티 연결)
            loggedInUser.setBadge(badge); 
            userRepository.save(loggedInUser);

            model.addAttribute("message", badge.getName() + " 뱃지를 성공적으로 구매했습니다!");
        } else {
            model.addAttribute("message", "포인트가 부족합니다.");
        }

        // 상점 페이지에 필요한 데이터 추가
        model.addAttribute("points", loggedInUser.getPoints());
        model.addAttribute("items", ShopItem.getShopItems());
        model.addAttribute("badges", badgeRepository.findAll());
        return "shop";
    }

}
