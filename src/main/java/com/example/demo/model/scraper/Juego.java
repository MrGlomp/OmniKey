package com.example.demo.model.scraper;
import java.math.BigDecimal;

public class Juego {
    private String titulo;
    private String urlImagen;
    private BigDecimal precio;
    private BigDecimal precioOriginal;
    private String moneda;
    private String plataforma;
    private String genero;
    private String urlProducto;
    private String region;
    private BigDecimal porcentajeDescuento;
    private String fuente;

    public Juego() {}

    public Juego(String titulo, String urlImagen, BigDecimal precio, BigDecimal precioOriginal,
                 String moneda, String plataforma, String genero, String urlProducto,
                 String region, BigDecimal porcentajeDescuento, String fuente) {
        this.titulo = titulo;
        this.urlImagen = urlImagen;
        this.precio = precio;
        this.precioOriginal = precioOriginal;
        this.moneda = moneda;
        this.plataforma = plataforma;
        this.genero = genero;
        this.urlProducto = urlProducto;
        this.region = region;
        this.porcentajeDescuento = porcentajeDescuento;
        this.fuente = fuente;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrlImagen() {
        return urlImagen;
    }
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public BigDecimal getPrecio() {
        return precio;
    }
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecioOriginal() {
        return precioOriginal;
    }
    public void setPrecioOriginal(BigDecimal precioOriginal) {
        this.precioOriginal = precioOriginal;
    }

    public String getMoneda() {
        return moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getPlataforma() {
        return plataforma;
    }
    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getUrlProducto() {
        return urlProducto;
    }
    public void setUrlProducto(String urlProducto) {
        this.urlProducto = urlProducto;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public String getFuente() {
        return fuente;
    }
    public void setFuente(String fuente) {
        this.fuente = fuente;
    }
}
