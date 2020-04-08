package org.habv.tse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author Herman Barrantes
 */
public class ReloadException extends WebApplicationException {

    private static final String MENSAJE_ERROR = "Se está recargando la base de datos actualmente, inténtelo más tarde";

    public ReloadException() {
        super(
                MENSAJE_ERROR,
                Response
                        .status(Response.Status.CONFLICT)
                        .entity(new Payload(MENSAJE_ERROR))
                        .build());
    }

}
