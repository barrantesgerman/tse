package org.habv.tse;

import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 *
 * @author Herman Barrantes
 */
public class Constantes {

    public static final ZoneId ZONE_ID_CR = ZoneId.of("-06:00");//CR
    public static final ZoneOffset ZONE_OFFSET_CR = ZoneOffset.of("-06:00");//CR
    public static final String CEDULA = "cedula";
    public static final String NOMBRE = "nombre";
    public static final String PRIMER_APELLIDO = "primerApellido";
    public static final String SEGUNDO_APELLIDO = "segundoApellido";

    private Constantes() {
    }

}
