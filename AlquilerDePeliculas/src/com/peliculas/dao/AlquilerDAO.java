package com.peliculas.dao;

import com.peliculas.config.ConexionBD;
import com.peliculas.model.Alquiler;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlquilerDAO {

    public boolean registrar(Alquiler a) {
        String sql = "INSERT INTO alquiler (pelicula_id, cliente_id, fecha_alquiler) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getPeliculaId());
            ps.setInt(2, a.getClienteId());
            ps.setDate(3, a.getFechaAlquiler());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar alquiler: " + e.getMessage());
            return false;
        }
    }

    public List<Alquiler> listar() {
        List<Alquiler> lista = new ArrayList<>();
        String sql = "SELECT a.id, p.titulo AS pelicula, c.nombre AS cliente, a.fecha_alquiler " +
                     "FROM alquiler a " +
                     "JOIN pelicula p ON a.pelicula_id = p.id " +
                     "JOIN cliente c ON a.cliente_id = c.id ORDER BY a.id DESC";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Alquiler a = new Alquiler();
                a.setId(rs.getInt("id"));
                a.setTituloPelicula(rs.getString("pelicula"));
                a.setNombreCliente(rs.getString("cliente"));
                a.setFechaAlquiler(rs.getDate("fecha_alquiler"));
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar alquileres: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM alquiler WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar alquiler de MySQL: " + e.getMessage());
            return false;
        }
    }
}