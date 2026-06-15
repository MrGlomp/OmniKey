package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        String url = "https://store.steampowered.com/search/?page=";
        int tiempoEspera = 3 * 60 * 1000; // 3 minutos

        while (true) {
            try {

                int paginaAleatoria = (int) (Math.random() * 100) + 1;
                String urlFinal = url + paginaAleatoria + "&t=" + System.currentTimeMillis();
                System.out.println("---Iniciando Extraccion---");

                Document doc = Jsoup.connect(urlFinal)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .header("Cache-Control", "no-cache")
                        .get();

                Elements juegos = doc.select(".search_result_row");

                if (!juegos.isEmpty()) {

                    int indiceAleatorio = (int) (Math.random() * juegos.size());
                    Element juegoAzar = juegos.get(indiceAleatorio);
                    String urlJuego = juegoAzar.attr("href").split("\\?")[0];

                    String nombre = juegoAzar.select(".title").text();
                    Document juegoDoc = Jsoup.connect(urlJuego + "?t=" +System.currentTimeMillis())
                            .userAgent("Mozilla/5.0")
                            .cookie("birthtime", "283996801")
                            .cookie("lastagecheckage", "1-0-1990")
                            .get();

                    Element precioElem = juegoDoc.selectFirst(".game_purchase_price.price");

                    String precio;
                    if (precioElem != null) {
                        precio = precioElem.text().trim();
                    } else {
                        Element precioOferta = juegoDoc.selectFirst(".discount_final_price");
                        if (precioOferta != null) {
                            precio = precioOferta.text().trim() + " (En Oferta)";
                        } else {
                            Element dataPriceElem = juegoDoc.selectFirst("[data-price-final]");
                            precio = (dataPriceElem != null) ? dataPriceElem.text().trim() : "Precio no disponible directamente";
                        }
                    }

                    System.out.println("[" + LocalTime.now().withNano(0) + "] Extraccion Exitosa:");
                    System.out.println("Nombre: "+nombre);
                    System.out.println("Precio: "+precio);
                    System.out.println("Enlace: "+urlJuego);
                    System.out.println("--------------------------------------------\n");
                } else {
                    System.out.println("No se encontraron juegos en esta pagina.");
                }

                System.out.println("Esperando 3 minutos...");
                Thread.sleep(tiempoEspera);

            } catch (Exception e) {
                System.err.println("Error: "+e.getMessage());
                try {
                    Thread.sleep(10000);
                } catch (Exception ex) {}
            }
        }
    }
}
