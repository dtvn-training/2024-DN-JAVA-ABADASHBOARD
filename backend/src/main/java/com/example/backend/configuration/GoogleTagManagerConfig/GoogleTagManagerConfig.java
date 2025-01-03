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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class GoogleTagManagerConfig {
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    @Value("${google-tag-manager.services_account}")
    private String googleTagServiceAccount;
    @Value("${google-tag-manager.application_name}")
    private String googleTagApplicationName;

    @Deprecated
    private Credential authorize() throws Exception {
        // Load service account key JSON file
        InputStream serviceAccountStream = Objects.requireNonNull(
                SpringBootApplication.class.getResourceAsStream(googleTagServiceAccount)
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
            return new TagManager.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(googleTagApplicationName).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
