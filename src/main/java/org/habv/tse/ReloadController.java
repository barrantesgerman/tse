package org.habv.tse;

import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 *
 * @author Herman Barrantes
 */
@RequestScoped
@Path("reload")
@Tag(name = "Servicio de Recarga", description = "Descarga el padrón del TSE y recarga la Base de Datos")
public class ReloadController {

    @Inject
    private ReloadService reloadService;

    @Operation(description = "Realiza la recarga de la Base de Datos, este método se debe usar como contingencia, la carga toma varios minutos.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Se recargó exitosamente la Base de Datos"),
        @APIResponse(responseCode = "400", description = "No se logró recargar la Base de Datos")
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response reload() {
        try {
            reloadService.reload();
            return Response.ok(new Payload("OK")).build();
        } catch (IOException ex) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new Payload("Ocurrió un error: %s", ex.getMessage()))
                    .build();
        }
    }

}
