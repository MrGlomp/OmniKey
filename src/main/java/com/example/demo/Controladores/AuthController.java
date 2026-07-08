package com.example.demo.Controladores;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        if (isAuthenticated()) {
            return "redirect:/juegos";
        }
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String username,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   Model model) {
        String resultado = authService.registrar(username, email, password);
        if (resultado.equals("OK")) {
            return "redirect:/login?registrado=true";
        }
        model.addAttribute("error", resultado);
        return "registro";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        if (isAuthenticated()) {
            return "redirect:/juegos";
        }
        return "login";
    }
}