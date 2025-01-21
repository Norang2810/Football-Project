package com.example.football.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Badge {
    @Id
    private Long id; // 직접 관리할 ID

    private String name; // 훈장 이름
    private String description; // 훈장 설명
    private int price; // 훈장 가격
    private String imageUrl; // 훈장 이미지 경로

    // 기본 생성자
    public Badge() {}

    // 모든 필드 생성자
    public Badge(Long id, String description, String imageUrl, String name, int price) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
    }

    // Getters and Setters
    // ...


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
