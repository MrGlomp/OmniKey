package com.example.demo;
import com.example.demo.model.scraper.Juego;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
public class JuegoTest {
    @Test
    void testAsignacionDatosBasicos() {
        Juego juego = new Juego();
        juego.setTitulo("Elden Ring");
        juego.setGenero("RPG");

        assertEquals("Elden Ring", juego.getTitulo());
        assertEquals("RPG", juego.getGenero());
    }

    @Test
    void testAsignacionDatosNumericos() {
        Juego juego = new Juego();
        juego.setPrecio(new BigDecimal("33490"));
        juego.setPorcentajeDescuento(new BigDecimal("44"));

        assertEquals(new BigDecimal("33490"), juego.getPrecio());
        assertEquals(new BigDecimal("44"), juego.getPorcentajeDescuento());
    }
}
