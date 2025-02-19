package com.kraken.spring_app_coworking.Utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class IPUtils {
    //public static final List<String> IP_LOOPBACK = List.of("0:0:0:0:0:0:0:1","::1");

    /**
     *
     * @param clientIp recibe la ip del cliente
     * @return retorna si es que se puede la ip en formato ipv4 y si no es posible la retorna en formato ipv6
     */
    public static String getClientIP(String clientIp) {
        // Verificar si la IP es IPv6 y la convertir si es posible
        if (clientIp.equals("0:0:0:0:0:0:0:1") || clientIp.equals("::1")) {
            // Si es una IP de loopback, reemplazarla con IPv4
            clientIp = "127.0.0.1";
        } else {
            // Verificar si es una dirección IPv6 con un prefijo IPv4 (::ffff:xxx.xxx.xxx.xxx)
            if (clientIp.startsWith("::ffff:")) {
                clientIp = clientIp.substring(7); // Extraer la parte IPv4
            }
        }
        // en el caso de que no lo sea entonces retorna la ip de tipo ipv6
        return clientIp;
    }
}
