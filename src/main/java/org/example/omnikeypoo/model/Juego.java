package org.example.omnikeypoo.model;

public class Juego {
    private String titulo;
    private String precio;
    private String imagen;
    private String tienda;

    public Juego(){

    }

    public Juego(String titulo, String precio, String imagen, String tienda) {
        this.titulo = titulo;
        this.precio = precio;
        this.imagen = imagen;
        this.tienda = tienda;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

  @Override
  public String toString(){
        return "Juego{" +
                "titulo:'" + titulo + '\'' +
                ", precio:'" + precio + '\'' +
                ", Imagen:'" + imagen + '\'' +
                ", Tienda:'" + tienda + '\'' +
                '}';
    }
}
