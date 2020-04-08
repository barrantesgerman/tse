package org.habv.tse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
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
    @APIResponse(
            responseCode = "200",
            description = "Se envió la solicitud de recarga",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(
            responseCode = "409",
            description = "Se está recargando la base de datos actualmente, inténtelo más tarde",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response reload() {
        return reloadService.reload();
    }

    @Operation(description = "Verifica el estado de la carga de la base de datos y muestra la bitácora.")
    @APIResponse(
            responseCode = "200",
            description = "Se obtiene exitosamente el estado de la carga y la bitácora",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @GET
    @Path("status")
    @Produces({MediaType.APPLICATION_JSON})
    public Response status() {
        return reloadService.status();
    }

}
