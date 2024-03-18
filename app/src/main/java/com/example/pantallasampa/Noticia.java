package com.example.pantallasampa;

public class Noticia {

    String titular;
    String subtitulo;
    String cuerpo;
    int clicks;
    int noticiaId; // Nuevo atributo

    public Noticia() {
    }

    public Noticia(String titular, String subtitulo, String cuerpo, int clicks, int noticiaId) {
        this.titular = titular;
        this.subtitulo = subtitulo;
        this.cuerpo = cuerpo;
        this.clicks = clicks;
        this.noticiaId = noticiaId; // Inicialización del nuevo atributo
    }

    public Noticia(String titular, String subtitulo, String cuerpo, int clicks) {
        this.titular = titular;
        this.subtitulo = subtitulo;
        this.cuerpo = cuerpo;
        this.clicks = clicks;
    }


    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getNoticiaId() {
        return noticiaId;
    }

    public void setNoticiaId(int noticiaId) {
        this.noticiaId = noticiaId;
    }
}
