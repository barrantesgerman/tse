package org.habv.tse;

import java.io.Serializable;
import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.json.bind.config.PropertyOrderStrategy;
import org.bson.Document;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 *
 * @author Herman Barrantes
 */
@JsonbPropertyOrder(PropertyOrderStrategy.ANY)
@Schema(name = "Persona", description = "Representa la información de una persona en el padrón")
public class Persona implements Serializable {

    @Schema(description = "Número de cédula", example = "105480818")
    private final String cedula;
    @Schema(description = "Nombre", example = "LAURA")
    private final String nombre;
    @Schema(description = "Primer apellido", example = "CHINCHILLA")
    private final String primerApellido;
    @Schema(description = "Segundo apellido", example = "MIRANDA")
    private final String segundoApellido;

    public Persona(Document document) {
        this.cedula = document.get(Constantes.CEDULA, String.class);
        this.nombre = document.get(Constantes.NOMBRE, String.class);
        this.primerApellido = document.get(Constantes.PRIMER_APELLIDO, String.class);
        this.segundoApellido = document.get(Constantes.SEGUNDO_APELLIDO, String.class);
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

}
