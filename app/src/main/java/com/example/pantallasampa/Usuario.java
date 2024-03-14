package com.example.pantallasampa;

public class Usuario {

    private String nombre;
    private String apellidos;
    private String email;
    private String telf;
    private String roll;

    public Usuario(){}

    public Usuario(String nombre, String apellidos, String email, String telf){
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telf = telf;
        this.roll = "usuario";
    }
    public String getRoll() {return roll;}

    public void setRoll(String roll) {this.roll = roll;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getApellidos() {return apellidos;}

    public void setApellidos(String apellidos) {this.apellidos = apellidos;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getTelf() {return telf;}

    public void setTelf(String telf) {this.telf = telf;}
}
