package com.kraken.spring_app_coworking.Exceptions.dto;

import lombok.Builder;

import java.util.Date;

//un record tiene ya definidos como equals o hashcode y tostring,getters,setters, y un constructor
//cada atributo es inmutable
//es una clase inmutable
@Builder
public record Error(String mensaje,//para mensaje personalizado
                    String descripcion,//causa del error
                    int status,//si es un 404,403 y asi
                    Date date) {
}
