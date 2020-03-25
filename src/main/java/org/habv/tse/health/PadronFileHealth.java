package org.habv.tse.health;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

/**
 *
 * @author Herman Barrantes
 */
@Readiness
@ApplicationScoped
public class PadronFileHealth implements HealthCheck {

    @Inject
    @ConfigProperty(name = "tse.download")
    private String download;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("PadronFile");
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(download).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            boolean state = responseCode == 200;
            
            return responseBuilder
                    .withData("file", download)
                    .withData("responseCode", responseCode)
                    .withData("responseMessage", responseMessage)
                    .state(state)
                    .build();
        } catch (IOException ex) {
            return responseBuilder
                    .withData("file", download)
                    .withData("exceptionMessage", ex.getMessage())
                    .down()
                    .build();
        }
    }

}
