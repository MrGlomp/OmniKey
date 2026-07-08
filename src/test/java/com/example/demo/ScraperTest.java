package com.example.demo;
import com.example.demo.model.scraper.Juego;
import com.example.demo.repository.scraper.JuegoRepository;
import com.example.demo.service.Scraper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
public class ScraperTest {
    @Mock
    private JuegoRepository juegoRepository;

    @InjectMocks
    private Scraper scraper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScraperLlamaGuardarOActualizar() {
        Juego juegoMock = new Juego();
        juegoMock.setTitulo("Test Game");

        juegoRepository.guardarOActualizar(juegoMock);

        verify(juegoRepository).guardarOActualizar(any(Juego.class));
    }
}
