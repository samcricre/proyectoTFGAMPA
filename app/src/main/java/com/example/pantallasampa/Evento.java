package com.example.pantallasampa;

public class Evento {

    private String titulo;
    private String descripcion;
    private String fechaIni;
    private String fechaFin;
    private String horaIni;
    private String horaFin;
    private String curso;

    public Evento(String titulo, String descripcion, String clase, String fechaIni, String fechaFin, String horaIni, String horaFin) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.curso = clase;
    }
    public Evento(String titulo, String descripcion, String clase, String fechaIni, String fechaFin) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.curso = clase;
    }
    public Evento(){}

    public String getTitulo() {return titulo;}

    public void setTitulo(String titulo) {this.titulo = titulo;}

    public String getDescripcion() {return descripcion;}

    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public String getFechaIni() {return fechaIni;}

    public void setFechaIni(String fechaIni) {this.fechaIni = fechaIni;}

    public String getFechaFin() {return fechaFin;}

    public void setFechaFin(String fechaFin) {this.fechaFin = fechaFin;}

    public String getHoraIni() {return horaIni;}

    public void setHoraIni(String horaIni) {this.horaIni = horaIni;}

    public String getHoraFin() {return horaFin;}

    public void setHoraFin(String horaFin) {this.horaFin = horaFin;}

    public String getCurso() {return curso;}

    public void setCurso(String curso) {this.curso = curso;}
}

