package com.tfs.auth.config;

import com.tfs.auth.model.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.List;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        try {
            // get SSL context ( trust * first )
            SSLContext sslContext = getSSLContext();
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);

            // setting of connection pool
            PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                    .setSSLSocketFactory(socketFactory)
                    .setMaxConnTotal(200) // max total connections
                    .setMaxConnPerRoute(200) // max connections per route
                    .build();

            // build closeable http client with manager
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .build();

            // set connection timeout
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            requestFactory.setConnectionRequestTimeout(Duration.ofSeconds(30));
            requestFactory.setConnectTimeout(Duration.ofSeconds(10));

            RestTemplate restTemplate = new RestTemplate(requestFactory);
            setCharset(restTemplate);

            log.info("Rest template initialized");
            return restTemplate;
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.error("Crete bean: RestTemplate error");
            throw new BaseException("error : " + e.getMessage());
        }
    }

    // Set SSL config if needed
    private SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return SSLContextBuilder.create()
                .loadTrustMaterial((x509Certificates, s) -> true).build();
    }

    // Set charset as UTF-8
    private void setCharset(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> messageConverter : messageConverters) {
            if (messageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) messageConverter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
    }

}
