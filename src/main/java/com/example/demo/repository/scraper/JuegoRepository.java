package com.example.demo.repository.scraper;
import com.example.demo.model.scraper.Juego;
import java.util.List;

public interface JuegoRepository {
    void guardarOActualizar(Juego juego);
    List<Juego> buscarFiltradoYOrdenado(String buscar, String genero, String orden);
    List<Juego> obtenerTodos();
}
