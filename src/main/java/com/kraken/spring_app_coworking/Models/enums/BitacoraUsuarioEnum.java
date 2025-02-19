package com.kraken.spring_app_coworking.Models.enums;

public enum BitacoraUsuarioEnum {
    INICIO_DE_SESION("I"),
    CIERRE_DE_SESION("C"),
    REFRESH_TOKEN("R");

    private final String abreviacion;

    BitacoraUsuarioEnum(String abreviacion){
        this.abreviacion = abreviacion;
    }

    public String getAbreviacion(){
        return abreviacion;
    }
}
