package com.example.backend.configuration.GoogleTagManagerConfig;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.TagManagerScopes;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class GoogleTagManagerConfig {
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    @Deprecated
    private Credential authorize() throws Exception {
        // Load service account key JSON file
        String SERVICES_ACCOUNT = "/services-account.json";
        InputStream serviceAccountStream = Objects.requireNonNull(
                SpringBootApplication.class.getResourceAsStream(SERVICES_ACCOUNT)
        );

        // Create GoogleCredential with the service account
        return GoogleCredential.fromStream(serviceAccountStream)
                .createScoped(TagManagerScopes.all());
    }

    @Bean
    public TagManager initTagManager() throws Exception {
        try {
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            // Authorization flow.
            Credential credential = authorize();
            String APPLICATION_NAME = "Doan65003";
            return new TagManager.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
