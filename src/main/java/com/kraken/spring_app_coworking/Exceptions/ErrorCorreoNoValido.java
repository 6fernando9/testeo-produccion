package com.kraken.spring_app_coworking.Exceptions;

public class ErrorCorreoNoValido extends Exception{
    public ErrorCorreoNoValido(){ super("Error.. Correo No Valido...");}
    public ErrorCorreoNoValido(String args){ super(args);}
}
