package com.example.demo;
import com.example.demo.Controladores.ControladorJuego;
import com.example.demo.model.scraper.Juego;
import com.example.demo.repository.scraper.JuegoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControladorJuegoTest {
    @Mock
    private JuegoRepository juegoRepository;

    @Mock
    private Model model;

    @InjectMocks
    private ControladorJuego controladorJuego;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetornaVistaCorrecta() {
        String vista = controladorJuego.listarJuegos(null, null, null, model);
        assertEquals("OmniKey", vista);
    }

    @Test
    void testListarJuegosSinFiltros() {
        List<Juego> listaMock = new ArrayList<>();
        listaMock.add(new Juego());
        when(juegoRepository.buscarFiltradoYOrdenado(null, null, null)).thenReturn(listaMock);
        controladorJuego.listarJuegos(null, null, null, model);
        verify(model).addAttribute("juegos", listaMock);
    }

    @Test
    void testListarJuegosConParametroDeBusqueda() {
        controladorJuego.listarJuegos("Cyberpunk", null, null, model);
        verify(juegoRepository).buscarFiltradoYOrdenado("Cyberpunk", null, null);
    }

    @Test
    void testListarJuegosConParametroDeGenero() {
        controladorJuego.listarJuegos(null, "Acción", null, model);
        verify(juegoRepository).buscarFiltradoYOrdenado(null, "Acción", null);
    }
    @Test
    void testListarJuegosConParametroDeOrden() {
        controladorJuego.listarJuegos(null, null, "precio-asc", model);
        verify(juegoRepository).buscarFiltradoYOrdenado(null, null, "precio-asc");
    }

    @Test
    void testModeloMantieneEstadoDeFiltros() {
        controladorJuego.listarJuegos("Zelda", "RPG", "precio-desc", model);

        verify(model).addAttribute("buscarActual", "Zelda");
        verify(model).addAttribute("generoActual", "RPG");
        verify(model).addAttribute("ordenActual", "precio-desc");
    }
    @Test
    void testListarJuegosListaVacia() {
        when(juegoRepository.buscarFiltradoYOrdenado("NoExiste", null, null))
                .thenReturn(List.of());
        controladorJuego.listarJuegos("NoExiste", null, null, model);

        verify(model).addAttribute(eq("juegos"), argThat(lista ->
                ((List<?>) lista).stream().count() == 0));
    }

}
