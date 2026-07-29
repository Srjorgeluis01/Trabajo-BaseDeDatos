package com.peliculas.model;

import java.sql.Date;

public class Alquiler {
    private int id;
    private int peliculaId;
    private int clienteId;
    private Date fechaAlquiler;

    // Auxiliares para mostrar nombres en tabla
    private String tituloPelicula;
    private String nombreCliente;

    public Alquiler() {}

    public Alquiler(int peliculaId, int clienteId, Date fechaAlquiler) {
        this.peliculaId = peliculaId;
        this.clienteId = clienteId;
        this.fechaAlquiler = fechaAlquiler;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPeliculaId() { return peliculaId; }
    public void setPeliculaId(int peliculaId) { this.peliculaId = peliculaId; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public Date getFechaAlquiler() { return fechaAlquiler; }
    public void setFechaAlquiler(Date fechaAlquiler) { this.fechaAlquiler = fechaAlquiler; }
    public String getTituloPelicula() { return tituloPelicula; }
    public void setTituloPelicula(String tituloPelicula) { this.tituloPelicula = tituloPelicula; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
}