package com.example.demo.service;

import com.example.demo.model.scraper.Juego;
import com.example.demo.repository.scraper.JuegoRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Scraper {

    private static final String FUENTE = "Steam";
    private static final String AGENTE_USUARIO =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    private final String servidorUrl = "jdbc:postgresql://localhost:5432/";
    private final String usuarioBd = "postgres";
    private final String claveBd = "Maxi1312$";
    private final String nombreBd = "OmniKeyGames_db";

    private final JuegoRepository juegoRepository;

    public Scraper(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
        inicializarBaseDeDatos();
    }

    private void inicializarBaseDeDatos() {
        try {
            Class.forName("org.postgresql.Driver");

            Connection conexionServidor = DriverManager.getConnection(servidorUrl, usuarioBd, claveBd);
            Statement stmt = conexionServidor.createStatement();
            try {
                stmt.executeUpdate("CREATE DATABASE " + nombreBd);
                System.out.println("Base de datos '" + nombreBd + "' creada.");
            } catch (Exception e) {
                System.out.println("La base de datos ya existe.");
            }
            stmt.close();
            conexionServidor.close();

            Connection conexionProyecto = DriverManager.getConnection(servidorUrl + nombreBd, usuarioBd, claveBd);
            Statement stmtTabla = conexionProyecto.createStatement();

            String sqlTabla = "CREATE TABLE IF NOT EXISTS juegos (" +
                    "titulo VARCHAR(255) NOT NULL, " +
                    "fuente VARCHAR(50) NOT NULL, " +
                    "url_imagen TEXT, " +
                    "precio NUMERIC(10,2), " +
                    "precio_original NUMERIC(10,2), " +
                    "moneda VARCHAR(10), " +
                    "plataforma VARCHAR(30), " +
                    "genero VARCHAR(50), " +
                    "url_producto TEXT, " +
                    "region VARCHAR(50), " +
                    "porcentaje_descuento NUMERIC(5,2), " +
                    "PRIMARY KEY (titulo, fuente)" +
                    ");";

            stmtTabla.executeUpdate(sqlTabla);
            System.out.println("Tabla 'juegos' verificada de forma correcta.");
            stmtTabla.close();
            conexionProyecto.close();

        } catch (Exception e) {
            System.err.println("Error inicializando almacenamiento: " + e.getMessage());
        }
    }

    public void ejecutarScraper() {
        String urlBase = "https://store.steampowered.com/search/?specials=1";
        int paginasMaximas = 5;

        Random aleatorio = new Random();

        System.out.println("Iniciando extracción en " + FUENTE + "...");

        for (int pagina = 1; pagina <= paginasMaximas; pagina++) {
            try {
                String urlObjetivo = urlBase + "&page=" + pagina;
                System.out.println("Analizando: " + urlObjetivo);

                String agenteDinamico = AGENTE_USUARIO + " Chrome/" + (120 + aleatorio.nextInt(5)) + ".0.0.0";

                Document documento = Jsoup.connect(urlObjetivo)
                        .userAgent(agenteDinamico)
                        .header("Accept-Language", "es-CL,es-419;q=0.9,es;q=0.8")
                        .timeout(20000)
                        .get();

                List<Juego> juegosExtraidos = procesarPagina(documento);

                if (juegosExtraidos.isEmpty()) {
                    System.out.println("No se encontraron juegos en la página " + pagina + ". Deteniendo.");
                    break;
                }

                for (Juego juego : juegosExtraidos) {
                    juegoRepository.guardarOActualizar(juego);
                }

                System.out.println("Página " + pagina + " procesada. Juegos guardados/actualizados: " + juegosExtraidos.size());

                int tiempoEsperaAlAzar = 9000 + aleatorio.nextInt(13000);
                System.out.println("Esperando "+ (tiempoEsperaAlAzar / 1000.0) + " segundos antes de avanzar.");

                Thread.sleep(tiempoEsperaAlAzar);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Error en página " + pagina + ": " + e.getMessage());

                try {
                    Thread.sleep(20000);
                } catch (Exception ignored) {}
            }
        }
        System.out.println("¡Scraping de Steam finalizado exitosamente!");
    }

    private List<Juego> procesarPagina(Document doc) {
        List<Juego> lista = new ArrayList<>();

        Elements tarjetas = doc.select("a.search_result_row");

        System.out.println("Tarjetas detectadas en el HTML de Steam: " + tarjetas.size());

        for (Element tarjeta : tarjetas) {
            try {
                Juego juego = mapearTarjetaAJuego(tarjeta);
                if (juego != null && juego.getTitulo() != null && !juego.getTitulo().trim().isEmpty()) {
                    lista.add(juego);
                }
            } catch (Exception e) {}
        }
        return lista;
    }

    private Juego mapearTarjetaAJuego(Element tarjeta) {
        String titulo = buscarTexto(tarjeta, "span.title");
        if (titulo == null || titulo.isEmpty()) return null;

        String urlImg = buscarAtributo(tarjeta, ".search_capsule img", "src");

        String urlProd = tarjeta.attr("href");

        BigDecimal precio = buscarPrecio(tarjeta, ".discount_final_price");
        BigDecimal precioOrig = buscarPrecio(tarjeta, ".discount_original_price");

        if (precioOrig == null) {
            precio = buscarPrecio(tarjeta, ".search_price");
            precioOrig = precio;
        }

        String textoDescuento = buscarTexto(tarjeta, ".discount_pct");
        BigDecimal descuento = BigDecimal.ZERO;
        if (textoDescuento != null && !textoDescuento.isEmpty()) {
            String numeroLimpio = textoDescuento.replaceAll("[^\\d]", "");
            if (!numeroLimpio.isEmpty()) {
                descuento = new BigDecimal(numeroLimpio);
            }
        }

        return new Juego(
                titulo.trim(),
                urlImg,
                precio,
                precioOrig,
                "CLP",
                "PC",
                adivinarGenero(titulo),
                urlProd,
                "Chile",
                descuento,
                FUENTE
        );
    }

    private String buscarTexto(Element origen, String selector) {
        Elements elementos = origen.select(selector);
        if (elementos.isEmpty()) return null;
        return elementos.first().text().trim();
    }

    private String buscarAtributo(Element origen, String selector, String atrib) {
        Elements elementos = origen.select(selector);
        if (elementos.isEmpty()) return null;
        return elementos.first().attr(atrib).trim();
    }

    private BigDecimal buscarPrecio(Element origen, String selector) {
        try {
            String texto = buscarTexto(origen, selector);
            if (texto == null) return null;
            String limpio = texto.replaceAll("[^\\d]", "");
            if (limpio.isEmpty()) return null;
            return new BigDecimal(limpio).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            return null;
        }
    }

    private String adivinarGenero(String titulo) {
        String t = titulo.toLowerCase();
        if (t.contains("rpg") || t.contains("witcher") || t.contains("fantasy") || t.contains("souls")) return "RPG";
        if (t.contains("fifa") || t.contains("fc") || t.contains("nba") || t.contains("f1") || t.contains("manager")) return "Simulación";
        if (t.contains("counter") || t.contains("strike") || t.contains("duty") || t.contains("battlefield") || t.contains("shooter")) return "Acción";
        if (t.contains("resident") || t.contains("evil") || t.contains("silent") || t.contains("horror")) return "Terror";
        if (t.contains("civilization") || t.contains("age") || t.contains("empire") || t.contains("strategy")) return "Estrategia";
        return "Indie";
    }
}