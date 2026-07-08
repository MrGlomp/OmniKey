package com.example.demo;

import com.example.demo.repository.scraper.JuegoRepository;
import com.example.demo.repository.scraper.JuegoRepositoryImpl;
import com.example.demo.service.Scraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    static void main(String[] args) {
            SpringApplication.run(Main.class, args);

        String url = "jdbc:postgresql://localhost:5432/OmniKeyGames_db";
        JuegoRepository miRepositorio = new JuegoRepositoryImpl(url,"postgres", "Maxi1312$");

        Scraper scraper = new Scraper(miRepositorio);

        scraper.ejecutarScraper();
    }
}