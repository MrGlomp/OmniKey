package com.example.demo.repository.scraper;

import com.example.demo.model.scraper.Juego;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JuegoRepositoryImpl implements JuegoRepository {

    private final String urlConexion;
    private final String usuarioBd;
    private final String claveBd;

    public JuegoRepositoryImpl(
            @Value("${app.datasource.scraper.jdbc-url}") String urlConexion,
            @Value("${app.datasource.scraper.username}") String usuarioBd,
            @Value("${app.datasource.scraper.password}") String claveBd) {
        this.urlConexion = urlConexion;
        this.usuarioBd = usuarioBd;
        this.claveBd = claveBd;
    }

    @Override
    public void guardarOActualizar(Juego juego) {
        String sqlUpsert = "INSERT INTO juegos (titulo, fuente, url_imagen, precio, precio_original, moneda, plataforma, genero, url_producto, region, porcentaje_descuento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (titulo, fuente) DO UPDATE SET " +
                "url_imagen = EXCLUDED.url_imagen, " +
                "precio = EXCLUDED.precio, " +
                "precio_original = EXCLUDED.precio_original, " +
                "url_producto = EXCLUDED.url_producto, " +
                "porcentaje_descuento = EXCLUDED.porcentaje_descuento;";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection con = DriverManager.getConnection(urlConexion, usuarioBd, claveBd);
                 PreparedStatement pstmt = con.prepareStatement(sqlUpsert)) {

                pstmt.setString(1, juego.getTitulo());
                pstmt.setString(2, juego.getFuente());
                pstmt.setString(3, juego.getUrlImagen());
                pstmt.setBigDecimal(4, juego.getPrecio());
                pstmt.setBigDecimal(5, juego.getPrecioOriginal());
                pstmt.setString(6, juego.getMoneda());
                pstmt.setString(7, juego.getPlataforma());
                pstmt.setString(8, juego.getGenero());
                pstmt.setString(9, juego.getUrlProducto());
                pstmt.setString(10, juego.getRegion());
                pstmt.setBigDecimal(11, juego.getPorcentajeDescuento());

                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println("Error en Repositorio al guardar/actualizar juego: " + e.getMessage());
        }
    }

    @Override
    public List<Juego> buscarFiltradoYOrdenado(String buscar, String genero, String orden){
        List<Juego> listaJuegos = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM juegos WHERE 1=1");
        List<String> parametros = new ArrayList<>();

        if (buscar != null && !buscar.trim().isEmpty()) {
            sql.append(" AND LOWER(titulo) LIKE LOWER(?)");
            parametros.add("%" + buscar.trim() + "%");
        }

        if (genero != null && !genero.trim().isEmpty() && !genero.equalsIgnoreCase("all")) {
            sql.append(" AND LOWER(genero) = LOWER(?)");
            parametros.add(genero.trim());
        }

        if (orden != null && !orden.trim().isEmpty()) {
            switch (orden) {
                case "price-asc":
                    sql.append(" ORDER BY precio ASC");
                    break;
                case "price-desc":
                    sql.append(" ORDER BY precio DESC");
                    break;
                case "name":
                    sql.append(" ORDER BY titulo ASC");
                    break;
                case "savings":
                default:
                    sql.append(" ORDER BY porcentaje_descuento DESC");
                    break;
            }
        } else {
            sql.append(" ORDER BY porcentaje_descuento DESC");
        }

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection con = DriverManager.getConnection(urlConexion, usuarioBd, claveBd);
                 PreparedStatement pstmt = con.prepareStatement(sql.toString())) {

                for (int i = 0; i < parametros.size(); i++) {
                    pstmt.setString(i + 1, parametros.get(i));
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Juego juego = new Juego();
                        juego.setTitulo(rs.getString("titulo"));
                        juego.setFuente(rs.getString("fuente"));
                        juego.setUrlImagen(rs.getString("url_imagen"));
                        juego.setPrecio(rs.getBigDecimal("precio"));
                        juego.setPrecioOriginal(rs.getBigDecimal("precio_original"));
                        juego.setMoneda(rs.getString("moneda"));
                        juego.setPlataforma(rs.getString("plataforma"));
                        juego.setGenero(rs.getString("genero"));
                        juego.setUrlProducto(rs.getString("url_producto"));
                        juego.setRegion(rs.getString("region"));
                        juego.setPorcentajeDescuento(rs.getBigDecimal("porcentaje_descuento"));

                        listaJuegos.add(juego);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error en Repositorio al filtrar juegos: " + e.getMessage());
        }

        return listaJuegos;
    }

    @Override
    public List<Juego> obtenerTodos() {
        List<Juego> listaJuegos = new ArrayList<>();
        String sql = "SELECT * FROM juegos;";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection con = DriverManager.getConnection(urlConexion, usuarioBd, claveBd);
                 PreparedStatement pstmt = con.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Juego juego = new Juego();
                    juego.setTitulo(rs.getString("titulo"));
                    juego.setFuente(rs.getString("fuente"));
                    juego.setUrlImagen(rs.getString("url_imagen"));
                    juego.setPrecio(rs.getBigDecimal("precio"));
                    juego.setPrecioOriginal(rs.getBigDecimal("precio_original"));
                    juego.setMoneda(rs.getString("moneda"));
                    juego.setPlataforma(rs.getString("plataforma"));
                    juego.setGenero(rs.getString("genero"));
                    juego.setUrlProducto(rs.getString("url_producto"));
                    juego.setRegion(rs.getString("region"));
                    juego.setPorcentajeDescuento(rs.getBigDecimal("porcentaje_descuento"));

                    listaJuegos.add(juego);
                }
            }
        } catch (Exception e) {
            System.err.println("Error en Repositorio al listar los juegos: " + e.getMessage());
        }

        return listaJuegos;
    }
}
