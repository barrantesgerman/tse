package org.habv.tse;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.habv.tse.mongodb.Collection;

/**
 *
 * @author Herman Barrantes
 */
@ApplicationScoped
public class ReloadService {

    private final AtomicBoolean reloading;
    private final ExecutorService executorService;

    @Inject
    @ConfigProperty(name = "tse.download")
    private String download;
    @Inject
    @ConfigProperty(name = "tse.zipfile")
    private String zipfile;
    @Inject
    @ConfigProperty(name = "tse.txtfile")
    private String txtfile;

    @Inject
    @Collection("padron")
    private MongoCollection<Document> padron;
    @Inject
    @Collection("bitacora")
    private MongoCollection<Document> bitacora;

    public ReloadService() {
        this.reloading = new AtomicBoolean(false);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @PreDestroy
    private void shutdown() {
        this.executorService.shutdown();
    }

    public void reload() {
        if (reloading.compareAndSet(false, true)) {
            executorService.submit(() -> {
                try {
                    bitacora.drop();
                    delete();
                    download();
                    unzip();
                    bulkLoad();
                    delete();
                } catch (IOException ex) {
                    trace("IOException %s", ex.getMessage());
                } catch (Exception ex) {
                    trace("Exception %s", ex.getMessage());
                }
                reloading.set(false);
            });
        } else {
            throw new ReloadException();
        }
    }

    private void trace(String message, Object... params) {
        try {
            Document trace = new Document()
                    .append("mensaje", String.format(message, params))
                    .append("fecha", LocalDateTime.now(ZoneId.of("-06:00")));
            bitacora.insertOne(trace);
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    private void download() throws IOException {
        trace("Download %s", download);
        URL url = new URL(download);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(zipfile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

    private void unzip() throws IOException {
        trace("Unzip %s", zipfile);
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipfile))) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                if (txtfile.equals(entry.getName())) {
                    try (FileOutputStream fos = new FileOutputStream(entry.getName())) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                entry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }

    private void bulkLoad() throws IOException {
        trace("Bulk Load");
        padron.drop();
        int bulkSize = 200;
        int count = 0;
        List<Document> bulk = new ArrayList<>(bulkSize);
        try (BufferedReader br = new BufferedReader(new FileReader(txtfile, StandardCharsets.ISO_8859_1))) {
            String line;
            while ((line = br.readLine()) != null) {
                count++;
                String[] parts = line.split("\\s*,\\s*");
                Document doc = new Document()
                        .append("cedula", parts[0])
                        .append("nombre", parts[5])
                        .append("primerApellido", parts[6])
                        .append("segundoApellido", parts[7].trim());
                bulk.add(doc);
                if (count == bulkSize) {
                    padron.insertMany(bulk);
                    bulk.clear();
                    count = 0;
                }
            }
        }
        if (!bulk.isEmpty()) {
            padron.insertMany(bulk);
            bulk.clear();
        }
        padron.createIndex(Indexes.hashed("cedula"));
    }

    private void delete() {
        trace("Delete %s %s", txtfile, zipfile);
        new File(txtfile).delete();
        new File(zipfile).delete();
    }
}
