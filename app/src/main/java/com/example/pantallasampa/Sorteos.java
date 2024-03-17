package com.example.pantallasampa;

import java.util.List;

public class Sorteos {
    private String nombre;
    private String descripcion;
    private String premios;
    private String ganador;
    private String creador;
    private boolean finalizado;
    private String codigoNoticia;

    public Sorteos() {
        // Constructor vacío necesario para Firebase
    }

    public Sorteos(String nombre, String descripcion, String premios, String creador, boolean finalizado, String codigoNoticia) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.premios = premios;
        this.creador = creador;
        this.finalizado = finalizado;
        this.codigoNoticia = codigoNoticia;
    }

    // Getters y setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPremios() {
        return premios;
    }

    public void setPremios(String premios) {
        this.premios = premios;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getCodigoNoticia() {
        return codigoNoticia;
    }

    public void setCodigoNoticia(String codigoNoticia) {
        this.codigoNoticia = codigoNoticia;
    }
}
