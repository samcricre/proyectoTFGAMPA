package com.example.pantallasampa;

public class Hijo {

    //Variables
    String nombre;
    String apellidos;
    String edad;
    String curso;


    //Constructores
    public Hijo(){}

    public Hijo(String nombre, String apellidos, String edad, String curso) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.curso = curso;
    }

    //Getter y setter
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
