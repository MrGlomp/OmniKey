package com.example.demo.repository.login;

import com.example.demo.model.login.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    List<LoginAttempt> findByUserIdAndSuccessFalseAndLoginTimeAfter(Long userId, LocalDateTime after);
}
