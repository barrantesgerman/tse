package org.habv.tse.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author Herman Barrantes
 */
@ApplicationScoped
public class MongoFactory {

    @Inject
    @ConfigProperty(name = "mongodb.uri")
    private String uri;
    @Inject
    @ConfigProperty(name = "mongodb.database")
    private String databaseName;

    private MongoClient client;
    private MongoDatabase database;

    @PostConstruct
    private void init() {
        this.client = MongoClients.create(uri);
        this.database = client.getDatabase(databaseName);
    }
    
    @PreDestroy
    private void end() {
        this.client.close();
    }

    @Produces
    @Collection("")
    public MongoCollection<Document> getCollection(InjectionPoint ip) {
        String collection = ip.getAnnotated().getAnnotation(Collection.class).value();
        return database.getCollection(collection);
    }
}
