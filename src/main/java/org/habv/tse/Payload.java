package org.habv.tse;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 *
 * @author Herman
 */
public class Payload implements Serializable {

    private final String mensaje;
    private final ZonedDateTime fecha;

    public Payload(String mensaje, Object... parametros) {
        this.mensaje = String.format(mensaje, parametros);
        this.fecha = ZonedDateTime.now();
    }

    public String getMensaje() {
        return mensaje;
    }

    public ZonedDateTime getFecha() {
        return fecha;
    }

}
