package org.habv.tse;

import org.habv.tse.mongodb.Collection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bson.Document;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 *
 * @author Herman Barrantes
 */
@RequestScoped
@Path("search")
@Tag(name = "Servicio de Busqueda", description = "Permite realizar busquedas en el padrón")
public class SearchController {

    @Inject
    @Collection("padron")
    private MongoCollection<Document> padron;

    @Operation(description = "Realiza una busqueda aleatorio")
    @APIResponse(
            responseCode = "200",
            description = "Retorna una lista aleatoria",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(
            responseCode = "400",
            description = "Alguno de los parámetros enviados es erroneo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @GET
    @Path("random")
    @Produces({MediaType.APPLICATION_JSON})
    public Response random(
            @Parameter(description = "Cantidad de valores a retornar, si no se indica la cantidad por defecto es 5, la cantidad mínima es 0 y la máxima 10")
            @QueryParam("size") @DefaultValue("5") Integer size) {
        if (size < 0) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new Payload("La cantidad debe ser mayor a cero y se obtuvo %d", size))
                    .build();
        }
        List<Document> random = new ArrayList<>(size);
        padron
                .aggregate(
                        Arrays.asList(
                                Aggregates.sample(size),
                                Aggregates.limit(10),
                                Aggregates.project(Projections.excludeId())))
                .into(random);
        return Response.ok(random).build();
    }

    @Operation(description = "Realiza la busqueda por número de cédula")
    @APIResponse(
            responseCode = "200",
            description = "Retorna la información de la persona con la cédula indicada",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(
            responseCode = "404",
            description = "No se encontró una persona con la cédula indicada",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @GET
    @Path("{cedula}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response search(
            @Parameter(description = "Número de cédula a buscar", required = true)
            @PathParam("cedula") String cedula) {
        Document doc = padron
                .find(Filters.eq("cedula", cedula))
                .projection(Projections.excludeId())
                .first();
        if (doc == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new Payload("No se encontró la cédula %s", cedula))
                    .build();
        }
        return Response.ok(doc).build();
    }

}
