package org.habv.tse;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 *
 * @author Herman
 */
public class Payload implements Serializable {

    private final String mensaje;
    private final OffsetDateTime fecha;

    public Payload(String mensaje, Object... parametros) {
        this.mensaje = String.format(mensaje, parametros);
        this.fecha = OffsetDateTime.now(ZoneId.of("-06:00"));
    }

    public String getMensaje() {
        return mensaje;
    }

    public OffsetDateTime getFecha() {
        return fecha;
    }

}
