package org.habv.tse;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

/**
 *
 * @author Herman Barrantes
 */
@OpenAPIDefinition(
        info = @Info(
                title = "TSE",
                description = "API Restful para consultar el padrón del TSE",
                version = "1.0.0",
                contact = @Contact(
                        name = "Herman Barrantes",
                        email = "barrantesgerman@gmail.com",
                        url = "http://habv.org")
        ),
        servers = {
            @Server(url = "http://localhost:8080/", description = "localhost"),
            @Server(url = "http://mnemosyne:8080/", description = "mnemosyne")
        }
)
@ApplicationPath("/api")
public class TseApplication extends Application {
}
