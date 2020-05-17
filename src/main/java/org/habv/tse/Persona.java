package org.habv.tse;

import java.io.Serializable;
import org.bson.Document;

/**
 *
 * @author Herman Barrantes
 */
public class Persona implements Serializable {

    private final String cedula;
    private final String nombre;
    private final String primerApellido;
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
