package com.example.backend.configuration.GoogleAnalyticConfig;

import com.google.analytics.data.v1beta.BetaAnalyticsDataClient;
import com.google.analytics.data.v1beta.BetaAnalyticsDataSettings;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GoogleAnalyticConfig {
    @Value("${google-analytic.services_account}")
    private String serviceAccountKey;

    private GoogleCredentials getGoogleCredentials() throws IOException {
        return GoogleCredentials.fromStream(new FileInputStream(serviceAccountKey))
                .createScoped("https://www.googleapis.com/auth/analytics.readonly");
    }

    @Bean
    public BetaAnalyticsDataClient initBetaAnalyticsDataClient() throws IOException {
        GoogleCredentials credentials = getGoogleCredentials();
        BetaAnalyticsDataSettings betaAnalyticsDataSettings =
                BetaAnalyticsDataSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();
        return BetaAnalyticsDataClient.create(betaAnalyticsDataSettings);
    }
}
