package com.example.pantallasampa;

public class Correo {
    private String correoId; // Nuevo atributo
    private String remitente;
    private String destinatario;
    private String asunto;
    private String contenido;
    private long timestamp;
    private boolean eliminado;
    private boolean eliminadoRemitente;

    public Correo() {
        // Constructor vacío requerido por Firebase
    }

    public Correo(String correoId, String remitente, String destinatario, String asunto, String contenido, long timestamp, boolean eliminado, boolean eliminadoRemitente) {
        this.correoId = correoId;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.contenido = contenido;
        this.timestamp = timestamp;
        this.eliminado = eliminado;
        this.eliminadoRemitente = eliminadoRemitente;
    }

    // Getter y setter para correoId
    public String getCorreoId() {
        return correoId;
    }

    public void setCorreoId(String correoId) {
        this.correoId = correoId;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public boolean isEliminadoRemitente() {
        return eliminadoRemitente;
    }

    public void setEliminadoRemitente(boolean eliminadoRemitente) {
        this.eliminadoRemitente = eliminadoRemitente;
    }
}
