package org.habv.tse;

import org.habv.tse.mongodb.Collection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bson.Document;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 *
 * @author Herman Barrantes
 */
@RequestScoped
@Path("search")
@Tag(name = "Servicio de Busqueda", description = "Permite realizar la busqueda en el padrón")
public class SearchController {

    @Inject
    @Collection("padron")
    private MongoCollection<Document> padron;

    @Operation(description = "Realiza la busqueda por número de cédula")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Retorna la información de la persona con la cédula indicada"),
        @APIResponse(responseCode = "404", description = "No se encontró una persona con la cédula indicada")
    })
    @GET
    @Path("{cedula:\\d{9,9}}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response search(@PathParam("cedula") String cedula) {
        Document doc = padron
                .find(Filters.eq("cedula", cedula))
                .projection(Projections.excludeId())
                .first();
        if (doc == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new Payload("No se encontró la cédula: %s", cedula))
                    .build();
        }
        return Response.ok(doc).build();
    }

}
