package com.example.demo.model.login;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_attempts")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

    @Column(name = "ip_address")
    private String ipAddress;

    private Boolean success;

    public LoginAttempt() {
    }

    public LoginAttempt(Long userId, String ipAddress, Boolean success) {
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.success = success;
        this.loginTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
