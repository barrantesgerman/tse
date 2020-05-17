package org.habv.tse;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 *
 * @author Herman
 */
public class Payload implements Serializable {

    private final String mensaje;
    private final OffsetDateTime fecha;

    public Payload(String mensaje, Object... parametros) {
        this.mensaje = String.format(mensaje, parametros);
        this.fecha = OffsetDateTime.now(Constantes.ZONE_ID_CR);
    }

    public String getMensaje() {
        return mensaje;
    }

    public OffsetDateTime getFecha() {
        return fecha;
    }

}
