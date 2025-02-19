package com.kraken.spring_app_coworking.Exceptions;

public class EntityNotFoundException extends Exception{
    public EntityNotFoundException(){ super("Error.. entidad no encontrada...");}
    public EntityNotFoundException(String args){ super(args);}
}
