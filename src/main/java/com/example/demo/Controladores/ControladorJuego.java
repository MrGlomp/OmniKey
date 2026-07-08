package com.example.demo.Controladores;
import com.example.demo.model.scraper.Juego;
import com.example.demo.repository.scraper.JuegoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
public class ControladorJuego {
    private final JuegoRepository juegoRepository;

    public ControladorJuego(JuegoRepository juegoRepository){
        this.juegoRepository=juegoRepository;
    }
    @GetMapping("/juegos")
    public String listarJuegos(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String orden,
            Model model) {

        List<Juego> listaJuegos = juegoRepository.buscarFiltradoYOrdenado(buscar,genero,orden);
        model.addAttribute("juegos", listaJuegos);
        model.addAttribute("buscarActual", buscar);
        model.addAttribute("generoActual", genero);
        model.addAttribute("ordenActual" , orden);

        return "OmniKey";
    }
}
