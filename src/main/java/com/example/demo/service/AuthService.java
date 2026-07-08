package com.example.demo.service;

import com.example.demo.model.login.LoginAttempt;
import com.example.demo.model.login.User;
import com.example.demo.repository.login.LoginAttemptRepository;
import com.example.demo.repository.login.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final int MAX_INTENTOS = 5;
    private static final int MINUTOS_BLOQUEO = 10;

    public String registrar(String username, String email, String contraseñaPlana) {
        if (userRepository.existsByUsername(username)) {
            return "El nombre de usuario ya existe";
        }
        if (userRepository.existsByEmail(email)) {
            return "El correo ya está registrado";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(contraseñaPlana));
        user.setRole("USER");
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return "OK";
    }

    public String login(String username, String contraseñaPlana, String ipAddress) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return "Usuario o contraseña incorrectos";
        }

        User user = userOpt.get();

        if (!user.getIsActive()) {
            return "Cuenta desactivada, contacta al administrador";
        }

        LocalDateTime hace10Min = LocalDateTime.now().minusMinutes(MINUTOS_BLOQUEO);
        List<LoginAttempt> fallidos = loginAttemptRepository
                .findByUserIdAndSuccessFalseAndLoginTimeAfter(user.getId(), hace10Min);

        if (fallidos.size() >= MAX_INTENTOS) {
            return "Cuenta bloqueada temporalmente por múltiples intentos fallidos. Intenta en " + MINUTOS_BLOQUEO + " minutos";
        }

        boolean coincide = passwordEncoder.matches(contraseñaPlana, user.getPasswordHash());

        loginAttemptRepository.save(new LoginAttempt(user.getId(), ipAddress, coincide));

        if (!coincide) {
            return "Usuario o contraseña incorrectos";
        }

        return "OK";
    }
}