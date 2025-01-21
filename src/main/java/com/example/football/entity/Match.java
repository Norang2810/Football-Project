package com.example.football.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "`match_table`")
public class Match {

    @Id
    private Long id;

    @Column(name = "`utc_date`")
    private String utcDate;

    @Column(name = "home_team")
    private String homeTeam;

    @Column(name = "away_team")
    private String awayTeam;

    @Column(name = "away_score")
    private Integer awayScore;

    @Column(name = "away_team_crest")
    private String awayTeamCrest;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "home_team_crest")
    private String homeTeamCrest;

    @Column(name = "match_winner")
    private String matchWinner;

    @Column(name = "status")
    private String status;

    @Column(name = "home_team_short_name")
    private String homeTeamShortName;

    @Column(name = "home_team_tla")
    private String homeTeamTla;

    @Column(name = "away_team_short_name")
    private String awayTeamShortName;

    @Column(name = "away_team_tla")
    private String awayTeamTla;

    @Column(name = "home_team_korean_name")
    private String homeTeamKoreanName;

    @Column(name = "away_team_korean_name")
    private String awayTeamKoreanName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(String utcDate) {
        this.utcDate = utcDate;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public String getAwayTeamCrest() {
        return awayTeamCrest;
    }

    public void setAwayTeamCrest(String awayTeamCrest) {
        this.awayTeamCrest = awayTeamCrest;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public String getHomeTeamCrest() {
        return homeTeamCrest;
    }

    public void setHomeTeamCrest(String homeTeamCrest) {
        this.homeTeamCrest = homeTeamCrest;
    }

    public String getMatchWinner() {
        return matchWinner;
    }

    public void setMatchWinner(String matchWinner) {
        this.matchWinner = matchWinner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHomeTeamShortName() {
        return homeTeamShortName;
    }

    public void setHomeTeamShortName(String homeTeamShortName) {
        this.homeTeamShortName = homeTeamShortName;
    }

    public String getHomeTeamTla() {
        return homeTeamTla;
    }

    public void setHomeTeamTla(String homeTeamTla) {
        this.homeTeamTla = homeTeamTla;
    }

    public String getAwayTeamShortName() {
        return awayTeamShortName;
    }

    public void setAwayTeamShortName(String awayTeamShortName) {
        this.awayTeamShortName = awayTeamShortName;
    }

    public String getAwayTeamTla() {
        return awayTeamTla;
    }

    public void setAwayTeamTla(String awayTeamTla) {
        this.awayTeamTla = awayTeamTla;
    }

    public String getHomeTeamKoreanName() {
        return homeTeamKoreanName;
    }

    public void setHomeTeamKoreanName(String homeTeamKoreanName) {
        this.homeTeamKoreanName = homeTeamKoreanName;
    }

    public String getAwayTeamKoreanName() {
        return awayTeamKoreanName;
    }

    public void setAwayTeamKoreanName(String awayTeamKoreanName) {
        this.awayTeamKoreanName = awayTeamKoreanName;
    }

    public String getKstDate() {
        LocalDateTime utcDateTime = LocalDateTime.parse(utcDate, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime kstDateTime = utcDateTime.plusHours(9);
        return kstDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"));
    }

    @Override
    public String toString() {
        return "Match{" +
                "homeTeam='" + homeTeam + '\'' +
                ", homeTeamKoreanName='" + homeTeamKoreanName + '\'' +
                ", homeScore=" + (homeScore != null ? homeScore : "N/A") +
                ", awayTeam='" + awayTeam + '\'' +
                ", awayTeamKoreanName='" + awayTeamKoreanName + '\'' +
                ", awayScore=" + (awayScore != null ? awayScore : "N/A") +
                ", kstDate='" + getKstDate() + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
