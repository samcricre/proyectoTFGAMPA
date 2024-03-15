package com.example.pantallasampa;

public class Noticia {

    String titular;
    String subtitulo;
    String cuerpo;

    public Noticia() {
    }

    public Noticia(String titular, String subtitulo, String cuerpo) {
        this.titular = titular;
        this.subtitulo = subtitulo;
        this.cuerpo = cuerpo;
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

}
