package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PruebaConexion {
   /* public static void main(String[] args) {
        // 1. Coloca aquí los mismos datos de tu archivo de configuración
        String url = "jdbc:postgresql://localhost:5432/OmniKeyGames_db";
        String usuario = "postgres";
        String clave = "ruca2006"; // Cambia esto si pruebas con otra clave como 'postgres' o 'admin'

        System.out.println("🔄 Intentando conectar a PostgreSQL...");

        try {
            // 2. Forzar la carga del driver de Postgres
            Class.forName("org.postgresql.Driver");

            // 3. Intentar establecer el enlace físico
            try (Connection con = DriverManager.getConnection(url, usuario, clave)) {
                if (con != null && !con.isClosed()) {
                    System.out.println("=========================================");
                    System.out.println("✅ ¡CONEXIÓN EXITOSA!");
                    System.out.println("👤 Usuario: " + usuario);
                    System.out.println("🗄️ Base de datos detectada correctamente.");
                    System.out.println("=========================================");
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se encontró el Driver de PostgreSQL en el proyecto.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ ERROR DE AUTENTICACIÓN O CONFIGURACIÓN:");
            System.err.println("Código de error SQL: " + e.getErrorCode());
            System.err.println("Detalle del fallo: " + e.getMessage());
        }
    }*/
}
